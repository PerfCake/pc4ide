/*
 *-----------------------------------------------------------------------------
 * pc4ide
 *
 * Copyright 2017 Jakub Knetl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *-----------------------------------------------------------------------------
 */

package org.perfcake.ide.core.model;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.perfcake.ide.core.Field;
import org.perfcake.ide.core.components.ComponentManager;
import org.perfcake.ide.core.components.PropertyField;
import org.perfcake.ide.core.model.director.FieldType;
import org.perfcake.ide.core.model.director.ModelDirector;
import org.perfcake.ide.core.model.director.ModelField;
import org.perfcake.ide.core.model.director.ReflectiveModelDirector;
import org.perfcake.ide.core.utils.TestUtils;

/**
 * Created by jknetl on 9/30/16.
 */
public class ReflectiveModelDirectorTest {

    public static final String VALIDATOR_ID = "validator-1";
    public static final String VALIDATOR_IMPLEMENTATION = "ScriptValidator";
    private ModelDirector validatorDirector;
    private ValidatorModel validator;
    static ComponentManager componentManager;

    @BeforeClass
    public static void beforeClass() {
        componentManager = TestUtils.createComponentManager();
    }

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        validator = new ValidatorModel();
        validator.setId(VALIDATOR_ID);
        validator.setClazz(VALIDATOR_IMPLEMENTATION);

        validatorDirector = new ReflectiveModelDirector(validator, componentManager);
    }

    @Test
    public void fieldsParsed() {
        assertThat(validatorDirector.getDocs(), CoreMatchers.not(Matchers.isEmptyOrNullString()));

        assertThat(validatorDirector.getModelFields(), Matchers.containsInAnyOrder(
                new ModelField(FieldType.SIMPLE, "id", null), new ModelField(FieldType.SIMPLE, "clazz", null)));

        Field idField = validatorDirector.getFieldByName("id");
        assertThat(idField, notNullValue());
        assertThat(idField.getFieldType(), is(FieldType.SIMPLE));
        // documentation is null since there is no documentation for it in perfcake
        // assertThat(idField.getDocs(), CoreMatchers.not(Matchers.isEmptyOrNullString()));

        assertThat(validatorDirector.getCustomPropertyFields(),
                allOf(hasItem(new PropertyField("engine", null, true)), hasItem(new PropertyField("script", null, false))));
    }

    @Test
    public void setFieldsTest() {
        Field idField = validatorDirector.getFieldByName("id");
        assertThat(idField, notNullValue());
        String newId1 = "newId";
        validatorDirector.setField(idField, newId1);
        assertThat(validator.getId(), equalTo(newId1));

        Field engineProperty = validatorDirector.getFieldByName("engine");
        validatorDirector.setField(engineProperty, newId1);

        List<PropertyModel> propertyModel = filterPropertiesByName(validator.getProperty(), "engine");
        assertThat(propertyModel, hasSize(1));
        assertThat(propertyModel.get(0).getValue(), is(newId1));
        Field engineField = validatorDirector.getFieldByName("engine");
        assertThat(validatorDirector.getFieldValue(engineField), is(newId1));

        String newId2 = "newId2";
        validatorDirector.setField(engineProperty, newId2);
        propertyModel = filterPropertiesByName(validator.getProperty(), "engine");
        assertThat(propertyModel, hasSize(1));
        assertThat(propertyModel.get(0).getValue(), is(newId2));

        Field engineField2 = validatorDirector.getFieldByName("engine");
        assertThat(validatorDirector.getFieldValue(engineField2), is(newId2));


        //test boolean value on validation
        ValidationModel validation = new ValidationModel();
        validation.setFastForward(false);
        validation.setEnabled(true);
        ModelDirector validationDirector = new ReflectiveModelDirector(validation, componentManager);

        Field fastForwad = validationDirector.getFieldByName("fastForward");
        assertThat(fastForwad, notNullValue());
        assertThat(validationDirector.getFieldValue(fastForwad), is(false));

        Field enabled = validationDirector.getFieldByName("enabled");
        assertThat(enabled, notNullValue());
        assertThat(validationDirector.getFieldValue(enabled), is(true));

        validationDirector.setField(enabled, false);
        assertThat(validation.isEnabled(), is(false));

    }

    private List<PropertyModel> filterPropertiesByName(List<PropertyModel> properties, String name) {
        List<PropertyModel> result = new ArrayList<>();
        for (PropertyModel p : properties) {
            if (p.getName().equals(name)) {
                result.add(p);
            }
        }

        return result;
    }
}
