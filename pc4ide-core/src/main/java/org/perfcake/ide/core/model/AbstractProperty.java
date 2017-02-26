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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.perfcake.ide.core.exception.UnsupportedPropertyException;
import org.perfcake.ide.core.model.listeners.PropertyListener;

/**
 * Contains information about property of a model.
 *
 * @author Jakub Knetl
 */
public abstract class AbstractProperty implements Property {

    /**
     * Type of this property.
     */
    private final PropertyType type;

    /**
     * Metadata about this property type. This field is filled in only when property is being added into some {@link PropertyContainer}.
     */
    private PropertyInfo propertyInfo;


    /**
     * Model object which owns this property value. This field is set by adding Property into {@link PropertyContainer} which is part
     * of some model.
     */
    private Model model;

    /**
     * Manages listeners and enables to fire events.
     */
    private PropertyChangeSupport pcs;

    /**
     * Creates new property.
     *
     * @param type type of the property
     */
    public AbstractProperty(PropertyType type) {
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }
        pcs = new PropertyChangeSupport(this);
        this.type = type;
    }

    @Override
    public PropertyInfo getPropertyInfo() {
        return propertyInfo;
    }

    @Override
    public <T extends Property> T cast(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null.");
        }

        if (!clazz.isAssignableFrom(type.getClazz())) {
            throw new UnsupportedPropertyException(String.format("Type of property value (%s) does not conform property type (%s)",
                    clazz.getCanonicalName(), type.getClazz().getCanonicalName()));
        }
        return clazz.cast(this);
    }

    @Override
    public void setPropertyInfo(PropertyInfo propertyInfo) {
        if (propertyInfo != null && propertyInfo.getType() != type) {
            throw new UnsupportedPropertyException(
                    String.format("Type of the property info (%s) is not compatible with property type (%s).",
                    propertyInfo.getType(), type));
        }
        this.propertyInfo = propertyInfo;
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void addPropertyListener(PropertyListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyListener(PropertyListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Notifies listeners about property change.
     *
     * @param oldValue old value of property
     * @param newValue new value of property
     */
    protected void fireChangeEvent(Object oldValue, Object newValue) {
        this.fireChangeEvent(null, oldValue, newValue);

    }

    /**
     * Notifies listeners about property change.
     *
     * @param eventSuffix event name will be fetched from propertyInfo and it will be suffixed by this value.
     * @param oldValue    old value of property
     * @param newValue    new value of property
     */
    protected void fireChangeEvent(String eventSuffix, Object oldValue, Object newValue) {
        String eventName;

        if (propertyInfo != null) {
            if (eventSuffix == null || eventSuffix.isEmpty()) {
                eventName = propertyInfo.getName();
            } else {
                eventName = String.format("%s-%s", propertyInfo.getName());
            }
        } else {
            eventName = "property value change";
        }

        pcs.firePropertyChange(eventName, oldValue, newValue);

    }
}
