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
import org.perfcake.model.Scenario.Validation.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidatorModel extends AbstractModel implements PropertyContainer {

	public static final String PROPERTY_CLASS = "validator-class";
	public static final String PROPERTY_ID = "validator-id";
	public static final String PROPERTY_VALUE = "validator-value";
	public static final String PROPERTY_PROPERTIES = "validator-properties";

	private Validator validator;

	ValidatorModel(Validator validator) {
		super();
		if (validator == null) {
			throw new IllegalArgumentException("Validator must not be null.");
		}
		this.validator = validator;

		if (validator.getProperty() != null) {
			for (final Property p : validator.getProperty()) {
				addProperty(new PropertyModel(p));
			}
		}
	}

	public ValidatorModel() {
		super();
		this.validator = new Validator();
	}

	/**
	 * This method should not be used for modifying validator (in a way getValidator().setClass())
	 * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
	 *
	 * @return PerfCake model of Validator
	 */
	Validator getValidator() {
		return validator;
	}

	public String getClazz() {
		return validator.getClazz();
	}

	public void setClazz(String clazz) {
		final String oldClazz = validator.getClazz();
		validator.setClazz(clazz);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_CLASS, oldClazz, clazz);
	}

	public String getId() {
		return validator.getId();
	}

	public void setId(String id) {
		final String oldId = validator.getId();
		validator.setId(id);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_ID, oldId, id);
	}

	@Override
	public void addProperty(PropertyModel Property) {
		addProperty(validator.getProperty().size(), Property);
	}

	@Override
	public void addProperty(int index, PropertyModel property) {
		validator.getProperty().add(index, property.getProperty());
		getMapper().bind(property.getProperty(), property);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, null, property);
	}

	@Override
	public void removeProperty(PropertyModel property) {
		if (validator.getProperty().remove(property.getProperty())) {
			getMapper().unbind(property.getProperty(), property);
			getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, property, null);
		}
	}

	@Override
	public List<PropertyModel> getProperty() {
		final List<PropertyModel> result = MapperUtils.getPc4ideList(validator.getProperty(), getMapper());
		return Collections.unmodifiableList(result);
	}

	@Override
	public List<AbstractModel> getModelChildren() {
		final List<AbstractModel> children = new ArrayList<>();
		children.addAll(getProperty());
		return children;
	}

}
