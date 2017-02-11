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
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.perfcake.PerfCakeException;
import org.perfcake.ide.core.model.ModelLoader;
import org.perfcake.ide.core.model.ScenarioModel;
import org.perfcake.ide.editor.swing.EditorJPanel;





/**
 * Represents a PerfCake scenario editor for IntelliJ Idea.
 */
public class ScenarioEditor implements FileEditor {
    private static final Logger LOG = Logger.getInstance(ScenarioEditor.class);
    public static final String PERFCAKE_NOTIFICATION_ID = "PerfCake Plugin";

    private Project project;
    private VirtualFile file;
    private long modificationStamp = 0L;
    // private ScenarioDocumentAdapter documentAdapter;
    private Module module;
    // private ScenarioManager manager;
    private boolean updateInProcess;

    private final EditorJPanel editorGui;
    // private final ScenarioModelWrapper modelWrapper;
    private ScenarioModel model = null;

    /**
     * Creates new scenario editor.
     *
     * @param project project to which scenario belongs
     * @param file file with the scenario
     */
    public ScenarioEditor(@NotNull Project project, @NotNull VirtualFile file) {
        this.project = project;
        this.file = file instanceof LightVirtualFile ? ((LightVirtualFile) file).getOriginalFile() : file;
        module = ModuleUtil.findModuleForFile(this.file, project);
        /*
          if (module != null) {
            NotificationsConfiguration.getNotificationsConfiguration()
                    .register(PERFCAKE_NOTIFICATION_ID, NotificationDisplayType.BALLOON);
            if (!PerfCakeModuleUtil.isPerfCakeModule(module)) {
                String[] logMsg = Messages.Log.UNSUPPORTED_MODULE;
                LOG.info(logMsg[0] + file.getName() + logMsg[1]);
                Notifications.Bus.notify(new Notification(PERFCAKE_NOTIFICATION_ID,
                        Messages.Title.UNSUPPORTED_MODULE,
                        Messages.Dialog.UNSUPPORTED_MODULE,
                        NotificationType.INFORMATION), project);
            }
        } else {
            String[] eMsg = Messages.Exception.NULL_MODULE;
            throw new IllegalArgumentException(eMsg[0] + file + eMsg[1] + project);
        }

        documentAdapter = new ScenarioDocumentAdapter(this);
        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document != null) {
            document.addDocumentListener(documentAdapter);
        }
        updateInProcess = false;

        manager = PerfCakeScenarioUtil.getScenarioManager(project, file);
        modelWrapper = new ScenarioModelWrapper(this);
        editorGui = new ScenarioEditorGui(modelWrapper.getScenarioGui());
        update();
        */

        final ModelLoader loader = new ModelLoader();
        final File scenarioFile = new File("src/main/resources/scenario/http.xml");
        try {
            // model = loader.loadModel(scenarioFile.toURI().toURL());
            model = loader.loadModel(new URL(file.getUrl()));
        } catch (PerfCakeException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        editorGui = new EditorJPanel(model);
    }

    @Override
    public void dispose() {
        // /*TODO threadIntrpted exc.(dispose in porgress?)*/
        // /*TODO filewatcher?*/

        deselectNotify();

        EditorHistoryManager.getInstance(project).updateHistoryEntry(file, false);

        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document != null) {
            // document.removeDocumentListener(documentAdapter);
        }
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return editorGui;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        // return editorGui.getPreferredFocusedComponent();
        return editorGui;
    }

    @NotNull
    @Override
    public String getName() {
        return "Designer";
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
    /*
        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document != null) {
            if (modificationStamp != document.getModificationStamp()) {
                FileDocumentManager.getInstance().saveDocument(document);
                update();
            }
        }
        modelWrapper.repaintDependencies();
        documentAdapter.enable();*/
    }

    @Override
    public void deselectNotify() {
        /*
        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document != null) {
            if (modificationStamp != document.getModificationStamp()) {
                FileDocumentManager.getInstance().saveDocument(document);
                modificationStamp = document.getModificationStamp();
            }
        }
        documentAdapter.disable();
        */
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
}

