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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.perfcake.ide.core.components.Component;
import org.perfcake.ide.core.components.ComponentKind;
import org.perfcake.ide.core.components.ComponentManager;
import org.perfcake.ide.core.components.PropertyField;
import org.perfcake.ide.core.newmodel.simple.Value;


/**
 * Represents model of a PerfCake component.
 *
 * @author Jakub Knetl
 */
public abstract class AbstractModel implements Model {

    public static final String IMPLEMENTATION_CLASS_PROPERTY = "Implementation";

    public static final String IMPLEMENTATION_PROPERTY = "implementation-property";

    /**
     * Map of the component properties.
     */
    private HashMap<PropertyInfo, PropertyContainer<? extends PropertyRepresentation>> properties;

    /**
     * Component API (interface or abstract class).
     */
    private Class<? extends PropertyRepresentation> api;

    /**
     * Component manager which is used for querying information about component.
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
    public AbstractModel(ComponentManager componentManager, Class api) {
        if (componentManager == null) {
            throw new IllegalArgumentException("componentManager must not be null");
        }
        if (api == null) {
            throw new IllegalArgumentException("API must not be null");
        }

        this.componentManager = componentManager;
        this.api = api;
        this.properties = new HashMap<>();
        this.pcs = new PropertyChangeSupport(this);
        initializeSupportedProperties();
    }

    @Override
    public Set<PropertyInfo> getSupportedProperties() {

        return properties.keySet();
    }

    @Override
    public PropertyInfo getSupportedProperty(String name) {

        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }

        PropertyInfo result = null;

        for (PropertyInfo type : properties.keySet()) {
            if (name.equals(type.getName())) {
                result = type;
                break;
            }
        }

        return result;
    }

    @Override
    public PropertyContainer<? extends PropertyRepresentation> getProperty(PropertyInfo propertyInfo) {
        if (propertyInfo == null) {
            throw new IllegalArgumentException("propertyInfo must not be null");
        }

        return properties.get(propertyInfo);
    }

    @Override
    public PropertyContainer<? extends PropertyRepresentation> getProperty(String propertyName) {

        if (propertyName == null) {
            throw new IllegalArgumentException("Property name cannot be null.");
        }

        PropertyContainer<? extends PropertyRepresentation> result = null;

        for (PropertyInfo type : properties.keySet()) {
            if (propertyName.equals(type.getName())) {
                result = properties.get(type);
                break;
            }
        }

        return result;
    }

    @Override
    public PropertyChangeSupport getPropertyChangeSupport() {
        return pcs;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    @Override
    public void updateImplementation(String clazz) throws ClassNotFoundException {
        Class newImplementation = Class.forName(clazz);
        ComponentKind kind = ComponentKind.getComponentKindByComponentClass(api);
        List<Component> components = componentManager.getComponentImplementations(kind);


        // remove old properties
        Iterator<Entry<PropertyInfo, PropertyContainer<? extends PropertyRepresentation>>> it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Entry<PropertyInfo, PropertyContainer<? extends PropertyRepresentation>> entry = it.next();
            if (IMPLEMENTATION_PROPERTY.equals(entry.getKey().getName())) {
                it.remove();
            }
        }

        // add new properties
        List<PropertyField> newImplementationProperties;
        for (Component c : components) {
            if (newImplementation.equals(c.getImplementation())) {
                newImplementationProperties = c.getPropertyFields();

                for (PropertyField f : newImplementationProperties) {
                    int minOccurs = (f.isMandatory()) ? 1 : 0;

                    //TODO(jknetl): get default value!
                    PropertyInfo implementationPropertyInfo =
                            new PropertyInfo(IMPLEMENTATION_PROPERTY, Value.class, null, minOccurs, -1);

                    properties.put(implementationPropertyInfo, new PropertyContainer<>(this, implementationPropertyInfo));

                }
                break;
            }
        }
    }

    /**
     * This method is called from constructor. It initializes supported properties defined in component type within the model. This
     * method should not initialize implementation properties. These are handled automatically.
     */
    protected abstract void initializeSupportedProperties();

    /**
     * Adds supported property.
     *
     * @param propertyInfo Property to be added as supported.
     */
    protected void addSupportedProperty(PropertyInfo propertyInfo) {
        if (propertyInfo == null) {
            throw new IllegalArgumentException("Property type must not be null.");
        }

        properties.put(propertyInfo, new PropertyContainer<>(this, propertyInfo));
    }

    /**
     * Adds multiple supported properties.
     *
     * @param propertyInfos Properties to be added as supported.
     */
    protected void addSupportedProperties(PropertyInfo... propertyInfos) {
        if (propertyInfos == null) {
            throw new IllegalArgumentException("Property type must not be null.");
        }

        for (PropertyInfo propertyInfo : propertyInfos) {
            addSupportedProperty(propertyInfo);
        }
    }
}
