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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.event.DocumentAdapter;
import com.intellij.openapi.editor.event.DocumentEvent;
import org.perfcake.ide.core.command.invoker.CommandInvoker;
import org.perfcake.ide.intellij.IntellijCommandInvoker;
import org.perfcake.ide.intellij.IntellijUtils;

/**
 * Manages updating scenario editor content on document change.
 *
 * @author Jakub Knetl
 */
class ScenarioDocumentListener extends DocumentAdapter {

    static final Logger logger = Logger.getInstance(ScenarioDocumentListener.class);

    private boolean enabled;
    private ScenarioEditor editor;

    public ScenarioDocumentListener(ScenarioEditor editor) {
        this.editor = editor;
        enabled = true;
    }

    @Override
    public void documentChanged(DocumentEvent e) {
        if (!enabled) {
            // ignore event if listener is not enabled
            return;
        }

        //ignore events which were caused by Intellij command invoker, since changes has been already drawn into editor
        CommandInvoker  commandInvoker = editor.getPc4ideEditor().getCommandInvoker();
        if (commandInvoker instanceof IntellijCommandInvoker && ((IntellijCommandInvoker) commandInvoker).isUpdateInProgress()) {
            return;
        }

        /**
         * TODO:
         * We should also catch events, which were caused by intellij idea undo action on scenario editor, because
         * it is not desired to reload whole model (and thus loose form context), when undo is done. We should rather use
         * Intellij implementation of command invoker in order to undo the event.
         *
         * If this won't be easy, then alternative approach migth be to somehow store form manager context and restore it on
         * the update
         */

        IntellijUtils.updateEditorContent(editor, e.getDocument());
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ScenarioDocumentListener setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}
