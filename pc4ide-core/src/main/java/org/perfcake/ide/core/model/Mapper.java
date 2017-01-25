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

package org.perfcake.ide.core.model;

import org.perfcake.ide.core.exception.MapperException;

/**
 * Mapper is used to store reversed relationship between pc4ide model and perfcake model.
 * Every pc4ide model has perfcake model as its member field. The mapper serves for obtaining
 * pc4ide model for given field.
 *
 * @author jknetl
 */
public interface Mapper {

    /**
     * Binds a perfcake model with pc4ide model.
     *
     * @param perfcakeModel PerfCake model
     * @param pc4ideModel pc4ide model
     */
    void bind(Object perfcakeModel, AbstractModel pc4ideModel);

    /**
     * Unbinds perfcake model from pc4ide model.
     *
     * @param perfcakeModel PerfCake model
     * @param pc4ideModel pc4ide model
     * @throws MapperException when there is no binding between such perfcakeModel and pc4ideModel
     */
    void unbind(Object perfcakeModel, AbstractModel pc4ideModel) throws MapperException;

    /**
     * Find a pc4ide model based on given PerfCake model.
     * @param perfcakeModel PerfCake model
     * @return pc4ide model for perfcake model or null if no such perfcake model was binded to pc4ide model in
     *     this mapper instance.
     */
    AbstractModel getModel(Object perfcakeModel);
}
