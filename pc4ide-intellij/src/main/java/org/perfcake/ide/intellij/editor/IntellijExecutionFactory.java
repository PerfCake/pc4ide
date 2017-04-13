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

package org.perfcake.ide.intellij.editor;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.ui.playback.commands.ActionCommand;
import com.intellij.openapi.util.ActionCallback;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.perfcake.ide.core.exception.Pc4ideException;
import org.perfcake.ide.core.manager.ScenarioManager;
import org.perfcake.ide.editor.controller.ExecutionFactory;

/**
 * Retrieves or creates  Intellij  PerfCake run configuration and executes scenario.
 *
 * @author Jakub Knetl
 */
public class IntellijExecutionFactory implements ExecutionFactory {

    @Override
    public void execute(ScenarioManager scenarioManager) throws Pc4ideException {
        String actionId = "ChooseRunConfiguration";
        AnAction action = ActionManager.getInstance().getAction(actionId);
        String[] actionIds = ActionManager.getInstance().getActionIds("");

        Path file = Paths.get("/home/jknetl/tmp/idea-actions");

        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (String id : actionIds) {
                writer.write(id);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (action == null) {
            //TODO: log and notify
            return;
        }

        ActionCallback actionCallback = ActionManager.getInstance().tryToExecute(action, ActionCommand.getInputEvent(actionId),
                null, ActionPlaces.RUNNER_TOOLBAR, true);
    }
}
