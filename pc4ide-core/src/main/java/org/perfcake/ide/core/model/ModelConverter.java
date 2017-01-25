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

import org.perfcake.model.Scenario;

/**
 * Model converter converts between perfcake model and pc4ide model.
 *
 * @author jknetl
 */
final class ModelConverter {

    private ModelConverter() {
    }

    public static ScenarioModel getPc4ideModel(Scenario perfcakeModel) {
        if (perfcakeModel == null) {
            throw new IllegalArgumentException("Model must not be null.");
        }

        final ScenarioModel model = new ScenarioModel(perfcakeModel);

        return model;

    }

    public static Scenario getPerfcakeModel(ScenarioModel pc4ideModel) {
        if (pc4ideModel == null) {
            throw new IllegalArgumentException("Model must not be null.");
        }

        return pc4ideModel.getScenario();

    }

}
