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

import org.perfcake.model.Property;
import org.perfcake.model.Scenario.Reporting.Reporter;
import org.perfcake.model.Scenario.Reporting.Reporter.Destination;

import java.util.Collections;
import java.util.List;

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
				addDestination(new DestinationModel(d));
			}
		}

		if (reporter.getProperty() != null) {
			for (final Property p : reporter.getProperty()) {
				addProperty(new PropertyModel(p));
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

	public void setClazz(String clazz) {
		final String oldClazz = reporter.getClazz();
		reporter.setClazz(clazz);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_CLASS, oldClazz, clazz);
	}

	public void addDestination(DestinationModel destination) {
		addDestination(reporter.getDestination().size(), destination);
	}

	public void addDestination(int index, DestinationModel destination) {
		reporter.getDestination().add(index, destination.getDestinatnion());
		getMapper().bind(destination.getDestinatnion(), destination);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_DESTINATIONS, null, destination);
	}

	public void removeDestionation(DestinationModel destination) {
		if (reporter.getDestination().remove(destination.getProperty())) {
			getMapper().unbind(destination.getDestinatnion(), destination);
			getPropertyChangeSupport().firePropertyChange(PROPERTY_DESTINATIONS, destination, null);
		}
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
}
