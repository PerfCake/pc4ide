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
import java.util.Set;

/**
 * Represents a model object of a PerfCake component.
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
     * @param propertyInfo property to get
     * @return Container of properties for given type.
     */
    PropertyContainer getPropertyContainer(PropertyInfo propertyInfo);

    /**
     * Finds a property based on its name.
     *
     * @param propertyName Name of the property
     * @return PropertyContainer which holds information about properties with given name, or null, if no such property can be found.
     */
    PropertyContainer getPropertyContainer(String propertyName);


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
