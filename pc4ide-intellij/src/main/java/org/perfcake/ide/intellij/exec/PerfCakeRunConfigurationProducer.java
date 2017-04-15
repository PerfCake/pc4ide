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

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.lang3.StringUtils;
import org.perfcake.ide.core.exception.PerfCakeResourceException;
import org.perfcake.ide.core.exec.PerfCakeExecutor;
import org.perfcake.ide.intellij.IntellijUtils;
import org.perfcake.ide.intellij.VirtualFileConverter;
import org.perfcake.ide.intellij.editor.PerfCakeScenarioUtil;

/**
 * PerfCake run configuration producer is able to create PerfCake run configuration from context.
 *
 * @author Jakub Knetl
 */
public class PerfCakeRunConfigurationProducer extends RunConfigurationProducer<PerfCakeRunConfiguration> {

    static final Logger logger = Logger.getInstance(PerfCakeRunConfigurationProducer.class);

    public PerfCakeRunConfigurationProducer() {
        this(new PerfCakeRunConfigurationType());
    }

    protected PerfCakeRunConfigurationProducer(ConfigurationFactory configurationFactory) {
        super(configurationFactory);
    }

    protected PerfCakeRunConfigurationProducer(ConfigurationType configurationType) {
        super(configurationType);
    }

    @Override
    protected boolean setupConfigurationFromContext(PerfCakeRunConfiguration configuration,
                                                    ConfigurationContext context, Ref<PsiElement> sourceElement) {
        boolean isPerfcake = false;
        VirtualFile file = null;
        try {
            file = context.getLocation().getVirtualFile();
        } catch (NullPointerException e) {
            // do nothing
        }

        if (file != null && PerfCakeScenarioUtil.isPerfCakeScenario(file)) {
            try {
                PerfCakeExecutor perfCakeExecutor = configuration.getPerfCakeExecutor();
                perfCakeExecutor.setScenarioDir(VirtualFileConverter.convertPath(file.getParent()));
                perfCakeExecutor.setScenario(file.getName());
                isPerfcake = true;
                configuration.setGeneratedName();
            } catch (PerfCakeResourceException e) {
                Notification notification = IntellijUtils.createNotification("Cannot execute scenario", NotificationType.WARNING)
                        .setContent("Cannot convert path: " + file.getParent());

                Notifications.Bus.notify(notification);
                logger.warn("Cannot execute scenario", e);
            }
        }
        return isPerfcake;
    }

    @Override
    public boolean isConfigurationFromContext(PerfCakeRunConfiguration configuration, ConfigurationContext context) {
        PerfCakeExecutor perfCakeExecutor = configuration.getPerfCakeExecutor();
        String scenarioDir = perfCakeExecutor.getScenarioDir() == null ? "" : perfCakeExecutor.getScenarioDir().toString();
        if (StringUtils.isNotBlank(perfCakeExecutor.getScenario()) && StringUtils.isNotBlank(scenarioDir)) {

            VirtualFile file;
            try {
                file = context.getLocation().getVirtualFile();
                Path contextPath = VirtualFileConverter.convertPath(file);
                return Files.isSameFile(contextPath, perfCakeExecutor.getScenarioDir().resolve(perfCakeExecutor.getScenario()));
            } catch (NullPointerException | PerfCakeResourceException | IOException e) {
                logger.warn("Cannot verify if configuration file was created from context", e);
            }

        }
        return false;
    }
}
