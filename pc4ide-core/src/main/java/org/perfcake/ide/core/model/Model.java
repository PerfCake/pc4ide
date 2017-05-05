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

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.perfcake.ide.core.components.PerfCakeComponent;
import org.perfcake.ide.core.docs.DocsService;
import org.perfcake.ide.core.exception.ImplementationNotFoundException;
import org.perfcake.ide.core.exception.PropertyLimitException;
import org.perfcake.ide.core.exception.UnsupportedPropertyException;
import org.perfcake.ide.core.model.listeners.ModelListener;

/**
 * Represents a model object of a PerfCake inspector. Model maintains supported properties and their values.
 *
 * @author Jakub Knetl
 */
public interface Model extends Property {

    /**
     * @return Unmodifiable set of all properties which are supported in this model.
     */
    Set<PropertyInfo> getSupportedProperties();

    /**
     * @return Unmodifiable set of properties which are supported by this model because of current implementation.
     */
    Set<PropertyInfo> getSupportedImplProperties();

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
     * Adds property to the model.
     *
     * @param supportedPropertyName name of supported property
     * @param property     Property to be added
     * @throws PropertyLimitException       If the maximum number of properties for given type are present in the model.
     * @throws UnsupportedPropertyException If the no supported property match suppertedPropertyName argument
     */
    void addProperty(String supportedPropertyName, Property property) throws PropertyLimitException, UnsupportedPropertyException;

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
     * Returns an unmodifiable list of properties of given type in this model.
     *
     * @param propertyInfo type of the properties
     * @return <em>unmodifiable list</em> of properties of given type in this model. If given propertyInfo is not supported,
     *     then null is returned.
     */
    List<Property> getProperties(PropertyInfo propertyInfo);

    /**
     * Returns an unmodifiable list of properties of given type in this model.
     *
     * @param supportedPropertyName Name of property type which is supported by this model
     * @return <em>unmodifiable list</em> of properties of given type in this model. If given property name is not supported,
     *     then null is returned.
     */
    List<Property> getProperties(String supportedPropertyName);

    /**
     * Gets single property for supported property with given name.
     *
     * @param supportedPropertyName name of the supported property.
     * @param type type of an expected property
     * @param <T> Type of the property
     * @throws UnsupportedPropertyException if property is found but it has different type so that it cannot be casted.
     * @return Property or null, if there is no such property.
     */
    <T extends Property> T getSingleProperty(String supportedPropertyName, Class<? extends T> type) throws UnsupportedPropertyException;


    /**
     * Determines whether the properties for given property info is empty.
     *
     * @param propertyInfo type of supported property
     * @return True if there is no property for given supported property type
     * @throws UnsupportedPropertyException if the property is not supported
     */
    boolean isEmpty(PropertyInfo propertyInfo) throws UnsupportedPropertyException;

    /**
     * Determines number of properties of given type in the model.
     *
     * @param propertyInfo type of supported property
     * @return number of properties of given type in the model
     * @throws UnsupportedPropertyException if the property is not supported
     */
    int size(PropertyInfo propertyInfo) throws UnsupportedPropertyException;

    /**
     * Obtains a documentation service.
     *
     * @return Documentation service.
     */
    DocsService getDocsService();

    /**
     * Adds a listener to the model.
     *
     * @param listener listener to be added.
     */
    void addModelListener(ModelListener listener);

    /**
     * Remove a listener to the model.
     *
     * @param listener listener to be removed
     */
    void removeModelListener(ModelListener listener);

    /**
     * @return return kind of PerfCake component which is represented by this model.
     */
    PerfCakeComponent getComponent();

    /**
     * Updates implementation class of given model. This method is intended to update list
     * of supported properties, because different implementation can support different properties.
     *
     * @param clazz fully qualified name of the new implementation class.
     * @throws ImplementationNotFoundException if implementation cannot cannot be found.
     */
    void updateImplementation(String clazz) throws ImplementationNotFoundException;
}
