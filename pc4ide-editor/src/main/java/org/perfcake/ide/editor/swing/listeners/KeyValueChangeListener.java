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

package org.perfcake.ide.editor.swing.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.perfcake.ide.core.command.Command;
import org.perfcake.ide.core.command.KeyValueCommand;
import org.perfcake.ide.core.command.invoker.CommandInvoker;
import org.perfcake.ide.core.model.properties.KeyValue;

/**
 * Listener for text field change.
 *
 * @author Jakub Knetl
 */
public class KeyValueChangeListener implements ActionListener, DocumentListener {

    private JTextComponent keyComponent;
    private JTextComponent valueComopnent;
    private CommandInvoker invoker;
    private KeyValue keyValue;

    /**
     * creates new keyValue change listener.
     *
     * @param keyComponent   text component
     * @param valueComopnent text component
     * @param invoker        invoker
     * @param keyValue       property
     */

    public KeyValueChangeListener(JTextComponent keyComponent, JTextComponent valueComopnent, CommandInvoker invoker, KeyValue keyValue) {
        this.keyComponent = keyComponent;
        this.valueComopnent = valueComopnent;
        this.invoker = invoker;
        this.keyValue = keyValue;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        fireCommand(keyComponent.getText(), valueComopnent.getText());
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        changedUpdate(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        changedUpdate(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        fireCommand(keyComponent.getText(), valueComopnent.getText());
    }

    protected void fireCommand(String key, String value) {
        Command command = new KeyValueCommand(keyValue, key, value);
        invoker.executeCommand(command);
    }
}
