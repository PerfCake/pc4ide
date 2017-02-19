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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.perfcake.ide.core.docs.DocsService;
import org.perfcake.ide.core.exception.PropertyLimitException;
import org.perfcake.ide.core.exception.UnsupportedPropertyException;
import org.perfcake.ide.core.newmodel.component.ImplementationField;
import org.perfcake.ide.core.newmodel.component.PropertyInspector;
import org.perfcake.ide.core.newmodel.component.PropertyUtilsInspector;
import org.perfcake.ide.core.newmodel.simple.SimpleValue;
import org.perfcake.ide.core.newmodel.simple.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Represents model of a PerfCake component.
 *
 * @author Jakub Knetl
 */
public abstract class AbstractModel extends AbstractProperty implements Model, PropertyChangeListener {

    static final Logger logger = LoggerFactory.getLogger(AbstractModel.class);

    public static final String IMPLEMENTATION_CLASS_PROPERTY = "class";

    /**
     * Map of the component properties.
     */
    private Map<PropertyInfo, PropertyContainer> properties;

    /**
     * Component API (interface or abstract class).
     */
    private Class<?> api;

    /**
     * Documentation service for obtaining properties documentation.
     */
    private DocsService docsService;

    /**
     * Support for sending property change events.
     */
    private PropertyChangeSupport pcs;

    /**
     * List of properties, which are specific for current component implementation. This list should never be accessible to
     * the client code. All implementation properties should be contained also in properties Map. This list only points to the keys
     * in the map to be able to recognize which properties were added on implementation class change.
     */
    private List<PropertyInfo> implementationProperties;

    /**
     * Creates new model of PerfCake component.
     *
     * @param api         Interface or abstract class of PerfCake component
     * @param docsService Documentation service
     */
    public AbstractModel(Class<?> api, DocsService docsService) {
        super(PropertyType.MODEL);
        if (api == null) {
            throw new IllegalArgumentException("API must not be null");
        }
        if (docsService == null) {
            throw new IllegalArgumentException("Documentation service must not be null.");
        }

        this.implementationProperties = new ArrayList<>();
        this.api = api;
        this.properties = new HashMap<>();
        this.pcs = new PropertyChangeSupport(this);
        this.docsService = docsService;
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
    public void addProperty(PropertyInfo propertyInfo, Property property) throws PropertyLimitException, UnsupportedPropertyException {
        if (propertyInfo == null) {
            throw new IllegalArgumentException("Property info must not be null.");
        }
        if (property == null) {
            throw new IllegalArgumentException("Property must not be null.");
        }

        PropertyContainer container = getPropertyContainer(propertyInfo);
        if (container == null) {
            throw new UnsupportedPropertyException(String.format("The %s is not supported by the model.", propertyInfo));
        }

        property.addPropertyListener(this);
        container.addProperty(property);
        pcs.firePropertyChange("property", null, property);
    }

    @Override
    public boolean removeProperty(PropertyInfo propertyInfo, Property property)
            throws PropertyLimitException, UnsupportedPropertyException {
        if (propertyInfo == null) {
            throw new IllegalArgumentException("Property info must not be null.");
        }

        if (property == null) {
            throw new IllegalArgumentException("Property must not be null.");
        }

        PropertyContainer container = getPropertyContainer(propertyInfo);
        if (container == null) {
            throw new UnsupportedPropertyException(String.format("The %s is not supported by the model.", propertyInfo));
        }

        boolean removed = container.removeProperty(property);
        if (removed) {
            property.removePropertyListener(this);
            pcs.firePropertyChange("property", property, null);
        }
        return removed;
    }

    @Override
    public Iterator<Property> propertyIterator(PropertyInfo propertyInfo) {
        if (propertyInfo == null) {
            throw new IllegalArgumentException("Property info must not be null.");
        }

        PropertyContainer container = getPropertyContainer(propertyInfo);
        if (container == null) {
            throw new UnsupportedPropertyException(String.format("The %s is not supported by the model.", propertyInfo));
        }
        return container.iterator();
    }

    @Override
    public DocsService getDocsService() {
        return this.docsService;
    }

    @Override
    public void addModelListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void removeModelListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    @Override
    public Class<?> getApi() {
        return api;
    }

    @Override
    public void updateImplementation(String clazz) throws ClassNotFoundException {

        // remove old properties
        for (PropertyInfo property : implementationProperties) {
            properties.remove(property);
        }
        implementationProperties.clear();


        Class<?> newImplementation = Class.forName(clazz);
        PropertyInspector inspector = new PropertyUtilsInspector();
        List<ImplementationField> fields = inspector.getProperties(newImplementation, api);

        for (ImplementationField f : fields) {
            int minOccurs = (f.isMandatory()) ? 1 : 0;
            SimpleValue value = (f.getValue() == null) ? null : new SimpleValue(f.getValue());
            PropertyInfo implementationPropertyInfo =
                    new PropertyInfo(f.getName(), this, Value.class, value, minOccurs, 1);

            properties.put(implementationPropertyInfo, new PropertyContainerImpl(this, implementationPropertyInfo));
            implementationProperties.add(implementationPropertyInfo);
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

        properties.put(propertyInfo, new PropertyContainerImpl(this, propertyInfo));
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

    /**
     * Finds a property based on its name.
     *
     * @param propertyName Name of the property
     * @return PropertyContainer which holds information about properties with given name, or null, if no such property can be found.
     */
    protected PropertyContainer getPropertyContainer(String propertyName) {

        if (propertyName == null) {
            throw new IllegalArgumentException("Property name cannot be null.");
        }

        PropertyContainer result = null;

        for (PropertyInfo type : properties.keySet()) {
            if (propertyName.equals(type.getName())) {
                result = properties.get(type);
                break;
            }
        }

        return result;
    }

    /**
     * @param propertyInfo property to get
     * @return Container of properties for given type or null, if no such propertyInfo can be found in this model.
     */
    protected PropertyContainer getPropertyContainer(PropertyInfo propertyInfo) {
        if (propertyInfo == null) {
            throw new IllegalArgumentException("propertyInfo must not be null");
        }

        return properties.get(propertyInfo);
    }

    /*
     * implementation of PropertyChangeListener in order to be able to get notification from properties
     * about their changes.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // update implementation class if the implementation of this model has been changed
        if (IMPLEMENTATION_CLASS_PROPERTY.equals(evt.getPropertyName())) {
            try {
                updateImplementation(String.valueOf(evt.getNewValue()));
            } catch (ClassNotFoundException e) {
                logger.warn(String.format("Cannot update implementation properties of a component %s", evt.getNewValue()), e);
            }
        }

        // forward events to model listeners
        pcs.firePropertyChange(evt);


    }
}
