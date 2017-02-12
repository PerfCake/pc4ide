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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.perfcake.ide.core.exception.PropertyLimitException;

/**
 * Property container manages one type of property for given model. Property container may be container
 * for single-value only, as well as the collection of the properties. The number of properties in container
 * must be within a bounds of {@link PropertyInfo#getMinOccurs()} and {@link PropertyInfo#getMaxOccurs()}.
 *
 * @author Jakub Knetl
 */
public class PropertyContainer<T extends PropertyRepresentation> implements Iterable<Property<? extends T>> {

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
    private List<Property<T>> properties;

    @Override
    public Iterator<Property<? extends T>> iterator() {
        return new PropertyIterator();
    }

    public class PropertyIterator implements Iterator<Property<? extends T>> {

        int index = 0;

        @Override
        public boolean hasNext() {
            return properties.size() > index;
        }

        @Override
        public Property<? extends T> next() {
            return properties.get(index++);
        }
    }

    /**
     * Creates container for given property type.
     *
     * @param model        model which owns this property container.
     * @param propertyInfo type of the properties in this container.
     */
    public PropertyContainer(Model model, PropertyInfo propertyInfo) {
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
     * Add new property.
     *
     * @param property property to be added
     * @throws PropertyLimitException if you try to add a property which has maximum number of occurrences used already.
     */
    public void addProperty(Property<T> property) throws PropertyLimitException {
        if (propertyInfo == null) {
            throw new IllegalArgumentException("propertyInfo must not be null");
        }
        if (property == null) {
            throw new IllegalArgumentException("property must not be null");
        }

        if (properties.size() == propertyInfo.getMaxOccurs()) {
            throw new PropertyLimitException("Property limit exceeded.");
        }

        properties.add(property);
        property.setModel(model);
        model.getPropertyChangeSupport()
                .firePropertyChange(new PropertyChangeEvent(this, propertyInfo.getName(), null, property));

    }

    /**
     * Removes a property.
     *
     * @param property property To be added
     * @return true if the property was removed.
     * @throws PropertyLimitException if you try to remove property, which cannot be removed due to required minimum occurrences.
     */
    public boolean removeProperty(Property<T> property)
            throws PropertyLimitException {
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

            model.getPropertyChangeSupport()
                    .firePropertyChange(new PropertyChangeEvent(this, propertyInfo.getName(), property, null));

        }
        return removed;
    }

}
