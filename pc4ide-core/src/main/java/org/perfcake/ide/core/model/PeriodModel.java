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

import org.perfcake.model.Scenario.Reporting.Reporter.Destination.Period;

public class PeriodModel extends AbstractModel {

	public static final String PROPERTY_TYPE = "period-type";
	public static final String PROPERTY_VALUE = "period-value";

	private Period period;

	PeriodModel(Period period) {
		super();
		if (period == null) {
			throw new IllegalArgumentException("Period must not be null.");
		}
		this.period = period;
	}

	public PeriodModel() {
		super();
		this.period = new Period();
	}

	/**
	 * This method should not be used for modifying Period(in a way getPeriod().setValue()))
	 * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
	 *
	 * @return PerfCake model of Period
	 */
	Period getPeriod() {
		return period;
	}

	public void setType(String type) {
		final String oldType = period.getType();
		period.setType(type);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_TYPE, oldType, type);
	}

	public void setValue(String value) {
		final String oldValue = period.getValue();
		period.setValue(value);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_VALUE, oldValue, value);
	}

}
