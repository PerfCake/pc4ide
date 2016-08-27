/**
 *
 */
package org.perfcake.ide.core.model;

import org.perfcake.ide.core.exception.MapperException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines helper methods for mapping between perfcake model and pc4ide model
 *
 * @author jknetl
 *
 */
final class MapperUtils {

	private MapperUtils() {
	}

	/**
	 *
	 * Converts list of perfcake model objects to pc4ide model objects.
	 *
	 * @param perfcakeList List of perfcake model objects
	 * @param mapper mapper where the relations between model is captured
	 * @throws MapperException when mapping for some of the objects in the list is not found
	 * @throws ClassCastException if pc4ide object has wrong type.
	 * @return List of pc4ide model objects which corresponds to the perfcakeList model objects.
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getPc4ideList(List<?> perfcakeList, Mapper mapper) throws MapperException, ClassCastException {
		final List<T> result = new ArrayList<T>();

		for (final Object o : perfcakeList) {
			final AbstractModel pc4ideModel = mapper.getModel(o);
			if (pc4ideModel == null) {
				throw new MapperException(new StringBuilder().append("Cannot find mapping for: ")
						.append(o)
						.append(" in mapper: ")
						.append(mapper)
						.toString());
			}

			result.add((T) pc4ideModel);
		}

		return result;

	}
}
