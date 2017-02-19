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
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.perfcake.ide.core.exception.ModelException;
import org.perfcake.ide.core.exception.PropertyLimitException;
import org.perfcake.ide.core.exception.UnsupportedPropertyException;

/**
 * This property container implementation manages one type of property for given model. Property container may be container
 * for single-value only, as well as the collection of the properties. The number of properties in container
 * must be within a bounds of {@link PropertyInfo#getMinOccurs()} and {@link PropertyInfo#getMaxOccurs()}.
 *
 * @author Jakub Knetl
 */
public class PropertyContainerImpl implements PropertyContainer {

    /**
     * Model which owns this container.
     */
    private Model model;

    /**
     * Type of contained properties.
     */
    private PropertyInfo propertyInfo;

    /**
     * Properties in the container.
     */
    private List<Property> properties;

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    @Override
    public Iterator<Property> iterator() {
        return new PropertyIterator();
    }

    public class PropertyIterator implements Iterator<Property> {

        int index = 0;

        @Override
        public boolean hasNext() {
            return properties.size() > index;
        }

        @Override
        public Property next() {
            return properties.get(index++);
        }
    }

    /**
     * Creates container for given property type.
     *
     * @param model        model which owns this property container.
     * @param propertyInfo type of the properties in this container.
     */
    public PropertyContainerImpl(Model model, PropertyInfo propertyInfo) {
        if (model == null) {
            throw new IllegalArgumentException("Model must not be null.");
        }
        if (propertyInfo == null) {
            throw new IllegalArgumentException("PropertyInfo must not be null.");
        }
        this.propertyInfo = propertyInfo;
        this.model = model;
        properties = new ArrayList<>();
    }


    /**
     * Adds property to the container.
     *
     * @param property property to be added
     * @throws PropertyLimitException       If maximum number of properties are in this container.
     * @throws UnsupportedPropertyException If the added property has wrong type, which is not supported by this container.
     */
    @Override
    public void addProperty(Property property) throws PropertyLimitException, UnsupportedPropertyException {
        if (property == null) {
            throw new IllegalArgumentException("property must not be null");
        }

        PropertyType propertyType = PropertyType.detectPropertyType(property.cast(Property.class).getClass());
        if (!propertyInfo.getType().equals(propertyType)) {
            throw new ModelException(String.format("Invalid property type. This container supports %s, but added property was %s ",
                    propertyInfo.getType().getClazz().getCanonicalName(), property.getPropertyInfo().getType().getClazz()));
        }

        if (properties.size() == propertyInfo.getMaxOccurs()) {
            throw new PropertyLimitException("Property limit exceeded.");
        }

        property.setModel(model);
        property.setPropertyInfo(propertyInfo);
        properties.add(property);
        pcs.firePropertyChange(new PropertyChangeEvent(this, propertyInfo.getName(), null, property));

    }

    @Override
    public boolean removeProperty(Property property) throws PropertyLimitException {
        if (propertyInfo == null) {
            throw new IllegalArgumentException("propertyInfo must not be null");
        }
        if (property == null) {
            throw new IllegalArgumentException("property must not be null");
        }


        if (properties.size() == propertyInfo.getMinOccurs()) {
            throw new PropertyLimitException("Property limit decreased under minimum value.");
        }

        boolean removed = properties.remove(property);

        if (removed) {
            property.setModel(null);
            property.setPropertyInfo(null);

            pcs.firePropertyChange(new PropertyChangeEvent(this, propertyInfo.getName(), property, null));

        }
        return removed;
    }

    @Override
    public void addListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void removeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

}
