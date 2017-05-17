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

package org.perfcake.ide.intellij;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;
import org.perfcake.ide.core.components.PerfCakeComponent;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.exception.ModelSerializationException;
import org.perfcake.ide.core.exception.PerfCakeResourceException;
import org.perfcake.ide.core.manager.ScenarioManager;
import org.perfcake.ide.core.manager.ScenarioManagers;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.editor.ServiceManager;
import org.perfcake.ide.intellij.dialogs.ScenarioDialog;

/**
 * Action which creates new scenario.
 *
 * @author Jakub Knetl
 */
public class NewScenarioAction extends AnAction {

    static final Logger logger = Logger.getInstance(NewScenarioAction.class);

    @Override
    public void actionPerformed(AnActionEvent e) {
        VirtualFile file = e.getData(DataKeys.VIRTUAL_FILE);
        Module module = e.getData(DataKeys.MODULE);
        Project project = e.getProject();

        ScenarioDialog dialog = new ScenarioDialog(project, module, file);
        dialog.show();

        if (dialog.getExitCode() == 0) {
            VirtualFile scenarioDir = dialog.getScenarioDir();
            String scenarioName = dialog.getScenarioFileName();
            Path scenarioPath = null;
            try {
                scenarioPath = VirtualFileConverter.convertPath(scenarioDir);
            } catch (PerfCakeResourceException e1) {
                Notification error = IntellijUtils.createNotification("Cannot create scenario", NotificationType.ERROR)
                        .setContent("Cannot convert path. See log file for more details");
                Notifications.Bus.notify(error, project);
                logger.error("Cannot create scenario file in directory: " + scenarioDir, e1);
            }

            scenarioPath = scenarioPath.resolve(scenarioName);
            ScenarioManager manager = null;

            switch (dialog.getType()) {
                case "xml":
                    manager = ScenarioManagers.createXmlManager(scenarioPath);
                    break;
                case "dsl":
                    manager = ScenarioManagers.createDslManager(scenarioPath);
                    break;
                default:
                    logger.warn("Unknown type of scenario: " + dialog.getType());
                    return;
            }

            //create scenario
            ServiceManager serviceManager = ApplicationManager.getApplication().getComponent(ServiceManager.class);
            ScenarioModel model = (ScenarioModel) serviceManager.getModelFactory().createModel(PerfCakeComponent.SCENARIO);
            try {
                manager.writeScenario(model);
                scenarioDir.refresh(false, false);
                VirtualFile scenarioFile = dialog.getScenarioDir().findChild(scenarioName);
                OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, scenarioFile);
                openFileDescriptor.navigate(true);
            } catch (ModelSerializationException | ModelConversionException e1) {
                Notification error = IntellijUtils.createNotification("Cannot create scenario", NotificationType.ERROR)
                        .setContent(String.format("Caused by: %s. See log for more details", e1.getMessage()));
                Notifications.Bus.notify(error, project);
                logger.error("Cannot create scenario", e1);
                return;
            }

        }

    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        VirtualFile file = e.getData(DataKeys.VIRTUAL_FILE);
        if (file == null) {
            e.getPresentation().setEnabledAndVisible(false);
        }
    }
}
