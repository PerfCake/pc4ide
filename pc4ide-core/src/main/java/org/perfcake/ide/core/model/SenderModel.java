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
import org.perfcake.model.Scenario.Sender;

import java.util.Collections;
import java.util.List;

public class SenderModel extends AbstractModel implements PropertyContainer {

	public static final String PROPERTY_CLASS = "sender-class";
	public static final String PROPERTY_PROPERTIES = "sender-property";

	private Sender sender;

	SenderModel(Sender sender) {
		super();
		if (sender == null) {
			throw new IllegalArgumentException("Sender must not be null");
		}
		this.sender = sender;

		if (sender.getProperty() != null) {
			for (final Property p : sender.getProperty()) {
				addProperty(new PropertyModel(p));
			}
		}
	}

	public SenderModel() {
		super();
		this.sender = new Sender();
	}

	/**
	 * This method should not be used for modifying Sender (in a way getSender().setClass()))
	 * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
	 *
	 * @return PerfCake model of Sender
	 */
	Sender getSender() {
		return sender;
	}

	public void setClazz(String clazz) {
		final String oldClazz = sender.getClazz();
		sender.setClazz(clazz);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_CLASS, oldClazz, clazz);
	}

	@Override
	public void addProperty(PropertyModel Property) {
		addProperty(sender.getProperty().size(), Property);
	}

	@Override
	public void addProperty(int index, PropertyModel property) {
		sender.getProperty().add(index, property.getProperty());
		getMapper().bind(property.getProperty(), property);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, null, property);
	}

	@Override
	public void removeProperty(PropertyModel property) {
		if (sender.getProperty().remove(property.getProperty())) {
			getMapper().unbind(property.getProperty(), property);
			getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, property, null);
		}
	}

	@Override
	public List<PropertyModel> getProperty() {
		final List<PropertyModel> result = MapperUtils.getPc4ideList(sender.getProperty(), getMapper());
		return Collections.unmodifiableList(result);
	}
}
