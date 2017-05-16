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

package org.perfcake.ide.editor.actions.handlers;

import java.awt.geom.Point2D;
import org.perfcake.ide.core.exception.Pc4ideException;
import org.perfcake.ide.editor.actions.ActionType;
import org.perfcake.ide.editor.controller.ExecutionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Run Handler is able to run PerfCake scenario.
 *
 * @author Jakub Knetl
 */
public class RunHandler extends AbstractHandler {

    static final Logger logger = LoggerFactory.getLogger(RunHandler.class);

    public RunHandler() {
        super(ActionType.RUN);
    }

    @Override
    public void handleAction(Point2D location) {
        ExecutionFactory executionFactory = getController().getRoot().getExecutionFactory();

        if (executionFactory == null) {
            logger.warn("Skipping execution. Execution manager has not been set");
        } else {
            try {
                executionFactory.execute(getController().getRoot().getScenarioManager());
            } catch (Pc4ideException e) {
                logger.warn("Exception when executing scenario", e);
            }
        }
    }
}
