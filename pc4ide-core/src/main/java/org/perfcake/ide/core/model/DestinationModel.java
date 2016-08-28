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
import org.perfcake.model.Scenario.Reporting.Reporter.Destination;
import org.perfcake.model.Scenario.Reporting.Reporter.Destination.Period;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DestinationModel extends AbstractModel implements PropertyContainer, Switchable {

	public static final String PROPERTY_PERIOD = "destination-period";
	public static final String PROPERTY_CLASS = "destination-class";
	public static final String PROPERTY_ENABLED = "destination-enabled";
	public static final String PROPERTY_PROPERTIES = "destination-property";

	private Destination destination;

	DestinationModel(Destination destination) {
		super();
		if (destination == null) {
			throw new IllegalArgumentException("Destination must not be null");
		}
		this.destination = destination;

		if (destination.getPeriod() != null) {
			for (final Period p : destination.getPeriod()) {
				addPeriod(new PeriodModel(p));
			}
		}

		if (destination.getProperty() != null) {
			for (final Property p : destination.getProperty()) {
				addProperty(new PropertyModel(p));
			}
		}
	}

	public DestinationModel() {
		super();
		destination = new Destination();
	}

	/**
	 * This method should not be used for modifying Destination (in a way destination.setClass()))
	 * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
	 *
	 * @return PerfCake model of Destination
	 */
	Destination getDestinatnion() {
		return destination;
	}

	public void setClazz(String clazz) {
		final String oldClazz = destination.getClazz();
		destination.setClazz(clazz);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_CLASS, oldClazz, clazz);
	}

	public String getClazz() {
		return destination.getClazz();
	}

	public void addPeriod(PeriodModel period) {
		addPeriod(destination.getPeriod().size(), period);
	}

	public void addPeriod(int index, PeriodModel period) {
		destination.getPeriod().add(index, period.getPeriod());
		getMapper().bind(period.getPeriod(), period);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_PERIOD, null, period);
	}

	public void removePeriod(PeriodModel period) {
		if (destination.getPeriod().remove(period)) {
			getMapper().unbind(period.getPeriod(), period);
			getPropertyChangeSupport().firePropertyChange(PROPERTY_PERIOD, period, null);
		}
	}

	public List<PeriodModel> getPeriod() {
		final List<PeriodModel> result = MapperUtils.getPc4ideList(destination.getPeriod(), getMapper());
		return Collections.unmodifiableList(result);
	}

	@Override
	public void addProperty(PropertyModel property) {
		addProperty(destination.getProperty().size(), property);
	}

	@Override
	public void addProperty(int index, PropertyModel property) {
		destination.getProperty().add(index, property.getProperty());
		getMapper().bind(property.getProperty(), property);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, null, property);
	}

	@Override
	public void removeProperty(PropertyModel property) {
		if (destination.getProperty().remove(property)) {
			getMapper().unbind(property.getProperty(), property);
			getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, property, null);
		}
	}

	@Override
	public List<PropertyModel> getProperty() {
		final List<PropertyModel> result = MapperUtils.getPc4ideList(destination.getProperty(), getMapper());
		return Collections.unmodifiableList(result);
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (enabled != destination.isEnabled()) {
			destination.setEnabled(enabled);
			getPropertyChangeSupport().firePropertyChange(PROPERTY_ENABLED, !enabled, enabled);
		}
	}

	@Override
	public boolean isEnabled() {
		return destination.isEnabled();
	}

	@Override
	public List<AbstractModel> getModelChildren() {
		final List<AbstractModel> children = new ArrayList<>();

		children.addAll(getProperty());
		children.addAll(getPeriod());
		return children;
	}

}
