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

package org.perfcake.ide.core.manager;

import java.nio.file.Path;
import org.apache.commons.lang3.SerializationException;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.exception.ModelSerializationException;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.components.ScenarioModel;

/**
 * Scenario manager manages a scenario file.
 *
 * @author Jakub Knetl
 */
public interface ScenarioManager {

    /**
     * Loads scenario from its location and build Scenario Model.
     *
     * @return Scenario model
     * @throws ModelSerializationException   If scenario cannot be loaded.
     * @throws ModelConversionException if model cannot be converted from its persistent representation.
     */
    ScenarioModel loadScenarioModel() throws ModelSerializationException, ModelConversionException;


    /**
     * Writes scenario model to a file.
     *
     * @param model Model to be written.
     * @throws ModelSerializationException   if model cannot be serialized
     * @throws ModelConversionException if model cannot be converted to its persistent representaiton.
     */
    void writeScenario(ScenarioModel model) throws ModelSerializationException, ModelConversionException;

    Path getScenarioLocation();

    void setScenarioLocation(Path path);

}
