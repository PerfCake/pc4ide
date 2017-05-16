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

package org.perfcake.ide.intellij.dialogs;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import javax.swing.JComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.perfcake.ide.editor.project.ProjectConstants;

/**
 * Creates new Scenario dialog.
 *
 * @author Jakub Knetl
 */
public class ScenarioDialog extends DialogWrapper {

    private ScenarioForm scenarioForm;
    private Project project;
    private Module module;
    private VirtualFile file;

    /**
     * Creates new scenario dialog.
     *
     * @param project project
     * @param module  module
     * @param file    file
     */
    public ScenarioDialog(@NotNull Project project, @Nullable Module module, @Nullable VirtualFile file) {
        super(project);
        init();
        setTitle("New PerfCake Scenario");
        this.module = module;
        this.file = file;
        this.project = project;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        scenarioForm = new ScenarioForm();
        return scenarioForm;
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (scenarioForm == null) {
            return new ValidationInfo("Scenario form has not been initialized yet.");
        }
        if (scenarioForm.getScenarioName() == null || scenarioForm.getScenarioName().isEmpty()) {
            return new ValidationInfo("Fill in scenario file.", scenarioForm.getNameTextField());
        }
        if (getScenarioDir().findChild(getScenarioFileName()) != null) {
            return new ValidationInfo(String.format("Scenario file '%s' already exists.",
                    getScenarioName()), scenarioForm.getNameTextField());
        }

        return null;
    }

    /**
     * @return Parent directory for  scenario file.
     */
    public VirtualFile getScenarioDir() {
        VirtualFile path = null;
        if (file != null) {
            if (file.isDirectory()) {
                path = file; // use selected directory
            } else {
                path = file.getParent(); // use parent directory of selected file
            }
        } else {
            // use scenario dir if exists
            VirtualFile scenariosDir = project.getBaseDir().findChild(ProjectConstants.SCENARIO_DIR);
            if (scenariosDir != null) {
                path = scenariosDir;
            } else {
                path = project.getBaseDir();
            }
        }

        return path;
    }

    public String getScenarioName() {
        return scenarioForm.getScenarioName();
    }

    public String getScenarioFileName() {
        return scenarioForm.getScenarioFileName();
    }

    public String getType() {
        return scenarioForm.getType();
    }
}
