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
import org.perfcake.model.Scenario.Properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PropertiesModel extends AbstractModel implements PropertyContainer {

	public static final String PROPERTY_PROPERTIES = "properties-property";

	private Properties properties;

	PropertiesModel(Properties properties) {
		super();
		if (properties == null) {
			throw new IllegalArgumentException("Properties must not be null");
		}
		this.properties = properties;

		if (properties.getProperty() != null) {
			for (final PropertyType p : properties.getProperty()) {
				getMapper().bind(p, new PropertyModel(p));
			}
		}
	}

	public PropertiesModel() {
		super();
		this.properties = new Properties();
	}

	/**
	 * This method should not be used for modifying properties (in a way getProperties().add()))
	 * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
	 *
	 * @return PerfCake model of Properties
	 */
	Properties getProperties() {
		return properties;
	}

	@Override
	public void addProperty(PropertyModel Property) {
		addProperty(properties.getProperty().size(), Property);
	}

	@Override
	public void addProperty(int index, PropertyModel property) {
		properties.getProperty().add(index, property.getProperty());
		getMapper().bind(property.getProperty(), property);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, null, property);
	}

	@Override
	public void removeProperty(PropertyModel property) {
		if (properties.getProperty().remove(property.getProperty())) {
			getMapper().unbind(property.getProperty(), property);
			getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, property, null);
		}
	}

	@Override
	public List<PropertyModel> getProperty() {
		final List<PropertyModel> result = MapperUtils.getPc4ideList(properties.getProperty(), getMapper());
		return Collections.unmodifiableList(result);
	}

	@Override
	public List<AbstractModel> getModelChildren() {
		final List<AbstractModel> children = new ArrayList<>();
		children.addAll(getProperty());
		return children;
	}

}
