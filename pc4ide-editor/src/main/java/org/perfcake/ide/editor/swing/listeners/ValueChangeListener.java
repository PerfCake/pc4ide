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
import org.perfcake.ide.core.command.SimplePropertyCommand;
import org.perfcake.ide.core.command.invoker.CommandInvoker;
import org.perfcake.ide.core.model.properties.Value;

/**
 * Listener for text field change.
 *
 * @author Jakub Knetl
 */
public class ValueChangeListener implements ActionListener, DocumentListener {

    private JTextComponent textComponent;
    private CommandInvoker invoker;
    private Value value;

    /**
     * creates new propertyInfo change listener.
     *
     * @param textComponent text component
     * @param invoker       invoker
     * @param value         property
     */
    public ValueChangeListener(JTextComponent textComponent, CommandInvoker invoker, Value value) {
        this.textComponent = textComponent;
        this.invoker = invoker;
        this.value = value;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        fireCommand(textComponent.getText());
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
        fireCommand(textComponent.getText());
    }

    protected void fireCommand(String text) {
        Command command = new SimplePropertyCommand(value, text);
        invoker.executeCommand(command);
    }
}
