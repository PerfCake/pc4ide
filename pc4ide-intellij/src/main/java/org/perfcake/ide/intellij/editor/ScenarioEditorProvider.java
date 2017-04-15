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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Updated by: Jakub Knetl
 * Date: 17.9.2014
 *
 * @author Stanislav Kaleta, Jakub Knetl
 */
public class ScenarioEditorProvider implements FileEditorProvider, DumbAware {
    private static final Logger LOG = Logger.getInstance(ScenarioEditorProvider.class);
    private static final String EDITOR_TYPE_ID = "PerfCakeEditor";
    private ScenarioEditor scenarioEditor;

    public static ScenarioEditorProvider getInstance() {
        return ApplicationManager.getApplication().getComponent(ScenarioEditorProvider.class);
    }

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        Module module = ModuleUtil.findModuleForFile(file, project);

        return module != null && PerfCakeScenarioUtil.isPerfCakeScenario(file);
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        LOG.assertTrue(accept(project, file));

        scenarioEditor = new ScenarioEditor(project, file);
        return scenarioEditor;
    }

    @Override
    public void disposeEditor(@NotNull FileEditor fileEditor) {
        Disposer.dispose(fileEditor);
    }

    @NotNull
    @Override
    public FileEditorState readState(@NotNull Element element, @NotNull Project project, @NotNull VirtualFile virtualFile) {
        return FileEditorState.INSTANCE;
    }

    @Override
    public void writeState(@NotNull FileEditorState fileEditorState, @NotNull Project project, @NotNull Element element) {
        //null
    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return EDITOR_TYPE_ID;
    }

    @NotNull
    @Override
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.PLACE_BEFORE_DEFAULT_EDITOR;
    }
}
