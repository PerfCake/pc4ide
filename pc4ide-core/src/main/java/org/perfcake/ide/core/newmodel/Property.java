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

import java.beans.PropertyChangeListener;
import org.perfcake.ide.core.exception.UnsupportedPropertyException;

/**
 * Represents a property of a model.
 *
 * @author Jakub Knetl
 */
public interface Property {
    /**
     * Gets propertyInfo of this property.
     *
     * @return PropertyInfo or null, if the preperty is not part of any model; and thus it has no info associated
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
     * Adds a listener for this property.
     * @param listener listener of the property.
     */
    void addPropertyListener(PropertyChangeListener listener);

    /**
     * Removes a listener for this property.
     * @param listener listener to be removed
     */
    void removePropertyListener(PropertyChangeListener listener);
}
