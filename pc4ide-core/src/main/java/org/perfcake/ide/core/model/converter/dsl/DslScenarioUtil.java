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

package org.perfcake.ide.core.model.converter.dsl;

import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.model.Scenario;

/**
 * Created by Stanislav Kaleta on 5/14/15.
 *
 * @author  Stanislav Kaleta, Jakub Knetl
 */
public class DslScenarioUtil {

    public static String getDslScenarioFrom(Scenario model, String name) {
        ScenarioBuilder builder = new ScenarioBuilder();
        return builder.buildScenario(model, name);
    }

    public static Scenario getModelFrom(String scenario) throws ModelConversionException {
        ScenarioParser parser = new ScenarioParser();
        return parser.parseScenario(scenario);
    }
}
