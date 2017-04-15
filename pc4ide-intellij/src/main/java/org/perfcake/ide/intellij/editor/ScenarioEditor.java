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

import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.perfcake.PerfCakeException;
import org.perfcake.ide.core.components.ReflectionComponentCatalogue;
import org.perfcake.ide.core.exception.Pc4ideException;
import org.perfcake.ide.core.exception.PerfCakeResourceException;
import org.perfcake.ide.core.manager.ScenarioManager;
import org.perfcake.ide.core.manager.ScenarioManagers;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.editor.ServiceManager;
import org.perfcake.ide.editor.controller.ExecutionFactory;
import org.perfcake.ide.editor.controller.RootController;
import org.perfcake.ide.editor.swing.editor.Pc4ideEditor;
import org.perfcake.ide.intellij.IntellijUtils;
import org.perfcake.ide.intellij.VirtualFileConverter;


/**
 * Represents a PerfCake scenario editor for IntelliJ Idea.
 */
public class ScenarioEditor implements FileEditor {
    public static final String EDITOR_NAME = "PerfCake designer";
    static final Logger logger = Logger.getInstance(ScenarioEditor.class);

    private Project project;
    private VirtualFile file;
    private long modificationStamp = 0L;
    // private ScenarioDocumentAdapter documentListener;
    private Module module;
    // private ScenarioManager manager;
    private boolean updateInProcess;

    private Pc4ideEditor pc4ideEditor;
    // private final ScenarioModelWrapper modelWrapper;
    private ScenarioModel model = null;
    private Document document;
    private ScenarioDocumentListener documentListener = null;

    /**
     * Creates new scenario editor.
     *
     * @param project project to which scenario belongs
     * @param file    file with the scenario
     */
    public ScenarioEditor(@NotNull Project project, @NotNull VirtualFile file) {
        this.project = project;
        this.file = file instanceof LightVirtualFile ? ((LightVirtualFile) file).getOriginalFile() : file;
        module = ModuleUtil.findModuleForFile(this.file, project);

        ScenarioManager manager = null;
        try {
            manager = ScenarioManagers.createXmlManager(VirtualFileConverter.convertPath(file));
            ExecutionFactory executionFactory = new IntellijExecutionFactory();

            ServiceManager serviceManager = ApplicationManager.getApplication().getComponent(ServiceManager.class);

            pc4ideEditor = new Pc4ideEditor(manager, executionFactory, serviceManager, new ReflectionComponentCatalogue());
            documentListener = new ScenarioDocumentListener(getPc4ideEditor());
            document = FileDocumentManager.getInstance().getDocument(file);
            if (document != null) {
                Notification notification = IntellijUtils.createNotification("Cannot locate file", NotificationType.WARNING)
                        .setContent("File won't be updated on external changes");
                document.addDocumentListener(documentListener);
            }

        } catch (PerfCakeException | PerfCakeResourceException e) {
            Notification notification = new Notification(IntellijUtils.PERFCAKE_NOTIFICATION_ID, "Error",
                    "Cannot create scenario", NotificationType.ERROR);
            Notifications.Bus.notify(notification);
        }
    }

    @Override
    public void dispose() {
        // /*TODO threadIntrpted exc.(dispose in porgress?)*/
        // /*TODO filewatcher?*/

        deselectNotify();

        EditorHistoryManager.getInstance(project).updateHistoryEntry(file, false);

        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document != null) {
            document.removeDocumentListener(documentListener);
        }
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return pc4ideEditor;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        // return pc4ideEditor.getPreferredFocusedComponent();
        return pc4ideEditor.getGraphicalEditorPanel();
    }

    @NotNull
    @Override
    public String getName() {
        return EDITOR_NAME;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void selectNotify() {
        documentListener.setEnabled(true);
        // redraw form to re-size properly
        pc4ideEditor.getFormManager().getCurrentPageController().drawForm();
    }

    @Override
    public void deselectNotify() {
        saveContent();

        documentListener.setEnabled(false);
    }

    private void saveContent() {
        try {
            pc4ideEditor.save();
            if (document != null && modificationStamp != document.getModificationStamp()) {
                // reloads document from disk --> causes update for other editors (e.g. XML editors)
                FileDocumentManager.getInstance().reloadFromDisk(document);
            }
        } catch (Pc4ideException e) {
            logger.error("Cannot save scenacrio", e);
            Notification notification = IntellijUtils.createNotification("Cannot save editor", NotificationType.ERROR)
                    .setContent(String.format("Caused by %s. See log for more details.", e.getClass().getName()));

            Notifications.Bus.notify(notification);
        }
    }

    @Override
    public void setState(@NotNull FileEditorState state) {
        // not used
    }

    @NotNull
    @Override
    public FileEditorState getState(@NotNull FileEditorStateLevel level) {
        return new FileEditorState() {
            @Override
            public boolean canBeMergedWith(FileEditorState fileEditorState, FileEditorStateLevel fileEditorStateLevel) {
                return true;
            }
        };
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
        // not used
    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
        // not used
    }

    @Nullable
    @Override
    public BackgroundEditorHighlighter getBackgroundHighlighter() {
        // not used
        return null;
    }

    @Nullable
    @Override
    public FileEditorLocation getCurrentLocation() {
        // not used
        return null;
    }

    @Nullable
    @Override
    public StructureViewBuilder getStructureViewBuilder() {
        // not used
        return null;
    }

    @Nullable
    @Override
    public <T> T getUserData(@NotNull Key<T> tKey) {
        // not used
        return null;
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> tKey, @Nullable T t) {
        // not used
    }

    /**
     * @return File which is edited by this editor.
     */
    public VirtualFile getFile() {
        return file;
    }

    /**
     * @return Scenario controller or null, if no controller has been loaded yet.
     */
    public RootController getScenarioController() {
        return pc4ideEditor.getGraphicalEditorPanel().getController();
    }

    public Pc4ideEditor getPc4ideEditor() {
        return pc4ideEditor;
    }

}

