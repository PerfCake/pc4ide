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

package org.perfcake.ide.core.components;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.notNullValue;

import static org.junit.Assert.assertThat;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.perfcake.ide.core.utils.TestUtils;
import org.perfcake.message.generator.DefaultMessageGenerator;
import org.perfcake.message.receiver.HttpReceiver;
import org.perfcake.message.sender.HttpSender;
import org.perfcake.message.sequence.NumberSequence;
import org.perfcake.reporting.destination.Log4jDestination;
import org.perfcake.reporting.reporter.WarmUpReporter;
import org.perfcake.validation.DictionaryValidator;


/**
 * This tests verify that parsing PerfCake components works.
 * @author jknetl
 */
public class ComponentParsingSmokeTest {

    private ComponentManager manager;

    @Before
    public void setUp() throws FileNotFoundException {
        manager = TestUtils.createComponentManager();
    }

    @Test
    public void testParsingComponents() {
        Assert.assertEquals(ComponentKind.values().length, manager.getComponentsMap().size());

        int i = 0;
        for (final ComponentKind kind : ComponentKind.values()) {
            if (kind.isAbstract()) {
                i++;
            }
        }
        Assert.assertEquals(i, manager.getComponentImplementationsMap().size());

        // check that each abstract component contains some implementation
        assertComponentsListContains(manager, ComponentKind.SENDER, HttpSender.class);
        assertComponentsListContains(manager, ComponentKind.GENERATOR, DefaultMessageGenerator.class);
        assertComponentsListContains(manager, ComponentKind.RECEIVER, HttpReceiver.class);
        assertComponentsListContains(manager, ComponentKind.SEQUENCE, NumberSequence.class);
        assertComponentsListContains(manager, ComponentKind.REPORTER, WarmUpReporter.class);
        assertComponentsListContains(manager, ComponentKind.DESTINATION, Log4jDestination.class);
        assertComponentsListContains(manager, ComponentKind.VALIDATOR, DictionaryValidator.class);
    }

    @Test
    public void testJavadocLoading() {
        //test that some javadoc is loaded

        //Assert abstract sender has a documentation
        final Component senderComponent = manager.getComponent(ComponentKind.SENDER);
        Assert.assertTrue(senderComponent.getDocumentation().length() > 0);

        //Assert http sender has documentation and also its field has documentation
        final Component httpSender = getComponentByImplementation(manager, ComponentKind.SENDER, HttpSender.class);
        Assert.assertNotNull(httpSender);
        final PropertyField urlField = getPropertyField(httpSender, "url");
        Assert.assertNotNull(urlField);
        Assert.assertTrue(urlField.getDocs().length() > 0);
    }

    @Test
    public void testAbstractComponentLoading() {
        //test that some javadoc is loaded

        //Assert abstract sender has a documentation
        final Component generatorInterface = manager.getComponent(ComponentKind.GENERATOR);
        assertThat(generatorInterface.getDocumentation(), not(isEmptyOrNullString()));

        //Assert that generator has thread and clazz fields
        final PropertyField urlField = getPropertyField(generatorInterface, "threads");
        assertThat(urlField, notNullValue());
        assertThat(urlField.getDocs(), not(isEmptyOrNullString()));
    }

    /**
     * Gets component by implementation.
     * @param manager component manager
     * @param kind kind of the componetn
     * @param implementation implementation of the component
     * @return Return component with given implementation or null if no such component found
     */
    public static Component getComponentByImplementation(ComponentManager manager, ComponentKind kind, Class<?> implementation) {
        final List<Component> list = manager.getComponentImplementations(kind);

        Component component = null;
        for (final Component c : list) {
            if (implementation.equals(c.getImplementation())) {
                component = c;
            }
        }

        return component;
    }

    public static void assertComponentsListContains(ComponentManager manager, ComponentKind kind, Class<?> clazz) {
        Assert.assertNotNull(getComponentByImplementation(manager, kind, clazz));
    }

    /**
     * Finds property field by name.
     *
     * @param component component whose property field is wanted
     * @param fieldName name of the field
     * @return Property field if found, or null if no such field can be found
     */
    public static PropertyField getPropertyField(Component component, String fieldName) {
        PropertyField field = null;

        if (component == null || fieldName == null) {
            return null;
        }

        for (final PropertyField f : component.getPropertyFields()) {
            if (fieldName.equals(f.getName())) {
                field = f;
            }
        }

        return field;
    }

}
