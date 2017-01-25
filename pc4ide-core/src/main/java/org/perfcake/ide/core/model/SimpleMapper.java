/*
 *-----------------------------------------------------------------------------
 * pc4ide
 *
 * Copyright 2017 Jakub Knetl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *-----------------------------------------------------------------------------
 */
/**
 *
 */

package org.perfcake.ide.core.model;

import java.util.HashMap;
import java.util.Map;

import org.perfcake.ide.core.exception.MapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of simple mapper. Simple mapper uses map to be able to translate perfcake model and pc4ide model back
 * and forth.
 *
 * @author jknetl
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

        if (pc4ideFromMap == null) {
            throw new MapperException("There is no such perfcake model in the mapper: " + perfcakeModel);
        }
        if (!pc4ideFromMap.equals(pc4ideModel)) {
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
