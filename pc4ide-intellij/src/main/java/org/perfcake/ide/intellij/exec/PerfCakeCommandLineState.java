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

package org.perfcake.ide.intellij.exec;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.perfcake.ide.core.exception.PerfCakeResourceException;
import org.perfcake.ide.core.exec.ExecutionManager;
import org.perfcake.ide.core.exec.PerfCakeExecutor;
import org.perfcake.ide.editor.controller.RootController;
import org.perfcake.ide.editor.controller.visitor.AttachDebugManagerVisitor;
import org.perfcake.ide.editor.controller.visitor.ControllerVisitor;
import org.perfcake.ide.intellij.IntellijUtils;
import org.perfcake.ide.intellij.VirtualFileConverter;
import org.perfcake.ide.intellij.editor.ScenarioEditor;

/**
 * Command line state for executing PerfCake scenario.
 *
 * @author Jakub Knetl
 */
public class PerfCakeCommandLineState extends CommandLineState {

    static final Logger logger = Logger.getInstance(PerfCakeCommandLineState.class);

    private PerfCakeExecutor perfCakeExecutor;

    protected PerfCakeCommandLineState(ExecutionEnvironment environment, PerfCakeExecutor perfCakeExecutor) {
        super(environment);
        this.perfCakeExecutor = perfCakeExecutor;
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        Process process = null;
        OSProcessHandler osProcessHandler = null;
        try {
            ExecutionManager manager = perfCakeExecutor.execute();

            attachExecutionManager(manager);
            manager.startWatching();

            osProcessHandler = new OSProcessHandler(manager.getProcess(), String.join(" ", perfCakeExecutor.createCommandLine()));
            osProcessHandler.startNotify();
            ProcessTerminatedListener.attach(osProcessHandler);
        } catch (IOException e) {
            Notification notification = IntellijUtils.createNotification("PerfCake execution error", NotificationType.WARNING)
                    .setContent("Execution cannot finish due to error");

            Notifications.Bus.notify(notification);
            logger.warn("Cannot execute perfcake scenario", e);
        }

        return osProcessHandler;
    }

    /**
     * Attaches execution manager to the all PerfCake editors which edits executed scenario.
     */
    private void attachExecutionManager(ExecutionManager executionManagerImpl) {

        try {
            VirtualFile scenarioFile = VirtualFileConverter.convertPath(
                    perfCakeExecutor.getScenarioDir().resolve(perfCakeExecutor.getScenario()));
            FileEditor[] allEditors = FileEditorManager.getInstance(getEnvironment().getProject()).getAllEditors(scenarioFile);

            for (FileEditor editor : allEditors) {
                if (ScenarioEditor.EDITOR_NAME.equals(editor.getName())
                        && editor instanceof ScenarioEditor) {
                    ScenarioEditor scenarioEditor = (ScenarioEditor) editor;

                    if (scenarioEditor.getFile().getCanonicalPath().equals(scenarioFile.getCanonicalPath())) {
                        RootController controller = scenarioEditor.getScenarioController();
                        ControllerVisitor attachVisitor = new AttachDebugManagerVisitor(executionManagerImpl);
                        controller.accept(attachVisitor);
                    }
                }
            }
        } catch (PerfCakeResourceException e) {
            e.printStackTrace();
        }
    }
}
