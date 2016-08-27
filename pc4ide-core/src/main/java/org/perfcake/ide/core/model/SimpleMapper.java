/**
 *
 */
package org.perfcake.ide.core.model;

import org.perfcake.ide.core.exception.MapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jknetl
 *
 */
public class SimpleMapper implements Mapper {

	static final Logger logger = LoggerFactory.getLogger(SimpleMapper.class);

	private Map<Object, AbstractModel> map = new HashMap<>();

	/* (non-Javadoc)
	 * @see org.perfcake.ide.core.model.Mapper#bind(java.lang.Object, org.perfcake.ide.core.model.AbstractModel)
	 */
	@Override
	public void bind(Object perfcakeModel, AbstractModel pc4ideModel) {
		map.put(perfcakeModel, pc4ideModel);
		logger.debug("Creating binding between: {} and {}", perfcakeModel, pc4ideModel);
	}

	/* (non-Javadoc)
	 * @see org.perfcake.ide.core.model.Mapper#unbind(java.lang.Object, org.perfcake.ide.core.model.AbstractModel)
	 */
	@Override
	public void unbind(Object perfcakeModel, AbstractModel pc4ideModel) throws MapperException {
		final AbstractModel pc4ideFromMap = map.get(perfcakeModel);

		if (pc4ideFromMap == null){
			throw new MapperException("There is no such perfcake model in the mapper: " + perfcakeModel);
		}
		if (!pc4ideFromMap.equals(pc4ideModel)){
			throw new MapperException("There is different binding for perfcake model in the mapper.");
		}

		logger.debug("Removing binding between: {} and {}", perfcakeModel, pc4ideModel);
		map.remove(perfcakeModel);
	}

	/* (non-Javadoc)
	 * @see org.perfcake.ide.core.model.Mapper#getModel(java.lang.Object)
	 */
	@Override
	public AbstractModel getModel(Object perfcakeModel) {
		return map.get(perfcakeModel);
	}

}
