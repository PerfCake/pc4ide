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

package org.perfcake.ide.core.model.serialization;

import java.io.InputStream;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.exception.ModelSerializationException;
import org.perfcake.ide.core.model.components.ScenarioModel;

/**
 * Model loader is able to load pc4ide scenario model.
 * @author Jakub Knetl
 */
public interface ModelLoader {
    /**
     * Loads a model of given scenario.
     *
     * @param inputStream input stream with scenario definition
     * @return Scenario model
     * @throws ModelSerializationException when model cannot be deserialized.
     * @throws ModelConversionException    when PerfCake model can't be converted to pc4ide model.
     */
    ScenarioModel loadModel(InputStream inputStream) throws ModelSerializationException, ModelConversionException;
}
