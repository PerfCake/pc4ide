package org.perfcake.ide.core;

import org.perfcake.ide.core.model.director.FieldType;
import org.perfcake.ide.core.model.director.ModelField;

/**
 * Represents a field of a component.
 */
public class Field {

	protected FieldType fieldType;
	protected String name;
	protected String docs;
	protected boolean isMandatory;

	public Field(FieldType fieldType, String name, String docs, boolean isMandatory) {
		if (fieldType == null){
			throw new IllegalArgumentException("Field type must not be null.");
		}
		if(name == null || name.isEmpty()){
			throw new IllegalArgumentException("Name must not be null or empty.");
		}
		this.name = name;
		this.fieldType = fieldType;
		this.docs = docs;
		this.isMandatory = isMandatory;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public String getName() {
		return name;
	}

	public String getDocs() {
		return docs;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Field field = (Field) o;

		if (isMandatory != field.isMandatory) return false;
		if (fieldType != field.fieldType) return false;
		return name.equals(field.name);
	}

	@Override
	public int hashCode() {
		int result = fieldType.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + (isMandatory ? 1 : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Field{" +
				"fieldType=" + fieldType +
				", name='" + name + '\'' +
				", docs='" + docs + '\'' +
				", isMandatory=" + isMandatory +
				'}';
	}
}
