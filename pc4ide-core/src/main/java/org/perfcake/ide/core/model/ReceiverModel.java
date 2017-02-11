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
/**
 *
 */

package org.perfcake.ide.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.perfcake.model.PropertyType;
import org.perfcake.model.Scenario.Receiver;
import org.perfcake.model.Scenario.Receiver.Correlator;

/**
 * Receiver model.
 *
 * @author jknetl
 */
public class ReceiverModel extends AbstractModel implements PropertyContainer {

    public static final String PROPERTY_CORRELATOR = "receiver-correlator";
    public static final String PROPERTY_SOURCE = "receiver-source";
    public static final String PROPERTY_CLASS = "receiver-class";
    public static final String PROPERTY_PROPERTIES = "receiver-properties";
    public static final String PROPERTY_THREADS = "receiver-threads";

    private Receiver receiver;

    ReceiverModel(Receiver receiver) {
        super();
        if (receiver == null) {
            throw new IllegalArgumentException("Receiver must not be null");
        }
        this.receiver = receiver;

        if (receiver.getCorrelator() != null) {
            getMapper().bind(receiver.getCorrelator(), new CorrelatorModel(receiver.getCorrelator()));
        }

        for (final PropertyType p : receiver.getProperty()) {
            getMapper().bind(p, new PropertyModel(p));
        }
    }

    /**
     * Creates new receiver model.
     */
    public ReceiverModel() {
        super();
        this.receiver = new Receiver();
    }

    /**
     * This method should not be used for modifying receiver (in a way getReceiver().setClazz(clazz)))
     * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
     *
     * @return PerfCake model of receiver
     */
    Receiver getReceiver() {
        return receiver;
    }

    public CorrelatorModel getCorrelator() {
        return (CorrelatorModel) getMapper().getModel(receiver.getCorrelator());
    }

    /**
     * Sets correlator for a receiver.
     *
     * @param value correalator to be set
     */
    public void setCorrelator(CorrelatorModel value) {
        final Correlator oldCorrelator = receiver.getCorrelator();
        AbstractModel oldCorrelatorModel = null;
        if (oldCorrelator != null) {
            oldCorrelatorModel = getMapper().getModel(oldCorrelator);
            getMapper().unbind(oldCorrelator, oldCorrelatorModel);
        }
        getMapper().bind(value.getCorrelator(), value);
        receiver.setCorrelator(value.getCorrelator());
        getPropertyChangeSupport().firePropertyChange(PROPERTY_CORRELATOR, oldCorrelatorModel, value);
    }

    public String getSource() {
        return receiver.getSource();
    }

    /**
     * Sets receiver source.
     * @param value source of the receiver
     */
    public void setSource(String value) {
        final String oldSource = receiver.getSource();
        receiver.setSource(value);
        getPropertyChangeSupport().firePropertyChange(PROPERTY_SOURCE, oldSource, value);
    }

    public String getClazz() {
        return receiver.getClazz();
    }

    /**
     * Sets receiver implementation class.
     *
     * @param value fully qualified class name of the receiver
     */
    public void setClazz(String value) {
        final String oldClazz = receiver.getClazz();
        receiver.setClazz(value);
        getPropertyChangeSupport().firePropertyChange(PROPERTY_CLASS, oldClazz, value);
    }

    public String getThreads() {
        return receiver.getThreads();
    }

    /**
     * Sets number of threads for the receiver.
     *
     * @param value number of threads
     */
    public void setThreads(String value) {
        final String oldThreads = receiver.getThreads();
        receiver.setThreads(value);
        getPropertyChangeSupport().firePropertyChange(PROPERTY_THREADS, oldThreads, value);
    }

    @Override
    public void addProperty(PropertyModel Property) {
        addProperty(receiver.getProperty().size(), Property);
    }

    @Override
    public void addProperty(int index, PropertyModel property) {
        receiver.getProperty().add(index, property.getProperty());
        getMapper().bind(property.getProperty(), property);
        getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, null, property);
    }

    @Override
    public void removeProperty(PropertyModel property) {
        if (receiver.getProperty().remove(property.getProperty())) {
            getMapper().unbind(property.getProperty(), property);
            getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, property, null);
        }
    }

    @Override
    public List<PropertyModel> getProperty() {
        final List<PropertyModel> result = MapperUtils.getPc4ideList(receiver.getProperty(), getMapper());
        return Collections.unmodifiableList(result);
    }

    /* (non-Javadoc)
     * @see org.perfcake.ide.core.model.AbstractModel#getModelChildren()
     */
    @Override
    public List<AbstractModel> getModelChildren() {
        final List<AbstractModel> children = new ArrayList<>();
        if (getCorrelator() != null) {
            children.add(getCorrelator());
        }
        children.addAll(getProperty());

        return children;
    }
}
