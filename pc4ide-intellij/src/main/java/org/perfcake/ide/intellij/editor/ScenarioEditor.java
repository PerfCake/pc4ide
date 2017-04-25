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
import java.util.Objects;
import javax.swing.JComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.perfcake.PerfCakeException;
import org.perfcake.ide.core.components.ReflectionComponentCatalogue;
import org.perfcake.ide.core.exception.PerfCakeResourceException;
import org.perfcake.ide.core.manager.ScenarioManager;
import org.perfcake.ide.core.manager.ScenarioManagers;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.editor.ServiceManager;
import org.perfcake.ide.editor.controller.ExecutionFactory;
import org.perfcake.ide.editor.controller.RootController;
import org.perfcake.ide.editor.swing.editor.Pc4ideEditor;
import org.perfcake.ide.intellij.IntellijCommandInvoker;
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

    // revision (timestamp) of the currently drawed document
    private long currentDocumentRevision = 0L;

    // private ScenarioDocumentAdapter documentListener;
    private Module module;
    // private ScenarioManager manager;
    private boolean updateInProcess;

    private Pc4ideEditor pc4ideEditor;
    // private final ScenarioModelWrapper modelWrapper;
    private ScenarioModel model = null;
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
            manager = ScenarioManagers.createScenarioManager(VirtualFileConverter.convertPath(file));
            ExecutionFactory executionFactory = new IntellijExecutionFactory();

            ServiceManager serviceManager = ApplicationManager.getApplication().getComponent(ServiceManager.class);

            Document document = FileDocumentManager.getInstance().getDocument(file);
            IntellijCommandInvoker commandInvoker = new IntellijCommandInvoker(project, this);
            pc4ideEditor = new Pc4ideEditor(manager, executionFactory, serviceManager, commandInvoker, new ReflectionComponentCatalogue());
            documentListener = new ScenarioDocumentListener(this);
            if (document != null) {
                currentDocumentRevision = document.getModificationStamp();
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
        return pc4ideEditor.getContentPanel();
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
        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document != null && document.getModificationStamp() != currentDocumentRevision) {
            // document has changed during editor inactivity, update needed
            IntellijUtils.updateEditorContent(this, document);
            currentDocumentRevision = document.getModificationStamp();
        } else {
            // form manager needs to be redrawn even if document didn't changed in order to resize itself properly
            pc4ideEditor.getFormManager().getCurrentPageController().drawForm();
        }
    }

    @Override
    public void deselectNotify() {
        saveContent();

        documentListener.setEnabled(false);
    }

    private void saveContent() {
        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document != null) {
            ApplicationManager.getApplication().runWriteAction(() -> {
                ApplicationManager.getApplication().invokeLater(() -> {
                    FileDocumentManager.getInstance().saveDocument(document);
                    currentDocumentRevision = document.getModificationStamp();
                });
            });
        }
    }

    @Override
    public void setState(@NotNull FileEditorState state) {
        // do nothing
    }

    @NotNull
    @Override
    public FileEditorState getState(@NotNull FileEditorStateLevel level) {
        return new ScenarioEditorState(currentDocumentRevision);
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

    public long getCurrentDocumentRevision() {
        return currentDocumentRevision;
    }

    /**
     * Updates stored document revision to latest version. This method should be called if some external class makes changes to
     * the document and updates editor view in order to keep track which version of the document was drawn.
     */
    public void updateDocumentRevision() {
        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document == null) {
            logger.warn("Cannot update document revision. Document is null");
        }
        currentDocumentRevision = document.getModificationStamp();
    }

    private static class ScenarioEditorState implements FileEditorState {

        long documentRevision;

        public ScenarioEditorState(long documentRevision) {
            this.documentRevision = documentRevision;
        }

        public long getDocumentRevision() {
            return documentRevision;
        }

        @Override
        public boolean canBeMergedWith(FileEditorState fileEditorState, FileEditorStateLevel fileEditorStateLevel) {
            return true;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ScenarioEditorState that = (ScenarioEditorState) o;
            return documentRevision == that.documentRevision;
        }

        @Override
        public int hashCode() {
            return Objects.hash(documentRevision);
        }
    }
}

