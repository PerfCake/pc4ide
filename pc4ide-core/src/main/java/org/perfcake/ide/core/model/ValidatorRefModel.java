package org.perfcake.ide.core.model;

import org.perfcake.model.Scenario.Messages.Message.ValidatorRef;

import java.util.Collections;
import java.util.List;

public class ValidatorRefModel extends AbstractModel {

	public static final String PROPERTY_ID = "validatorRef-id";

	private ValidatorRef validatorRef;

	ValidatorRefModel(ValidatorRef validatorRef) {
		super();
		if (validatorRef == null) {
			throw new IllegalArgumentException("ValidatorRef must not be null.");
		}
	}

	public ValidatorRefModel() {
		super();
		validatorRef = new ValidatorRef();
	}

	/**
	 * This method should not be used for modifying validatorRef (in a way getValidatorRef().setId()))
	 * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
	 *
	 * @return PerfCake model of validatorRef
	 */
	ValidatorRef getValidatorRef() {
		return validatorRef;
	}

	public String getId() {
		return validatorRef.getId();
	}

	public void setId(String id) {
		final String oldId = getValidatorRef().getId();
		validatorRef.setId(id);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_ID, oldId, id);
	}

	@Override
	public List<AbstractModel> getModelChildren() {
		return Collections.emptyList();
	}

}
