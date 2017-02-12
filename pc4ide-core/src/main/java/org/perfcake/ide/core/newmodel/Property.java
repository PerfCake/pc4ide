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
import org.perfcake.ide.core.exception.ModelException;

/**
 * Contains information about property of a model.
 *
 * @author Jakub Knetl
 */
public class Property {

    /**
     * Info about this property type.
     */
    private PropertyInfo propertyInfo;

    /**
     * current value of the property.
     */
    private Object value;

    /**
     * Model object which owns this property value.
     */
    private Model model;

    /**
     * Creates new property
     *
     * @param <T>          Type of a property value, the value type must be compatible with type defined in propertyInfo
     * @param model        model which owns the property
     * @param propertyInfo information about property type
     * @param value        property value.
     * @throws ModelException if you try to assign value which is different from expected value which is defined in propertyInfo parameter.
     */
    public <T extends PropertyRepresentation> Property(Model model, PropertyInfo propertyInfo, T value)
            throws ModelException {
        if (model == null) {
            throw new IllegalArgumentException("Model must not be null");
        }
        if (propertyInfo == null) {
            throw new IllegalArgumentException("PropertyInfo must not be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }

        if (!propertyInfo.getType().getClazz().isAssignableFrom(value.getClass())) {
            throw new ModelException(String.format("Type of property value (%s) does not conform property type (%s)",
                    value.getClass().getCanonicalName(), propertyInfo.getType().getClazz().getCanonicalName()));
        }
        this.model = model;
        this.propertyInfo = propertyInfo;
        this.value = value;
    }

    /**
     * Sets a new value of the property.
     *
     * @param <T>   type of the property
     * @param value new value to be set
     * @throws ClassNotFoundException if implementation class is changed by property and it cannot be found.
     */
    public <T> void setValue(T value) throws ClassNotFoundException {

        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }

        if (!propertyInfo.getType().getClazz().isAssignableFrom(value.getClass())) {
            throw new ModelException(String.format("Type of property value (%s) does not conform property type (%s)",
                    value.getClass().getCanonicalName(), propertyInfo.getType().getClazz().getCanonicalName()));
        }
        Object oldValue = this.value;
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

    public PropertyInfo getPropertyInfo() {
        return propertyInfo;
    }

    /**
     * gets value of a property.
     *
     * @param <T>   expected type of a value (must be same type or supertype of the type defined in the propertyInfo of this property)
     * @param clazz clazz which represents type of a property value
     * @return property value
     */
    public <T> T getValue(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null.");
        }

        if (!clazz.isAssignableFrom(propertyInfo.getType().getClazz())) {
            throw new ModelException(String.format("Type of property value (%s) does not conform property type (%s)",
                    clazz.getCanonicalName(), propertyInfo.getType().getClazz().getCanonicalName()));
        }
        return clazz.cast(value);
    }

    public void setPropertyInfo(PropertyInfo propertyInfo) {
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
        Property property = (Property) o;
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
