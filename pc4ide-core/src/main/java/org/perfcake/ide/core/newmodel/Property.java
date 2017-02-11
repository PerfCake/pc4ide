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
public class Property<T> {

    /**
     * Info about this property type.
     */
    private PropertyType<T> propertyType;

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
     * @param propertyType information about property type
     * @param value        property value.
     */
    public Property(Model model, PropertyType<T> propertyType, T value) {
        this.model = model;
        this.propertyType = propertyType;
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
        if (Model.IMPLEMENTATION_CLASS_PROPERTY.equals(propertyType.getName())) {
            model.updateImplementation(String.valueOf(value));
        }

        PropertyChangeEvent event = new PropertyChangeEvent(this, propertyType.getName(), oldValue, value);
        if (value instanceof Model) {
            ((Model) value).getPropertyChangeSupport().firePropertyChange(event);
        } else {
            model.getPropertyChangeSupport().firePropertyChange(event);
        }
    }

    public PropertyType<T> getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType<T> propertyType) {
        this.propertyType = propertyType;
    }

    public T getValue() {
        return value;
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
        return Objects.equals(propertyType, property.propertyType)
               && Objects.equals(value, property.value)
               && Objects.equals(model, property.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyType, value, model);
    }

    @Override
    public String toString() {
        return "Property{"
               + "propertyType=" + propertyType
               + ", value=" + value
               + ", model=" + model
               + '}';
    }
}
