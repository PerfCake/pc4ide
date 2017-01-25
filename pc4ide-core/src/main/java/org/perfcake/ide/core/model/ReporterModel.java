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
/*
 * PerfClispe
 *
 *
 * Copyright (c) 2014 Jakub Knetl
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
 */

package org.perfcake.ide.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.perfcake.model.PropertyType;
import org.perfcake.model.Scenario.Reporting.Reporter;
import org.perfcake.model.Scenario.Reporting.Reporter.Destination;

public class ReporterModel extends AbstractModel implements PropertyContainer, Switchable {

    public static final String PROPERTY_CLASS = "reporter-class";
    public static final String PROPERTY_DESTINATIONS = "reporter-destination";
    public static final String PROPERTY_PROPERTIES = "reporter-property";
    public static final String PROPERTY_ENABLED = "reporter-enabled";

    private Reporter reporter;

    ReporterModel(Reporter reporter) {
        super();
        if (reporter == null) {
            throw new IllegalArgumentException("Reporter must not be null");
        }
        this.reporter = reporter;

        if (reporter.getDestination() != null) {
            for (final Destination d : reporter.getDestination()) {
                getMapper().bind(d, new DestinationModel(d));
            }
        }

        if (reporter.getProperty() != null) {
            for (final PropertyType p : reporter.getProperty()) {
                getMapper().bind(p, new PropertyModel(p));
            }
        }
    }

    public ReporterModel() {
        super();
    }

    /**
     * This method should not be used for modifying Reporter(in a way getReporter().setClass()))
     * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
     *
     * @return PerfCake model of Reporter
     */
    Reporter getReporter() {
        return reporter;
    }

    public String getClazz() {
        return reporter.getClazz();
    }

    /**
     * Sets implementation class of of the reporter.
     * @param clazz fully qualified name of the class
     */
    public void setClazz(String clazz) {
        final String oldClazz = reporter.getClazz();
        reporter.setClazz(clazz);
        getPropertyChangeSupport().firePropertyChange(PROPERTY_CLASS, oldClazz, clazz);
    }

    /**
     * Adds a destination to reporter.
     *
     * @param destination destination to be added
     */
    public void addDestination(DestinationModel destination) {
        addDestination(reporter.getDestination().size(), destination);
    }

    /**
     * Adds a destination to reporter.
     *
     * @param index index of new destination
     * @param destination destination to be added
     */
    public void addDestination(int index, DestinationModel destination) {
        reporter.getDestination().add(index, destination.getDestinatnion());
        getMapper().bind(destination.getDestinatnion(), destination);
        getPropertyChangeSupport().firePropertyChange(PROPERTY_DESTINATIONS, null, destination);
    }

    /**
     * Removes destination from the reporter.
     * @param destination destination to be removed
     */
    public void removeDestionation(DestinationModel destination) {
        if (reporter.getDestination().remove(destination.getProperty())) {
            getMapper().unbind(destination.getDestinatnion(), destination);
            getPropertyChangeSupport().firePropertyChange(PROPERTY_DESTINATIONS, destination, null);
        }
    }

    /**
     *
     * @return Unmodifiable list of destinations.
     */
    public List<DestinationModel> getDestination() {
        final List<DestinationModel> result = MapperUtils.getPc4ideList(reporter.getDestination(), getMapper());
        return Collections.unmodifiableList(result);
    }

    @Override
    public void addProperty(PropertyModel Property) {
        addProperty(reporter.getProperty().size(), Property);
    }

    @Override
    public void addProperty(int index, PropertyModel property) {
        reporter.getProperty().add(index, property.getProperty());
        getMapper().bind(property.getProperty(), property);
        getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, null, property);
    }

    @Override
    public void removeProperty(PropertyModel property) {
        if (reporter.getProperty().remove(property.getProperty())) {
            getMapper().unbind(property.getProperty(), property);
            getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, property, null);
        }
    }

    @Override
    public List<PropertyModel> getProperty() {
        final List<PropertyModel> result = MapperUtils.getPc4ideList(reporter.getProperty(), getMapper());
        return Collections.unmodifiableList(result);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled != reporter.isEnabled()) {
            reporter.setEnabled(enabled);
            getPropertyChangeSupport().firePropertyChange(PROPERTY_ENABLED, !enabled, enabled);
        }
    }

    @Override
    public boolean isEnabled() {
        return reporter.isEnabled();
    }

    @Override
    public List<AbstractModel> getModelChildren() {
        final List<AbstractModel> children = new ArrayList<>();
        children.addAll(getDestination());
        children.addAll(getProperty());
        return children;
    }
}
