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
/*
 * PerfClispe
 *
 *
 * Copyright (c) 2014 Jakub Knetl
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
 */

package org.perfcake.ide.core.model;

import java.util.ArrayList;
import java.util.List;

import org.perfcake.model.PropertyType;
import org.w3c.dom.Element;

public class PropertyModel extends AbstractModel {

    public static final String PROPERTY_NAME = "property-name";
    public static final String PROPERTY_VALUE = "property-value";
    private static final String PROPERTY_ANY = "property-any";

    private PropertyType property;

    PropertyModel(PropertyType property) {
        super();
        if (property == null) {
            throw new IllegalArgumentException("Property must not be null");
        }
        this.property = property;
    }

    /**
     * Creates new property model.
     */
    public PropertyModel() {
        super();
        this.property = new PropertyType();
    }

    /**
     * This method should not be used for modifying property (in a way getProperty().setName()))
     * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
     *
     * @return PerfCake model of Property
     */
    PropertyType getProperty() {
        return property;
    }

    public String getName() {
        return property.getName();
    }

    /**
     * Sets property name.
     *
     * @param name property name
     */
    public void setName(String name) {
        final String oldName = getProperty().getName();
        property.setName(name);
        getPropertyChangeSupport().firePropertyChange(PROPERTY_NAME, oldName, name);
    }

    public String getValue() {
        return property.getValue();
    }

    /**
     * Sets property value.
     *
     * @param value property value
     */
    public void setValue(String value) {
        final String oldValue = getProperty().getValue();
        property.setValue(value);
        getPropertyChangeSupport().firePropertyChange(PROPERTY_VALUE, oldValue, value);
    }

    public Element getAny() {
        return property.getAny();
    }

    /**
     * Sets "any" attribute of the property.
     *
     * @param value value of "any" attribute
     */
    public void setAny(Element value) {
        final Element oldAny = getProperty().getAny();
        property.setAny(value);
        getPropertyChangeSupport().firePropertyChange(PROPERTY_ANY, oldAny, value);
    }

    @Override
    public List<AbstractModel> getModelChildren() {
        final List<AbstractModel> children = new ArrayList<>();
        return children;
    }
}
