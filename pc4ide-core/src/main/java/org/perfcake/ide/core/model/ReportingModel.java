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

import org.perfcake.model.PropertyType;
import org.perfcake.model.Scenario.Reporting;
import org.perfcake.model.Scenario.Reporting.Reporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportingModel extends AbstractModel implements PropertyContainer {

	public static final String PROPERTY_PROPERTIES = "reporting-property";
	public static final String PROPERTY_REPORTERS = "reporting-reporter";

	private Reporting reporting;

	ReportingModel(Reporting reporting) {
		super();
		if (reporting == null) {
			throw new IllegalArgumentException("Reporting must not be null.");
		}
		this.reporting = reporting;

		if (reporting.getReporter() != null) {
			for (final Reporter r : reporting.getReporter()) {
				getMapper().bind(r, new ReporterModel(r));
			}
		}

		if (reporting.getProperty() != null) {
			for (final PropertyType p : reporting.getProperty()) {
				getMapper().bind(p, new PropertyModel(p));
			}
		}
	}

	public ReportingModel() {
		super();
		this.reporting = new Reporting();
	}

	/**
	 * This method should not be used for modifying Reporting(in a way getReporting().addReporter()))
	 * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
	 *
	 * @return PerfCake model of Reporting
	 */
	Reporting getReporting() {
		return reporting;
	}

	public void addReporter(ReporterModel reporter) {
		addReporter(reporting.getReporter().size(), reporter);
	}

	public void addReporter(int index, ReporterModel reporter) {
		reporting.getReporter().add(index, reporter.getReporter());
		getMapper().bind(reporter.getReporter(), reporter);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_REPORTERS, null, reporter);
	}

	public void removeReporter(ReporterModel reporter) {
		if (reporting.getReporter().remove(reporter.getReporter())) {
			getMapper().unbind(reporter.getReporter(), reporter);
			getPropertyChangeSupport().firePropertyChange(PROPERTY_REPORTERS, reporter, null);
		}
	}

	public List<ReporterModel> getReporter() {
		final List<ReporterModel> result = MapperUtils.getPc4ideList(reporting.getReporter(), getMapper());
		return Collections.unmodifiableList(result);
	}

	@Override
	public void addProperty(PropertyModel property) {
		addProperty(reporting.getProperty().size(), property);
	}

	@Override
	public void addProperty(int index, PropertyModel property) {
		reporting.getProperty().add(index, property.getProperty());
		getMapper().bind(property.getProperty(), property);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, null, property);
	}

	@Override
	public void removeProperty(PropertyModel property) {
		if (reporting.getProperty().remove(property.getProperty())) {
			getMapper().unbind(property.getProperty(), property);
			getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, property, null);
		}
	}

	@Override
	public List<PropertyModel> getProperty() {
		final List<PropertyModel> result = MapperUtils.getPc4ideList(reporting.getProperty(), getMapper());
		return Collections.unmodifiableList(result);
	}

	@Override
	public List<AbstractModel> getModelChildren() {
		final List<AbstractModel> children = new ArrayList<>();
		children.addAll(getReporter());
		children.addAll(getProperty());
		return children;
	}
}
