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

import org.perfcake.ide.core.exception.UnsupportedPropertyException;
import org.perfcake.ide.core.model.listeners.PropertyListener;
import org.perfcake.ide.core.model.validation.error.ValidationError;

/**
 * Represents a property of a model.
 *
 * @author Jakub Knetl
 */
public interface Property {
    /**
     * Gets propertyInfo of this property.
     *
     * @return PropertyInfo or null, if the property is not part of any model; and thus it has no info associated
     */
    PropertyInfo getPropertyInfo();

    /**
     * Casts the property value safely, possibly to more specific type.
     *
     * @param <T>   expected type of a value (must be same type or supertype of the type defined in the propertyInfo of this property)
     * @param clazz clazz which represents type of a property value
     * @return property value
     * @throws UnsupportedPropertyException if this property represents another than requested, thus it is not possible to cast property
     *      to the requested type.
     */
    <T extends Property> T cast(Class<T> clazz) throws UnsupportedPropertyException;

    void setPropertyInfo(PropertyInfo propertyInfo);

    /**
     * Gets Model which owns this property.
     *
     * @return Model which owns this property or null, if the preperty is not part of any model; and thus it has no model associated.
     */
    Model getModel();

    /**
     * Sets a model which owns this property.
     * @param model model which owns this property.
     */
    void setModel(Model model);

    /**
     * This method performs validation of this property and determines if property is valid.
     * @return true if and only if the content of a property is valid.
     */
    boolean isValid();

    /**
     * This method performs validation of this property and in case that property is invalid it returns error
     * description.
     * @return validation error object or null if this property is valid.
     */
    ValidationError getValidationError();

    /**
     * Adds a listener for this property.
     * @param listener listener of the property.
     */
    void addPropertyListener(PropertyListener listener);

    /**
     * Removes a listener for this property.
     * @param listener listener to be removed
     */
    void removePropertyListener(PropertyListener listener);
}
