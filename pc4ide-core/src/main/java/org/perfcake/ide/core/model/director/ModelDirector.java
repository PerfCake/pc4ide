package org.perfcake.ide.core.model.director;

import org.perfcake.ide.core.Field;
import org.perfcake.ide.core.components.ComponentManager;
import org.perfcake.ide.core.components.PropertyField;
import org.perfcake.ide.core.exception.ModelDirectorException;

import java.util.List;

/**
 * ModelDirector is used to manipulate with arbitrary model class in a generic and uniform way.
 *
 * There are two types of fields in the model:
 * <ol>
 *     <li>model fields</li>
 *     <li>custom property fields</li>
 * </ol>
 *
 * Model fields are fields which are defined statically in the perfcake XML model. The model fields
 * are dependent just on the specific model.
 *
 * Custom property fields are the fields which are specific for some model implementation. So they are dependent
 * on model class and the particular implementation (usually specified by the class field in the model class)
 *
 * @author jknetl
 */
public interface ModelDirector {

	/**
	 * @return Model object which is controled by the director.
	 */
	Object getModel();

	/**
	 * @return Documentation of the model
	 */
	String getDocs();

	/**
	 *
	 * @return List of all fields applicable to model.
	 */
	List<Field> getAllFields();

	/**
	 * Return list of fields which are defined by the model. Custom property fields are ignored
	 * since they are handled by {@link #getCustomPropertyFields()}
	 */
	List<ModelField> getModelFields();

	/**
	 * Finds a field by name. It searches through all fields (both model fields and custom property fields)
	 * @param name name of the field
	 * @return a field with given name or null if no such field can be found.
	 */
	Field getFieldByName(String name);

	/**
	 * current value of the field
	 * @param field
	 * @return current value of the field
	 * @throws ModelDirectorException if no such field can be found
	 */
	Object getFieldValue(Field field) throws ModelDirectorException;

	/**
	 * Sets field to given value.
	 *
	 * @param field field to set
	 * @param value new value of the field
	 * @throws ModelDirectorException when field does not correspond to model or setMethod cannot be found.
	 */
	void setField(Field field, Object value) throws ModelDirectorException;


	/**
	 *  Returns list of custom property fields corresponding with specific component implementation.
	 *
	 */
	List<PropertyField> getCustomPropertyFields();
}
