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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.perfcake.ide.core.command.invoker.CommandInvoker;
import org.perfcake.ide.core.model.Property;

/**
 * Listener for text field change.
 *
 * @author Jakub Knetl
 */
public class TextValueChangeListener extends ValueChangeListener implements DocumentListener {

    protected JTextComponent textComponent;

    /**
     * creates new propertyInfo change listener.
     *
     * @param textComponent text component
     * @param keyValueField either key or property field (applicable only for KeyValue property
     * @param invoker       invoker
     * @param property      property
     */
    protected TextValueChangeListener(JTextComponent textComponent, KeyValueField keyValueField,
                                      CommandInvoker invoker, Property property) {
        super(property, keyValueField, invoker, textComponent);
        this.textComponent = textComponent;
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
        fireCommand(valueAgent.getValue());
    }

    @Override
    public void subscribeAll() {
        textComponent.getDocument().addDocumentListener(this);
    }

    @Override
    public void unsubscribeAll() {
        textComponent.getDocument().removeDocumentListener(this);
    }
}
