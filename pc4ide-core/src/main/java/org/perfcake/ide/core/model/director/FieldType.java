package org.perfcake.ide.core.model.director;

/**
 * Represents type of the field.
 */
public enum FieldType {
	/**
	 * Simple field represents single value field (etc. int, double, String)
	 */
	SIMPLE,

	/**
	 * Property is a simple field which has always String value, but it is special field because it is
	 * specific for particular component implementation. If you change class field of the component then
	 * its PROPERTY fields will probably no longer apply.
 	 */
	PROPERTY,
	/**
	 * PERFCAKE field is represented by enclosing class defined in perfcake/pc4ide (e.g. DestinationModel)
	 */
	PERFCAKE,

	/**
	 * The field is represented by the collection of elements
	 */
	COLLECTION;
}
