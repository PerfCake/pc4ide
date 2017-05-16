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

package org.perfcake.ide.editor.controller;

import org.perfcake.ide.core.exception.Pc4ideException;
import org.perfcake.ide.core.manager.ScenarioManager;

/**
 * Execution manager is able to execute scenario. Individual plugins for specific IDE should implement the behaviour.
 *
 * @author Jakub Knetl
 */
public interface ExecutionFactory {

    /**
     * Executes scenario.
     *
     * @param scenarioManager manager of the scenario.
     * @throws Pc4ideException if error with execution occurs.
     */
    void execute(ScenarioManager scenarioManager) throws Pc4ideException;
}
