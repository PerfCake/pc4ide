package org.perfcake.ide.core.components;

/**
 * Represents property field of the perfcake component.
 *
 * @author jknetl
 *
 */
public class PropertyField {

	/**
	 * name of the property field
	 */
	private String name;

	/**
	 * documentation of the property field
	 */
	private String documentation;

	/**
	 *  is property field mandatory
	 */
	private boolean mandatory;

	/**
	 *
	 * @param name
	 * @param documentation
	 * @param mandatory
	 */
	public PropertyField(String name, String documentation, boolean mandatory) {
		super();
		this.name = name;
		this.documentation = documentation;
		this.mandatory = mandatory;
	}

	/**
	 *
	 * @return Name of the field
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @return documentation of the field
	 */
	public String getDocumentation() {
		return documentation;
	}

	/**
	 *
	 * @return is field mandatory?
	 */
	public boolean isMandatory() {
		return mandatory;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PropertyField that = (PropertyField) o;

		if (mandatory != that.mandatory) return false;
		return name != null ? name.equals(that.name) : that.name == null;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (mandatory ? 1 : 0);
		return result;
	}

	@Override
	public String toString() {
		return "PropertyField{" +
				"name='" + name + '\'' +
				", documentation='" + documentation + '\'' +
				", mandatory=" + mandatory +
				'}';
	}
}
