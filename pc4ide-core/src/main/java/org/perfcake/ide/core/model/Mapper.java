package org.perfcake.ide.core.model;

import org.perfcake.ide.core.exception.MapperException;

/**
 * Mapper is used to store reversed relationship between pc4ide model and perfcake model.
 * Every pc4ide model has perfcake model as its member field. The mapper serves for obtaining
 * pc4ide model for given field.
 * @author jknetl
 *
 */
public interface Mapper {

	/**
	 * binds perfcake model with pc4ide model
	 *
	 * @param perfcakeModel
	 * @param pc4ideModel
	 */
	void bind(Object perfcakeModel, AbstractModel pc4ideModel);

	/**
	 * unbinds perfcake model with pc4ide model
	 *
	 * @param perfcakeModel
	 * @param pc4ideModel
	 * @throws MapperException when there is no binding between such perfcakeModel and pc4ideModel
	 */
	void unbind(Object perfcakeModel, AbstractModel pc4ideModel) throws MapperException;

	/**
	 *
	 * @param perfcakeModel
	 * @return pc4ide model for perfcake model or null if no such perfcake model was binded to pc4ide model in
	 * this mapper instance.
	 */
	AbstractModel getModel(Object perfcakeModel);
}
