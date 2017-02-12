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

package org.perfcake.ide.core.newmodel;

import java.beans.PropertyChangeEvent;
import java.util.Objects;

/**
 * Contains information about property of a model.
 *
 * @author Jakub Knetl
 */
public class Property<T extends PropertyRepresentation> {

    /**
     * Info about this property type.
     */
    private PropertyInfo<T> propertyInfo;

    /**
     * current value of the property.
     */
    private T value;

    /**
     * Model object which owns this property value.
     */
    private Model model;

    /**
     * Creates new property
     *
     * @param model        model which owns the property
     * @param propertyInfo information about property type
     * @param value        property value.
     */
    public Property(Model model, PropertyInfo<T> propertyInfo, T value) {
        this.model = model;
        this.propertyInfo = propertyInfo;
        this.value = value;
    }

    /**
     * Sets a new value of the property.
     *
     * @param value new value to be set
     * @throws ClassNotFoundException if implementation class is changed by property and it cannot be found.
     */
    public void setValue(T value) throws ClassNotFoundException {
        T oldValue = this.value;
        this.value = value;

        // if implementation changes it is required to update model to reflect supported properties by new implementation
        if (AbstractModel.IMPLEMENTATION_CLASS_PROPERTY.equals(propertyInfo.getName()) && model != null) {
            model.updateImplementation(String.valueOf(value));
        }

        PropertyChangeEvent event = new PropertyChangeEvent(this, propertyInfo.getName(), oldValue, value);
        if (value instanceof Model) {
            ((Model) value).getPropertyChangeSupport().firePropertyChange(event);
        } else if (model != null) {
            model.getPropertyChangeSupport().firePropertyChange(event);
        }
    }

    public PropertyInfo<T> getPropertyInfo() {
        return propertyInfo;
    }

    public T getValue() {
        return value;
    }

    public void setPropertyInfo(PropertyInfo<T> propertyInfo) {
        this.propertyInfo = propertyInfo;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Property<?> property = (Property<?>) o;
        return Objects.equals(propertyInfo, property.propertyInfo)
               && Objects.equals(value, property.value)
               && Objects.equals(model, property.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyInfo, value, model);
    }

    @Override
    public String toString() {
        return "Property{"
               + "propertyInfo=" + propertyInfo
               + ", value=" + value
               + ", model=" + model
               + '}';
    }
}
