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
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.perfcake.ide.core.components.Component;
import org.perfcake.ide.core.components.ComponentKind;
import org.perfcake.ide.core.components.ComponentManager;
import org.perfcake.ide.core.components.PropertyField;
import org.perfcake.ide.core.exception.PropertyLimitException;


/**
 * Represents model of a PerfCake component.
 *
 * @author Jakub Knetl
 */
public abstract class Model {

    public static String IMPLEMENTATION_CLASS_PROPERTY = "implementation-class";

    public static String IMPLEMENTATION_PROPERTY = "implementation-property";

    /**
     * Map of the component properties.
     */
    private HashMap<PropertyInfo<?>, List<Property<?>>> properties;

    /**
     * Component implementation class.
     */
    private Class implementation;

    /**
     * Component API (interface or abstract class).
     */
    private Class api;

    /**
     * Component manager which is used for quering information about component.
     */
    private ComponentManager componentManager;

    /**
     * Support for sending property change events.
     */
    private PropertyChangeSupport pcs;

    /**
     * Creates new model of PerfCake component.
     *
     * @param componentManager PerfCake component manager
     * @param api              Interface or abstract class of PerfCake component
     */
    public Model(ComponentManager componentManager, Class api) {
        if (componentManager == null) {
            throw new IllegalArgumentException("componentManager must not be null");
        }
        if (componentManager == null) {
            throw new IllegalArgumentException("API must not be null");
        }

        this.componentManager = componentManager;
        this.api = api;
        this.properties = new HashMap<>();
    }

    /**
     * @return Set of properties which are supported in this model.
     */
    public Set<PropertyInfo<?>> getSupportedProperties() {

        return properties.keySet();
    }

    /**
     * @param propertyInfo property to get
     * @return List of properties in the component which has  given property info.
     */
    public List<Property<?>> getProperty(PropertyInfo<?> propertyInfo) {
        if (propertyInfo == null) {
            throw new IllegalArgumentException("propertyInfo must not be null");
        }

        return properties.get(propertyInfo);
    }

    /**
     * Add new property.
     *
     * @param propertyInfo type of a property
     * @param property     property To be added
     * @param <T> type of the property value
     * @throws PropertyLimitException if you try to add a property which has maximum number of occurrences used already.
     */
    public <T> void addProperty(PropertyInfo<? extends T> propertyInfo, Property<? extends T> property) throws PropertyLimitException {
        if (propertyInfo == null) {
            throw new IllegalArgumentException("propertyInfo must not be null");
        }
        if (property == null) {
            throw new IllegalArgumentException("property must not be null");
        }

        List<Property<?>> existingProperties = properties.get(propertyInfo);

        if (existingProperties.size() == propertyInfo.getMaxOccurs()) {
            throw new PropertyLimitException("Property limit exceeded.");
        }

        existingProperties.add(property);
        pcs.firePropertyChange(new PropertyChangeEvent(this, propertyInfo.getName(), null, property));

    }

    /**
     * Removes a property.
     *
     * @param propertyInfo type of a property
     * @param property     property To be added
     * @param <T> type of the property value
     * @return true if the property was removed.
     * @throws PropertyLimitException if you try to remove property, which cannot be removed due to required minimum occurrences.
     */
    public <T> boolean removeProperty(PropertyInfo<? extends T> propertyInfo, Property<? extends T> property)
            throws PropertyLimitException {
        if (propertyInfo == null) {
            throw new IllegalArgumentException("propertyInfo must not be null");
        }
        if (property == null) {
            throw new IllegalArgumentException("property must not be null");
        }

        List<Property<?>> existingProperties = properties.get(propertyInfo);

        if (existingProperties.size() == propertyInfo.getMinOccurs()) {
            throw new PropertyLimitException("Property limit decreased under minimum value.");
        }

        boolean removed = existingProperties.remove(property);
        pcs.firePropertyChange(new PropertyChangeEvent(this, propertyInfo.getName(), property, null));

        return removed;
    }

    /**
     * Gets the property change support class.
     *
     * @return listeners
     */
    public PropertyChangeSupport getPropertyChangeSupport() {
        return pcs;
    }

    /**
     * Adds a listener.
     *
     * @param listener listener to be added.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Remove a listener.
     *
     * @param listener listener to be removed
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


    /**
     * Updates implementation class of given model. It effectively changes supported properties.
     *
     * @param clazz fully qualified name of the new implementation class.
     * @throws ClassNotFoundException if implementation cannot cannot be found.
     */
    public void updateImplementation(String clazz) throws ClassNotFoundException {
        Class newImplementation = Class.forName(clazz);
        ComponentKind kind = ComponentKind.getComponentKindByComponentClass(api);
        List<Component> components = componentManager.getComponentImplementations(kind);


        // remove old properties
        Iterator<Entry<PropertyInfo<?>, List<Property<?>>>> it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Entry<PropertyInfo<?>, List<Property<?>>> entry = it.next();
            if (IMPLEMENTATION_PROPERTY.equals(entry.getKey().getName())) {
                it.remove();
            }
        }

        // add new properties
        List<PropertyField> newImplementationProperties = null;
        for (Component c : components) {
            if (newImplementation.equals(c.getImplementation())) {
                newImplementationProperties = c.getPropertyFields();

                for (PropertyField f : newImplementationProperties) {
                    int minOccurs = (f.isMandatory()) ? 1 : 0;

                    //TODO(jknetl): get default value!
                    PropertyInfo<String> implementationPropertyInfo =
                            new PropertyInfo<>(IMPLEMENTATION_PROPERTY, String.class, "", minOccurs, -1);

                    List<Property<?>> values = Collections.emptyList();

                    properties.put(implementationPropertyInfo, values);

                }
                break;
            }
        }
    }
}
