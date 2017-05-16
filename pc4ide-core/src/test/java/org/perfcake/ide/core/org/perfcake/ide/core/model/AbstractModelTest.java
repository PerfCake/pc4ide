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

package org.perfcake.ide.core.org.perfcake.ide.core.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.perfcake.ide.core.docs.DocsService;
import org.perfcake.ide.core.docs.DocsServiceImpl;
import org.perfcake.ide.core.exception.PropertyLimitException;
import org.perfcake.ide.core.exception.UnsupportedPropertyException;
import org.perfcake.ide.core.model.AbstractModel;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.PropertyInfo;
import org.perfcake.ide.core.model.components.GeneratorModel;
import org.perfcake.ide.core.model.components.ReporterModel;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.components.SenderModel;
import org.perfcake.ide.core.model.listeners.ModelListener;
import org.perfcake.ide.core.model.properties.KeyValue;
import org.perfcake.ide.core.model.properties.SimpleValue;
import org.perfcake.ide.core.model.properties.Value;
import org.perfcake.ide.core.utils.TestUtils;

/**
 * Tests for {@link org.perfcake.ide.core.model.AbstractModel}.
 *
 * @author Jakub Knetl
 */
public class AbstractModelTest {

    private DocsService docsService;
    private AbstractModel model;

    /**
     * Sets up tests.
     *
     * @throws IOException when there is problem in reading javadoc properties.
     */
    @Before
    public void setUp() throws IOException {
        docsService = new DocsServiceImpl(TestUtils.loadJavadocProperties());
    }

    @Test
    public void testSupportedProperties() {
        model = new SenderModel(docsService);
        assertThat(model.getProperties("method"), nullValue());
        PropertyInfo implProperty = model.getSupportedProperty(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY);
        model.addProperty(implProperty, new SimpleValue("HttpSender"));
        assertThat(model.getProperties("method"), empty());
    }

    @Test
    public void testAddProperty() {
        model = new ScenarioModel(docsService);

        PropertyInfo generatorInfo = model.getSupportedProperty(ScenarioModel.PropertyNames.GENERATOR.toString());
        assertThat(model.getProperties(generatorInfo), empty());

        Model generatorModel = new GeneratorModel(docsService);
        assertThat(generatorModel.getModel(), nullValue());
        assertThat(generatorModel.getPropertyInfo(), nullValue());
        model.addProperty(generatorInfo, generatorModel);
        assertThat(generatorModel.getModel(), is(model));
        assertThat(generatorModel.getPropertyInfo(), is(generatorInfo));
        assertThat(model.getProperties(generatorInfo).get(0), is(generatorModel));

        try {
            model.addProperty(generatorInfo, generatorModel); // adding generator again should fail
            fail("Possible to add multiple generators");
        } catch (PropertyLimitException e) {
            // ok, expected behviour
        }

        try {
            model.removeProperty(generatorInfo, generatorModel); // should fail because minimum number of generators is 1
            fail("Possible to remove generator");
        } catch (PropertyLimitException e) {
            // ok, expected behaviour
        }

        try {
            model.addProperty(generatorInfo, new SimpleValue("value"));
            fail("Possible to add different type of property");
        } catch (UnsupportedPropertyException e) {
            // ok, expected behaviour
        }

        Model reporter1 = new ReporterModel(docsService);
        Model reporter2 = new ReporterModel(docsService);
        PropertyInfo implInfo = reporter1.getSupportedProperty(ReporterModel.PropertyNames.IMPLEMENTATION.toString());
        PropertyInfo implInfo2 = reporter2.getSupportedProperty(ReporterModel.PropertyNames.IMPLEMENTATION.toString());
        reporter1.addProperty(implInfo, new SimpleValue("MemoryUsageReporter"));
        reporter2.addProperty(implInfo2, new SimpleValue("RawReporter"));

        PropertyInfo reporterInfo = model.getSupportedProperty(ScenarioModel.PropertyNames.REPORTERS.toString());
        assertThat(model.getProperties(reporterInfo), empty());
        model.addProperty(reporterInfo, reporter1);
        model.addProperty(reporterInfo, reporter2);
        assertThat(model.getProperties(reporterInfo), containsInAnyOrder(reporter1, reporter2));
        model.removeProperty(reporterInfo, reporter1);
        assertThat(model.getProperties(reporterInfo), containsInAnyOrder(reporter2));
    }

    @Test
    public void testGetProperties() {
        model = new ScenarioModel(docsService);

        PropertyInfo info = model.getSupportedProperty(ScenarioModel.PropertyNames.GENERATOR.toString());
        PropertyInfo wrongInfo = PropertyInfo.createValueInfo(info.getName() + "wrong", info.getDisplayName(), info.getModel(),
                1, 1, info.getValueDataType(), null);
        assertThat(model.getProperties("non-existent-property"), nullValue());
        assertThat(model.getProperties(info), empty());
        assertThat(model.getProperties(wrongInfo), nullValue());
        assertThat(model.getProperties(info).isEmpty(), equalTo(true));

        Model generator = new GeneratorModel(docsService);
        model.addProperty(info, generator);
        assertThat(model.getProperties(info).isEmpty(), equalTo(false));
    }

    @Test
    public void testGetSingleProperty() {

        model = new ScenarioModel(docsService);

        PropertyInfo info = model.getSupportedProperty(ScenarioModel.PropertyNames.GENERATOR.toString());

        Model generator = new GeneratorModel(docsService);
        model.addProperty(info, generator);
        assertThat(model.getSingleProperty(ScenarioModel.PropertyNames.GENERATOR.toString(), Model.class), equalTo(generator));

        try {
            model.getSingleProperty(ScenarioModel.PropertyNames.GENERATOR.toString(), Value.class);
            fail("UnsupportedPropertyException expected!");
        } catch (UnsupportedPropertyException e) {
            //OK, expected behaviour
        }

        assertThat(model.getSingleProperty("wrong-name", KeyValue.class), nullValue());
    }

    @Test
    public void testModifyListeners() {
        model = new ScenarioModel(docsService);

        // add mock listeners
        ModelListener listener1 = mock(ModelListener.class);
        ModelListener listener2 = mock(ModelListener.class);
        model.addModelListener(listener1);
        model.addModelListener(listener2);

        Model reporter = new ReporterModel(docsService);
        PropertyInfo reporterInfo = model.getSupportedProperty(ScenarioModel.PropertyNames.REPORTERS.toString());

        // add property
        model.addProperty(reporterInfo, reporter);

        // remove first listener
        model.removeModelListener(listener1);

        // remove property
        model.removeProperty(reporterInfo, reporter);

        verify(listener1, times(1)).propertyChange(anyObject());
        verify(listener2, times(2)).propertyChange(anyObject());
    }

    @Test
    public void testPropertyEventsPropagation() {
        model = new ScenarioModel(docsService);

        // add property
        Model generator = new GeneratorModel(docsService);
        PropertyInfo generatorInfo = model.getSupportedProperty(ScenarioModel.PropertyNames.GENERATOR.toString());
        model.addProperty(generatorInfo, generator);

        PropertyInfo threadsInfo = generator.getSupportedProperty(GeneratorModel.PropertyNames.THREADS.toString());
        generator.addProperty(threadsInfo, new SimpleValue("10"));

        // add mock listeners
        ModelListener listener1 = mock(ModelListener.class);
        generator.addModelListener(listener1);

        verify(listener1, never()).propertyChange(anyObject());
        generator.getProperties(GeneratorModel.PropertyNames.THREADS.toString()).get(0).cast(Value.class).setValue("20");
        verify(listener1).propertyChange(anyObject());
    }
}
