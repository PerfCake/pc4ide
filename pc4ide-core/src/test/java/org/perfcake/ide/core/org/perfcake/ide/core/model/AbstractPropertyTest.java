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

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;

import java.beans.PropertyChangeListener;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.perfcake.ide.core.exception.UnsupportedPropertyException;
import org.perfcake.ide.core.model.AbstractProperty;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.listeners.PropertyListener;
import org.perfcake.ide.core.model.properties.KeyValue;
import org.perfcake.ide.core.model.properties.SimpleValue;
import org.perfcake.ide.core.model.properties.Value;

/**
 * Tests of Property.
 *
 * @author Jakub Knetl
 */
public class AbstractPropertyTest {

    private AbstractProperty property;

    @Test
    public void testFieldsValue() {
        property = new SimpleValue("value");

        assertThat(property.getPropertyInfo(), nullValue());
        assertThat(property.getModel(), nullValue());
    }

    @Test
    public void testCasts() {

        property = new SimpleValue("value");


        try {
            property.cast(KeyValue.class);
            Assert.fail("it is possible to cast Value to Key value.");
        } catch (UnsupportedPropertyException e) {
            //OK, expected exception
        }

        try {
            property.cast(Model.class);
            Assert.fail("it is possible to cast Value to Model.");
        } catch (UnsupportedPropertyException e) {
            //OK, expected exception
        }

        // should be ok
        property.cast(Value.class);
    }

    @Test
    public void testListeners() {
        property = new SimpleValue("value");

        PropertyListener listener1 = mock(PropertyListener.class);
        PropertyListener listener2 = mock(PropertyListener.class);
        PropertyListener listener3 = mock(PropertyListener.class);

        property.addPropertyListener(listener1);
        property.addPropertyListener(listener2);
        property.addPropertyListener(listener3);

        property.removePropertyListener(listener2);

        property.cast(Value.class).setValue("new-value");

        Mockito.verify(listener1).propertyChange(anyObject());
        Mockito.verify(listener2, Mockito.never()).propertyChange(anyObject());
        Mockito.verify(listener3).propertyChange(anyObject());
    }
}
