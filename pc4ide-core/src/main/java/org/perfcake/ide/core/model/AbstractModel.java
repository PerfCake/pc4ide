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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


/**
 * Abstract PerfClipse model representation of PerfCake model.
 * @author Jakub Knetl
 *
 */
public class AbstractModel {

	private PropertyChangeSupport propertyChangeSupport;
	private final Mapper mapper;

	public AbstractModel() {
		mapper = new SimpleMapper();
		propertyChangeSupport = new PropertyChangeSupport(this);
	}


	/**
	 * @return ModelMapper object.
	 */
	protected Mapper getMapper() {
		return mapper;
	}

	/**
	 *
	 * @return listeners
	 */
	public PropertyChangeSupport getPropertyChangeSupport() {
		return propertyChangeSupport;
	}

	/**
	 * Adds listener
	 * @param listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener){
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	/**
	 * Remove listener
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener){
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
}
