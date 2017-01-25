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

import java.util.ArrayList;
import java.util.List;

import org.perfcake.ide.core.exception.MapperException;

/**
 * This class defines helper methods for mapping between perfcake model and pc4ide model.
 *
 * @author jknetl
 */
final class MapperUtils {

    private MapperUtils() {
    }

    /**
     * Converts list of perfcake model objects to pc4ide model objects.
     *
     * @param perfcakeList List of perfcake model objects
     * @param mapper       mapper where the relations between model is captured
     * @return List of pc4ide model objects which corresponds to the perfcakeList model objects.
     * @throws MapperException    when mapping for some of the objects in the list is not found
     * @throws ClassCastException if pc4ide object has wrong type.
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
