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

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JComponent;
import org.jetbrains.annotations.NotNull;
import org.perfcake.ide.core.exec.PerfCakeExecutor;
import org.perfcake.ide.core.utils.PathUtils;

/**
 * Represents UI for PerfCake Run configuration.
 *
 * @author Jakub Knetl
 */
public class PerfCakeSettingsEditor extends SettingsEditor<PerfCakeRunConfiguration> {

    private final Project project;
    private PerfCakeRunConfigPanel perfCakePanel;

    public PerfCakeSettingsEditor(Project project) {
        this.project = project;
    }

    @Override
    protected void resetEditorFrom(@NotNull PerfCakeRunConfiguration s) {
        PerfCakeExecutor e = s.getPerfCakeExecutor();

        String scenarioDir = PathUtils.pathToString(e.getScenarioDir());
        StringBuilder scenarioPath = new StringBuilder()
                .append(scenarioDir)
                .append(File.separator);

        String scenarioName = e.getScenario();
        if (!scenarioDir.isEmpty() && scenarioName != null) {
            scenarioPath.append(scenarioName);
        }

        perfCakePanel.setScenarioPath(scenarioPath.toString());
        if (e.getJavaHome() != null) {
            perfCakePanel.setJavaHome(PathUtils.pathToString(e.getJavaHome()));
        }
        perfCakePanel.setPerfcakeHomeField(PathUtils.pathToString(e.getPerfCakeHome()));
        perfCakePanel.setMessageDir(PathUtils.pathToString(e.getMessageDir()));
        perfCakePanel.setPluginDir(PathUtils.pathToString(e.getPluginDir()));
        perfCakePanel.setSystemProperties(e.getSystemProperties());

        perfCakePanel.setDebugMode(e.isDebugMode());
        if (e.getDebugName() != null) {
            perfCakePanel.setDebugAgentName(e.getDebugName());
        }

    }

    @Override
    protected void applyEditorTo(@NotNull PerfCakeRunConfiguration s) throws ConfigurationException {
        PerfCakeExecutor executor = s.getPerfCakeExecutor();
        if (!perfCakePanel.getScenarioPath().isEmpty()) {
            Path scenarioPath = Paths.get(perfCakePanel.getScenarioPath());
            executor.setScenarioDir(scenarioPath.getParent());
            executor.setScenario(PathUtils.pathToString(scenarioPath.getFileName()));
        }
        executor.setPerfCakeHome(Paths.get(perfCakePanel.getPerfcakeHome()))
                .setJavaHome(Paths.get(perfCakePanel.getJavaHome()))
                .setMessageDir(Paths.get(perfCakePanel.getMessageDir()))
                .setPluginDir(Paths.get(perfCakePanel.getPluginDir()));

        executor.setDebugMode(perfCakePanel.isDebugMode());
        if (!perfCakePanel.getDebugAgentName().isEmpty()) {
            executor.setDebugName(perfCakePanel.getDebugAgentName());
        }
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        perfCakePanel = new PerfCakeRunConfigPanel(project, true);
        return perfCakePanel;
    }

}
