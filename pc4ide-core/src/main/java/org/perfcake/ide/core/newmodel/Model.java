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
import java.beans.PropertyChangeSupport;
import java.util.Iterator;
import java.util.Set;
import org.perfcake.ide.core.exception.PropertyLimitException;
import org.perfcake.ide.core.exception.UnsupportedPropertyException;
import org.perfcake.ide.core.newmodel.PropertyContainerImpl.PropertyIterator;

/**
 * Represents a model object of a PerfCake component. Model maintains supported properties and their values.
 *
 * @author Jakub Knetl
 */
public interface Model extends PropertyRepresentation {

    /**
     * @return Set of properties which are supported in this model.
     */
    Set<PropertyInfo> getSupportedProperties();

    /**
     * Finds supported property type by name.
     *
     * @param name name of the property
     * @return Type of the supported property with the name or null, if no property with the name was found.
     */
    PropertyInfo getSupportedProperty(String name);

    /**
     * Adds property to the model.
     *
     * @param propertyInfo metadata about type of the property to be added
     * @param property     Property to be added
     * @throws PropertyLimitException       If the maximum number of properties for given type are present in the model.
     * @throws UnsupportedPropertyException If the property is not supported by this model.
     */
    void addProperty(PropertyInfo propertyInfo, Property property) throws PropertyLimitException, UnsupportedPropertyException;

    /**
     * Removes property to the model.
     *
     * @param propertyInfo metadata about type of the property to be removed.
     * @param property     the property to be removed.
     * @return true if the property was removed, false if no such property has been found.
     * @throws PropertyLimitException       If the minimum number of properties of this type are in the model.
     * @throws UnsupportedPropertyException If the property is not supported by this model.
     */
    boolean removeProperty(PropertyInfo propertyInfo, Property property) throws PropertyLimitException, UnsupportedPropertyException;

    /**
     * Gets an iterator for properties of given type.
     *
     * @param propertyInfo Metadata about type of the properties for which iterator should be returned.
     * @return Iterator over the properties, or null if no such propertyInfo is supported by this model.
     */
    Iterator<Property> propertyIterator(PropertyInfo propertyInfo);

    /**
     * Gets the property change support class.
     *
     * @return listeners
     */
    PropertyChangeSupport getPropertyChangeSupport();

    /**
     * Adds a listener.
     *
     * @param listener listener to be added.
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Remove a listener.
     *
     * @param listener listener to be removed
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Updates implementation class of given model. This method is intended to update list
     * of supported properties, because different implementation can support different properties.
     *
     * @param clazz fully qualified name of the new implementation class.
     * @throws ClassNotFoundException if implementation cannot cannot be found.
     */
    void updateImplementation(String clazz) throws ClassNotFoundException;
}
