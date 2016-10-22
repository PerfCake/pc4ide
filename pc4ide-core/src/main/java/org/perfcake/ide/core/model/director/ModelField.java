package org.perfcake.ide.core.model.director;

import org.perfcake.ide.core.Field;

/**
 * ModelField represents a field in the model.
 *
 * @author jknetl
 */
public class ModelField extends Field {

	public ModelField(FieldType fieldType, String name, String docs, boolean isMandatory) {
		super(fieldType, name, docs, isMandatory);
	}

	public ModelField(FieldType fieldType, String name, String docs) {
		super(fieldType, name, docs, false);
	}
}
