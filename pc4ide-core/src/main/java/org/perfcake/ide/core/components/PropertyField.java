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
}
