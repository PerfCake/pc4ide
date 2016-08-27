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

import org.perfcake.model.Scenario.Run;

public class RunModel extends AbstractModel {
	public static final String PROPERTY_TYPE = "run-type";
	public static final String PROPERTY_VALUE = "run-value";
	private Run run;

	RunModel(Run run) {
		super();
		if (run == null) {
			throw new IllegalArgumentException("Run must not be null");
		}
		this.run = run;
	}

	public RunModel() {
		super();
		this.run = new Run();
	}

	/**
	 * This method should not be used for modifying run (in a way getRun().setType(s))
	 * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
	 *
	 * @return PerfCake model of Run
	 */
	Run getRun() {
		return run;
	}

	public void setType(String value) {
		final String oldValue = run.getType();
		run.setType(value);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_TYPE, oldValue, value);
	}

	public void setValue(String value) {
		final String oldValue = run.getValue();
		run.setValue(value);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_VALUE, oldValue, value);
	}
}
