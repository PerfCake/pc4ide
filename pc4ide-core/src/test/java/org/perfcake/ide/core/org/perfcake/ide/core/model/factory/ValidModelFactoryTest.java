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

package org.perfcake.ide.core.org.perfcake.ide.core.model.factory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.perfcake.ide.core.components.ComponentCatalogue;
import org.perfcake.ide.core.components.PerfCakeComponent;
import org.perfcake.ide.core.components.ReflectionComponentCatalogue;
import org.perfcake.ide.core.docs.DocsService;
import org.perfcake.ide.core.docs.DocsServiceImpl;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.components.CorrelatorModel;
import org.perfcake.ide.core.model.components.DestinationModel;
import org.perfcake.ide.core.model.components.GeneratorModel;
import org.perfcake.ide.core.model.components.MessageModel;
import org.perfcake.ide.core.model.components.ReceiverModel;
import org.perfcake.ide.core.model.components.ReporterModel;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.components.SenderModel;
import org.perfcake.ide.core.model.components.SequenceModel;
import org.perfcake.ide.core.model.components.ValidatorModel;
import org.perfcake.ide.core.model.factory.ModelFactory;
import org.perfcake.ide.core.model.factory.ValidModelFactory;
import org.perfcake.ide.core.utils.TestUtils;

/**
 * Tests for {@link org.perfcake.ide.core.model.factory.ValidModelFactory}
 *
 * @author Jakub Knetl
 */
public class ValidModelFactoryTest {

    private ModelFactory factory;

    /**
     * Before method.
     *
     * @throws IOException when docs service cannot be loaded
     */
    @Before
    public void setUp() throws IOException {
        DocsService service = new DocsServiceImpl(TestUtils.loadJavadocProperties());
        ComponentCatalogue catalogue = new ReflectionComponentCatalogue();

        factory = new ValidModelFactory(service, catalogue);
    }

    @Test
    public void testFactory() {
        for (PerfCakeComponent component : PerfCakeComponent.values()) {
            Class<? extends Model> clazz = getModelClazz(component);
            testModel(component, clazz);
        }

    }

    private void testModel(PerfCakeComponent component, Class<? extends Model> modelClazz) {

        Model product = factory.createModel(component);

        assertThat(product, not(nullValue()));
        assertTrue(modelClazz.isAssignableFrom(product.getClass()));
        assertThat(product.isValid(), is(true));

    }

    private Class<? extends Model> getModelClazz(PerfCakeComponent component) {
        Class<? extends Model> clazz = null;
        switch (component) {
            case VALIDATOR:
                clazz = ValidatorModel.class;
                break;
            case SENDER:
                clazz = SenderModel.class;
                break;
            case SCENARIO:
                clazz = ScenarioModel.class;
                break;
            case RECEIVER:
                clazz = ReceiverModel.class;
                break;
            case CORRELATOR:
                clazz = CorrelatorModel.class;
                break;
            case DESTINATION:
                clazz = DestinationModel.class;
                break;
            case GENERATOR:
                clazz = GeneratorModel.class;
                break;
            case MESSAGE:
                clazz = MessageModel.class;
                break;
            case REPORTER:
                clazz = ReporterModel.class;
                break;
            case SEQUENCE:
                clazz = SequenceModel.class;
                break;
            default:
                Assert.fail("Unexpected component");
                break;
        }

        return clazz;
    }

}
