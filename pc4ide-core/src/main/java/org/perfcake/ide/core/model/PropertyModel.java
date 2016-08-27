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

public class PropertyModel extends AbstractModel {

	public static final String PROPERTY_NAME = "property-name";
	public static final String PROPERTY_VALUE = "property-value";

	private Property property;

	PropertyModel(Property property) {
		super();
		if (property == null){
			throw new IllegalArgumentException("Property must not be null");
		}
		this.property = property;
	}

	public PropertyModel() {
		super();
		this.property = new Property();
	}

	/**
	 * This method should not be used for modifying property (in a way getProperty().setName()))
	 * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
	 *
	 * @return PerfCake model of Property
	 */
	Property getProperty() {
		return property;
	}

	public void setName(String name){
		final String oldName = getProperty().getName();
		property.setName(name);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_NAME, oldName, name);
	}


	public void setValue(String value){
		final String oldValue = getProperty().getValue();
		property.setValue(value);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_VALUE, oldValue, value);
	}
}
