package org.perfcake.ide.core.components;

import java.util.List;

/**
 *
 * Represents PerfCake component.
 *
 * @author jknetl
 *
 */
public class Component {

	/**
	 * Kind of the component
	 */
	private ComponentKind kind;

	/**
	 * class of the component
	 */
	private Class<?> implementation;

	/**
	 * List of the fields in the component
	 */
	private List<PropertyField> propertyFields;

	/**
	 * Documentation of the component
	 */
	private String documentation;

	public Component(ComponentKind kind, Class<?> implementation, List<PropertyField> fields, String documentation) {
		super();
		this.kind = kind;
		this.implementation = implementation;
		this.propertyFields = fields;
		this.documentation = documentation;
	}

	/**
	 *
	 * @return Kind of the component
	 */
	public ComponentKind getKind() {
		return kind;
	}

	/**
	 *
	 * @return class which implements the component
	 */
	public Class<?> getImplementation() {
		return implementation;
	}

	/**
	 *
	 * @return List of the property fields of the component
	 */
	public List<PropertyField> getPropertyFields() {
		return propertyFields;
	}

	/**
	 *
	 * @return Documentation of the component
	 */
	public String getDocumentation() {
		return documentation;
	}

	/**
	 * @param fieldName simple name of the field
	 * @return PropertyField with given name or null if no such field can be found.
	 */
	public PropertyField getPropertyField(String fieldName) {

		if (fieldName == null) {
			return null;
		}

		PropertyField result = null;

		for (final PropertyField field : propertyFields){
			if (fieldName.equals(field.getName())) {
				result = field;
			}
		}

		return result;
	}
}
