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
	 * PERFCAKE field is represented by enclosing class defined in perfcake/pc4ide (e.g. DestinationModel)
	 */
	PERFCAKE,

	/**
	 * The field is represented by the collection of elements
	 */
	COLLECTION;
}
