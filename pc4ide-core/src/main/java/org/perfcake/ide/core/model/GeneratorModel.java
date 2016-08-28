/*
 * Perfclispe
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
import org.perfcake.model.Scenario.Generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneratorModel extends AbstractModel implements PropertyContainer {

	public final static String PROPERTY_CLASS = "generator-class";
	public final static String PROPERTY_THREADS = "generator-threads";
	public final static String PROPERTY_RUN = "generator-run";
	public final static String PROPERTY_PROPERTY = "generator-property";

	private Generator generator;

	GeneratorModel(Generator generator) {
		super();
		if (generator == null) {
			throw new IllegalArgumentException("Generator must not be null");
		}
		this.generator = generator;

		if (generator.getProperty() != null) {
			for (final Property p : generator.getProperty()) {
				addProperty(new PropertyModel(p));
			}
		}
	}

	public GeneratorModel() {
		super();
		this.generator = new Generator();
	}

	/**
	 * This method should not be used for modifying generator (in a way getGenerator().setThreads(n))
	 * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
	 *
	 * @return PerfCake model of Generator
	 */
	Generator getGenerator() {
		return generator;
	}

	public String getThreads() {
		return generator.getThreads();
	}

	public void setThreads(String value) {
		final String oldValue = generator.getThreads();
		generator.setThreads(value);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_THREADS, oldValue, value);
	}

	public String getClazz() {
		return generator.getClazz();
	}

	public void setClazz(String value) {
		final String oldValue = generator.getClazz();
		generator.setClazz(value);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_CLASS, oldValue, value);
	}

	@Override
	public void addProperty(PropertyModel newProperty) {
		addProperty(generator.getProperty().size(), newProperty);
	}

	@Override
	public void addProperty(int index, PropertyModel newProperty) {
		generator.getProperty().add(index, newProperty.getProperty());
		getMapper().bind(newProperty.getProperty(), newProperty);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTY, null, newProperty);
	}

	@Override
	public void removeProperty(PropertyModel property) {
		if (generator.getProperty().remove(property)) {
			getMapper().unbind(property.getProperty(), property);
			getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTY, property, null);
		}

	}

	@Override
	public List<PropertyModel> getProperty() {
		final List<PropertyModel> result = MapperUtils.getPc4ideList(generator.getProperty(), getMapper());
		return Collections.unmodifiableList(result);
	}

	@Override
	public List<AbstractModel> getModelChildren() {
		final List<AbstractModel> children = new ArrayList<>();
		children.addAll(getProperty());
		return children;
	}

}
