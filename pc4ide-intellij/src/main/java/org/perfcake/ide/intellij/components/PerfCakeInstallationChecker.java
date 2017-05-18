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

package org.perfcake.ide.intellij.components;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import java.nio.file.Paths;
import javax.swing.event.HyperlinkEvent;
import org.jetbrains.annotations.NotNull;
import org.perfcake.ide.core.exec.PerfCakeInstallationValidator;
import org.perfcake.ide.core.exec.SimpleInstallationValidator;
import org.perfcake.ide.intellij.IntellijUtils;
import org.perfcake.ide.intellij.settings.Pc4ideSettings;

/**
 * PerfCakeInstallationChecker components checks whether perfcake installation directory is set and if it is correct. If it is incorrect,
 * then user is notified to set it up.
 *
 * @author Jakub Knetl
 */
public class PerfCakeInstallationChecker implements ApplicationComponent {

    public static final String PERFCAKE_DIR_CONFIG = "perfcake-dir-config";

    private PerfCakeInstallationValidator validator = new SimpleInstallationValidator();

    @Override
    public void initComponent() {
        validateInstallationDir();
    }

    /**
     * Validates if PerfCake installation dir is set properly. If not, then notification is displayed.
     */
    public void validateInstallationDir() {
        String installationDir = PropertiesComponent.getInstance().getValue(Pc4ideSettings.INSTALLATION_DIR_KEY, "");
        String content = null;
        if (installationDir.isEmpty() || !validator.isValid(Paths.get(installationDir))) {
            content = "PerfCake installation directory is not configured. <a href='" + PERFCAKE_DIR_CONFIG + "'>Configure perfcake dir</a>";
        } else if (!validator.isValid(Paths.get(installationDir))) {
            content = "PerfCake installation directory points to invalid location.  <a href='" + PERFCAKE_DIR_CONFIG + "'>Configure perfcake dir</a>";

        }
        if (content != null) {
            Notification notification = IntellijUtils.createNotification("Invalid PerfCake installation dir", NotificationType.WARNING)
                    .setContent(content)
                    .setListener((n, e) -> {
                        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                            if (PERFCAKE_DIR_CONFIG.equals(e.getDescription())) {
                                Project project = ProjectManager.getInstance().getDefaultProject();
                                ShowSettingsUtil.getInstance().showSettingsDialog(project, Pc4ideSettings.class);
                            }
                        }

                    });
            Notifications.Bus.notify(notification);
        }
    }

    @Override
    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "PerfCakeInstallationChecker";
    }
}
