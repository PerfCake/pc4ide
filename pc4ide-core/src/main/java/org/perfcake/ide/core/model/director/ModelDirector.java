package org.perfcake.ide.core.model.director;

import org.perfcake.ide.core.components.ComponentManager;
import org.perfcake.ide.core.components.PropertyField;
import org.perfcake.ide.core.exception.ModelDirectorException;

import java.util.List;

/**
 * ModelDirector is used to manipulate with arbitrary model class in a generic and uniform way.
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
	 * Return list of fields which are defined by the model. Custom property fields are ignored
	 * since they are handled by {@link #getCustomProperty()}
	 */
	List<ModelField> getModelFields();

	/**
	 * Finds a model field by name
	 * @param name name of the field
	 * @return Model field with given name or null if no such field can be found.
	 */
	ModelField getModelFieldByName(String name);

	/**
	 * current value of the field
	 * @param field
	 * @return current value of the field
	 * @throws ModelDirectorException if no such field can be found
	 */
	Object getModelFieldValue(ModelField field) throws ModelDirectorException;

	/**
	 * Sets model field to given value.
	 * @param field field to set
	 * @param value new value of the field
	 * @throws ModelDirectorException when field does not correspond to model or setMethod cannot be found.
	 */
	void setModelField(ModelField field, Object value) throws ModelDirectorException;


	/**
	 *  Returns list of custom property fields corresponding with specific component implementation.
	 *
	 */
	List<PropertyField> getCustomProperty();

	/**
	 *
	 * @param name name of the custom property
	 * @return PropertyField or null if no field with the name was found.
	 */
	PropertyField getCustomPropertyByName(String name);

	/**
	 * @param name
	 * @return Value of the property with given name or null if no such property can be found for the given implementation
	 */
	String getCustomPropertyValue(String name);

	/**
	 * sets custom property of the component
	 * @param property
	 * @param value
	 * @throws ModelDirectorException if no such property is supported by the specific component implementation.
	 */
	void setCustomProperty(PropertyField property, String value) throws ModelDirectorException;

}
