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
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.command.undo.UndoManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.perfcake.ide.core.command.Command;
import org.perfcake.ide.core.command.invoker.CommandInvoker;
import org.perfcake.ide.core.exception.CommandException;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.exception.ModelSerializationException;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.intellij.editor.ScenarioEditor;

/**
 * Command invoker which forwards commands to Intellij Command manager.
 *
 * @author Jakub Knetl
 */
public class IntellijCommandInvoker implements CommandInvoker {

    static final Logger logger = Logger.getInstance(IntellijCommandInvoker.class);

    private Project project;
    private ScenarioEditor fileEditor;
    private AtomicBoolean updateInProgress;

    public IntellijCommandInvoker(Project project, ScenarioEditor fileEditor) {
        this.project = project;
        this.fileEditor = fileEditor;
        updateInProgress = new AtomicBoolean(false);
    }

    @Override
    public void executeCommand(Command command) {

        Document document = FileDocumentManager.getInstance().getDocument(fileEditor.getFile());
        if (document == null) {
            logger.warn("Cannot locate document for scenario file: " + fileEditor.getFile().toString());
            Notification notification = IntellijUtils.createNotification("Cannot update scenario", NotificationType.WARNING)
                    .setContent("Scenario document cannot be acquired.");
            Notifications.Bus.notify(notification);
            return;
        } else {
            ApplicationManager.getApplication().runWriteAction(() -> {
                CommandProcessor.getInstance().executeCommand(project, () -> {
                    updateInProgress.set(true);
                    try {
                        // execute command which modifies model
                        command.execute();

                        // store updated model into Intellij document
                        ScenarioModel model = (ScenarioModel) fileEditor
                                .getPc4ideEditor().getGraphicalEditorPanel().getController().getModel();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        fileEditor.getPc4ideEditor().getScenarioManager().writeScenario(model, byteArrayOutputStream);
                        document.setText(byteArrayOutputStream.toString("UTF-8"));
                        fileEditor.updateDocumentRevision();
                    } catch (CommandException | ModelSerializationException | ModelConversionException
                            | UnsupportedEncodingException e1) {
                        logger.warn("Cannot execute command", e1);
                    } finally {
                        updateInProgress.set(false);
                    }

                }, IntellijUtils.PLUGIN_ID, null, UndoConfirmationPolicy.DEFAULT, document);
            });
        }

    }

    @Override
    public boolean canUndo() {
        return UndoManager.getInstance(project).isUndoAvailable(fileEditor);
    }

    @Override
    public boolean canRedo() {
        return UndoManager.getInstance(project).isRedoAvailable(fileEditor);
    }

    @Override
    public void undo() {
        UndoManager.getInstance(project).undo(fileEditor);
    }

    @Override
    public void redo() {
        UndoManager.getInstance(project).redo(fileEditor);
    }

    public boolean isUpdateInProgress() {
        return updateInProgress.get();
    }
}
