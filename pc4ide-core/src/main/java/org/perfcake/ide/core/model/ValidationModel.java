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

import org.perfcake.model.Scenario.Validation;
import org.perfcake.model.Scenario.Validation.Validator;

public class ValidationModel extends AbstractModel {

	public static final String PROPERTY_VALIDATORS = "validation-validators";
	public static final String PROPERTY_ENABLED = "validation-enabled";
	public static final String PROPERTY_FAST_FORWARD = "validation-fast-forward";

	private Validation validation;

	ValidationModel(Validation validation) {
		super();
		if (validation == null) {
			throw new IllegalArgumentException("Validation must not be null");
		}
		this.validation = validation;

		if (validation.getValidator() != null) {
			for (final Validator v : validation.getValidator()) {
				addValidator(new ValidatorModel(v));
			}
		}
	}

	public ValidationModel() {
		super();
		this.validation = new Validation();
	}

	/**
	 * This method should not be used for modifying Validation (in a way getValidation().getValidator().add()))
	 * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
	 *
	 * @return PerfCake model of Validation
	 */
	Validation getValidation() {
		return validation;
	}

	public void addValidator(ValidatorModel validator) {
		addValidator(validation.getValidator().size(), validator);
	}

	public void addValidator(int index, ValidatorModel validator) {
		validation.getValidator().add(index, validator.getValidator());
		getMapper().bind(validator.getValidator(), validator);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_VALIDATORS, null, validator);
	}

	public void removeValidator(ValidatorModel validator) {
		if (validation.getValidator().remove(validator.getValidator())) {
			getMapper().bind(validator.getValidator(), validator);
			getPropertyChangeSupport().firePropertyChange(PROPERTY_VALIDATORS, validator, null);
		}
	}

	public void setEnabled(boolean enabled) {
		final boolean oldEnabled = validation.isEnabled();
		validation.setEnabled(enabled);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_ENABLED, oldEnabled, enabled);
	}

	public void setFastForward(boolean fastForward) {
		final boolean oldFastForward = validation.isFastForward();
		validation.setFastForward(fastForward);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_FAST_FORWARD, oldFastForward, fastForward);
	}
}
