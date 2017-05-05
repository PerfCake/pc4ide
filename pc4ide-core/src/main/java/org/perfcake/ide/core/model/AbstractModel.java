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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.perfcake.ide.core.components.ComponentLoader;
import org.perfcake.ide.core.components.ComponentLoaderImpl;
import org.perfcake.ide.core.components.PerfCakeComponent;
import org.perfcake.ide.core.docs.DocsService;
import org.perfcake.ide.core.exception.ImplementationNotFoundException;
import org.perfcake.ide.core.exception.PropertyLimitException;
import org.perfcake.ide.core.exception.UnsupportedPropertyException;
import org.perfcake.ide.core.inspector.ImplementationField;
import org.perfcake.ide.core.inspector.PropertyInspector;
import org.perfcake.ide.core.inspector.PropertyUtilsInspector;
import org.perfcake.ide.core.model.listeners.ModelListener;
import org.perfcake.ide.core.model.listeners.PropertyListener;
import org.perfcake.ide.core.model.properties.SimpleValue;
import org.perfcake.ide.core.model.properties.Value;
import org.perfcake.ide.core.model.validation.ModelValidator;
import org.perfcake.ide.core.model.validation.Validator;
import org.perfcake.ide.core.model.validation.error.ValidationError;
import org.perfcake.ide.core.model.visitor.ModelVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Represents model of a PerfCake inspector.
 *
 * @author Jakub Knetl
 */
public abstract class AbstractModel extends AbstractProperty implements Model, PropertyListener {

    static final Logger logger = LoggerFactory.getLogger(AbstractModel.class);

    Validator<Model> validator;

    public static final String IMPLEMENTATION_CLASS_PROPERTY = "class";
    public static final String SUPPORTED_PROPERTIES_PROPERTY = "supported-properties";

    /**
     * Map of the inspector properties.
     */
    private Map<PropertyInfo, PropertyContainer> properties;

    /**
     * PerfCakeComponet kind, which is represented by this model.
     */
    private PerfCakeComponent component;

    /**
     * Documentation service for obtaining properties documentation.
     */
    private DocsService docsService;

    /**
     * Support for sending property change events.
     */
    private PropertyChangeSupport pcs;

    /**
     * List of properties, which are specific for current inspector implementation. This list should never be accessible to
     * the client code. All implementation properties should be contained also in properties Map. This list only points to the keys
     * in the map to be able to recognize which properties were added on implementation class change.
     */
    private List<PropertyInfo> implementationProperties;

    /**
     * Creates new model of PerfCake inspector.
     *
     * @param component   kind of the PerfCake component which is represented by this model.
     * @param docsService Documentation service
     */
    public AbstractModel(PerfCakeComponent component, DocsService docsService) {
        super(PropertyType.MODEL);
        if (component == null) {
            throw new IllegalArgumentException("API must not be null");
        }
        if (docsService == null) {
            throw new IllegalArgumentException("Documentation service must not be null.");
        }

        this.implementationProperties = new ArrayList<>();
        this.component = component;
        this.properties = new HashMap<>();
        this.pcs = new PropertyChangeSupport(this);
        this.docsService = docsService;
        this.validator = new ModelValidator();
        initializeSupportedProperties();
    }

    @Override
    public Set<PropertyInfo> getSupportedProperties() {
        return properties.keySet();
    }

    @Override
    public Set<PropertyInfo> getSupportedImplProperties() {
        return Collections.unmodifiableSet(new HashSet<PropertyInfo>(implementationProperties));
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
    public void addProperty(String supportedPropertyName, Property property) throws PropertyLimitException, UnsupportedPropertyException {
        if (supportedPropertyName == null) {
            throw new IllegalArgumentException("Supported property name cannot be null.");
        }

        PropertyInfo propertyInfo = getSupportedProperty(supportedPropertyName);

        if (propertyInfo == null) {
            throw new UnsupportedPropertyException(String.format("No supported property has name %s.%n", supportedPropertyName));
        }

        addProperty(propertyInfo, property);
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

        property.addPropertyListener(this); // add listener so that this model listen for child property changes
        container.addProperty(property); // add property
        propertyChange(new PropertyChangeEvent(this, propertyInfo.getName(), null, property));
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
            property.removePropertyListener(this); // remove listener for property changes
            propertyChange(new PropertyChangeEvent(this, propertyInfo.getName(), property, null));
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
    public List<Property> getProperties(PropertyInfo propertyInfo) {
        if (propertyInfo == null || !getSupportedProperties().contains(propertyInfo)) {
            return null;
        }

        return getPropertyContainer(propertyInfo).getProperties();
    }

    @Override
    public List<Property> getProperties(String supportedPropertyName) {
        PropertyInfo propertyInfo = getSupportedProperty(supportedPropertyName);
        if (propertyInfo == null || !getSupportedProperties().contains(propertyInfo)) {
            return null;
        }

        return getPropertyContainer(propertyInfo).getProperties();
    }

    @Override
    public <T extends Property> T getSingleProperty(String supportedPropertyName, Class<? extends T> type)
            throws UnsupportedPropertyException {

        PropertyInfo info = getSupportedProperty(supportedPropertyName);

        if (info == null) {
            return null;
        }

        List<Property> properties = getProperties(info);

        if (properties.isEmpty()) {
            return null;
        }

        Property firstProperty = properties.get(0);
        if (firstProperty != null && !type.isAssignableFrom(firstProperty.getClass())) {
            throw new UnsupportedPropertyException(String.format("Cannot cast '%s' to '%s'",
                    firstProperty.getClass().getCanonicalName(), type.getCanonicalName()));
        }

        return firstProperty.cast(type);
    }

    @Override
    public boolean isEmpty(PropertyInfo propertyInfo) throws UnsupportedPropertyException {
        PropertyContainer container = getPropertyContainer(propertyInfo);
        if (propertyInfo == null || container == null) {
            throw new UnsupportedPropertyException(String.format("Property info %s is not supported by this model (%s).",
                    propertyInfo, this.getClass().getCanonicalName()));
        }

        return container.isEmpty();
    }

    @Override
    public int size(PropertyInfo propertyInfo) throws UnsupportedPropertyException {
        PropertyContainer container = getPropertyContainer(propertyInfo);
        if (propertyInfo == null || container == null) {
            throw new UnsupportedPropertyException(String.format("Property info %s is not supported by this model (%s).",
                    propertyInfo, this.getClass().getCanonicalName()));
        }

        return container.size();
    }

    @Override
    public void accept(ModelVisitor visitor) {
        visitor.visit(this);

        for (PropertyInfo info : getSupportedProperties()) {
            if (info.getType() == PropertyType.MODEL) {
                for (Property p : getProperties(info)) {
                    ((Model) p).accept(visitor);
                }
            }
        }
    }

    @Override
    public DocsService getDocsService() {
        return this.docsService;
    }

    @Override
    public boolean isValid() {
        return validator.validate(this, this) == null;
    }

    @Override
    public ValidationError getValidationError() {
        return validator.validate(this, this);
    }

    @Override
    public void addModelListener(ModelListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void removeModelListener(ModelListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public PerfCakeComponent getComponent() {
        return component;
    }

    @Override
    public void updateImplementation(String clazz) throws ImplementationNotFoundException {

        // remove old properties
        for (PropertyInfo propertyInfo : implementationProperties) {
            for (Property p : properties.get(propertyInfo)) {
                p.removePropertyListener(this);
            }
            properties.remove(propertyInfo);
            pcs.firePropertyChange(SUPPORTED_PROPERTIES_PROPERTY, propertyInfo, null);
        }
        implementationProperties.clear();


        ComponentLoader loader = new ComponentLoaderImpl();
        Class<?> newImplementation = loader.loadComponent(clazz, component);
        if (newImplementation == null) {
            throw new ImplementationNotFoundException("Cannot find implementation of " + clazz);
        }
        PropertyInspector inspector = new PropertyUtilsInspector();
        List<ImplementationField> fields = inspector.getProperties(newImplementation);

        for (ImplementationField f : fields) {
            int minOccurs = (f.isMandatory()) ? 1 : 0;
            SimpleValue value = (f.getValue() == null) ? null : new SimpleValue(f.getValue());

            //TODO: determine data type
            PropertyInfo implementationPropertyInfo =
                    PropertyInfo.createValueInfo(f.getName(), this, minOccurs, 1);

            PropertyContainer propertyContainer = new PropertyContainerImpl(this, implementationPropertyInfo);
            properties.put(implementationPropertyInfo, propertyContainer);
            pcs.firePropertyChange(SUPPORTED_PROPERTIES_PROPERTY, null, implementationPropertyInfo);
            implementationProperties.add(implementationPropertyInfo);
        }
    }

    /**
     * This method is called from constructor. It initializes supported properties defined in inspector type within the model. This
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

        PropertyContainer container = new PropertyContainerImpl(this, propertyInfo);
        properties.put(propertyInfo, container);
        pcs.firePropertyChange(SUPPORTED_PROPERTIES_PROPERTY, null, propertyInfo);

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
                String clazz;
                if (evt.getNewValue() instanceof Value) {
                    clazz = ((Value) evt.getNewValue()).getValue();
                } else {
                    clazz = String.valueOf(evt.getNewValue());
                }

                updateImplementation(String.valueOf(clazz));
            } catch (ClassNotFoundException e) {
                logger.warn(String.format("Cannot update implementation properties of a inspector %s", evt.getNewValue()), e);
            }
        }

        // forward events to model listeners
        pcs.firePropertyChange(evt);
    }
}
