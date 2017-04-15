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

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import java.nio.file.Paths;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.perfcake.ide.core.exception.PerfCakeResourceException;
import org.perfcake.ide.core.exec.PerfCakeExecutor;
import org.perfcake.ide.intellij.IntellijUtils;
import org.perfcake.ide.intellij.PerfCakeIcons;
import org.perfcake.ide.intellij.VirtualFileConverter;
import org.perfcake.ide.intellij.module.PerfCakeModuleType;
import org.perfcake.ide.intellij.settings.Pc4ideSettings;

/**
 * Run configuration factory.
 *
 * @author Jakub Knetl
 */
public class PerfCakeConfigurationFactory extends ConfigurationFactory {

    static final Logger logger = Logger.getInstance(PerfCakeConfigurationFactory.class);

    protected PerfCakeConfigurationFactory(@NotNull ConfigurationType type) {
        super(type);
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        PerfCakeRunConfiguration config = new PerfCakeRunConfiguration(project, this, "PerfCake");
        PerfCakeExecutor executor = config.getPerfCakeExecutor();
        VirtualFile messagesDir = project.getBaseDir().findChild(PerfCakeModuleType.MESSAGES_DIR_NAME);
        if (messagesDir != null) {
            try {
                executor.setMessageDir(VirtualFileConverter.convertPath(messagesDir));
            } catch (PerfCakeResourceException e) {
                Notification notification = IntellijUtils.createNotification("Cannot set messages dir", NotificationType.WARNING);
                logger.warn("Cannot set messages dir", e);
            }
        }

        VirtualFile pluginDir = project.getBaseDir().findChild(PerfCakeModuleType.PLUGINS_DIR_NAME);
        if (pluginDir != null) {
            try {
                executor.setPluginDir(VirtualFileConverter.convertPath(pluginDir));
            } catch (PerfCakeResourceException e) {
                Notification notification = IntellijUtils.createNotification("Cannot set plugin dir", NotificationType.WARNING);
                logger.warn("Cannot set plugin dir", e);
            }
        }

        VirtualFile scenarioDir = project.getBaseDir().findChild(PerfCakeModuleType.SCENARIOS_DIR_NAME);
        if (scenarioDir != null) {
            try {
                executor.setScenarioDir(VirtualFileConverter.convertPath(scenarioDir));
            } catch (PerfCakeResourceException e) {
                Notification notification = IntellijUtils.createNotification("Cannot set scenario dir", NotificationType.WARNING);
                logger.warn("Cannot set scenario dir", e);
            }
        }

        String perfCakeHome = PropertiesComponent.getInstance().getValue(Pc4ideSettings.INSTALLATION_DIR_KEY, "");
        if (!perfCakeHome.isEmpty()) {
            executor.setPerfCakeHome(Paths.get(perfCakeHome));
        }
        return config;
    }

    @Override
    public Icon getIcon() {
        return PerfCakeIcons.SMALL_PERFCAKE_ICON;
    }
}
