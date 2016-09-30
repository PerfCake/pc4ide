package org.perfcake.ide.core.model.director;

/**
 * ModelField represents a field in the model.
 *
 * @author jknetl
 */
public class ModelField {

	private FieldType fieldType;
	private String name;
	private String docs;

	public ModelField(FieldType fieldType, String name, String docs) {
		if (fieldType == null){
			throw new IllegalArgumentException("Field type must not be null.");
		}
		if(name == null || name.isEmpty()){
			throw new IllegalArgumentException("Name must not be null or empty.");
		}

		this.fieldType = fieldType;
		this.name = name;
		this.docs = docs;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ModelField that = (ModelField) o;

		if (fieldType != that.fieldType) return false;
		return name.equals(that.name);
	}

	@Override
	public int hashCode() {
		int result = fieldType.hashCode();
		result = 31 * result + name.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "ModelField{" +
				"fieldType=" + fieldType +
				", name='" + name + '\'' +
				", docs='" + docs + '\'' +
				'}';
	}
}
