package org.perfcake.ide.core.components;

import org.perfcake.ide.core.Field;
import org.perfcake.ide.core.model.director.FieldType;

/**
 * Represents property field of the perfcake component.
 *
 * @author jknetl
 *
 */
public class PropertyField extends Field {

	public PropertyField(String name, String docs, boolean isMandatory) {
		super(FieldType.PROPERTY, name, docs, isMandatory);
	}
}
