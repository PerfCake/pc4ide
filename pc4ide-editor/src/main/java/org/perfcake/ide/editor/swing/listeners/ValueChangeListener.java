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
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import org.perfcake.ide.core.command.Command;
import org.perfcake.ide.core.command.KeyChangeKeyValueCommand;
import org.perfcake.ide.core.command.SimplePropertyCommand;
import org.perfcake.ide.core.command.ValueChangeKeyValueCommand;
import org.perfcake.ide.core.command.invoker.CommandInvoker;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.properties.KeyValue;
import org.perfcake.ide.core.model.properties.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Swing listener for simple property (including Value and KeyValue) change.
 *
 * @author Jakub Knetl
 */
public abstract class ValueChangeListener implements ActionListener {

    static final Logger logger = LoggerFactory.getLogger(ValueChangeListener.class);

    protected final JComponent jComponent;
    protected ValueAgent valueAgent;
    protected CommandInvoker invoker;
    protected Property property;
    private KeyValueField keyValueField;

    public enum KeyValueField {
        KEY, VALUE;
    }

    /**
     * Creates new property change listener.
     *
     * @param property   property
     * @param invoker    command invoker
     * @param jComponent Swing component
     */
    protected ValueChangeListener(Property property, KeyValueField keyValueField, CommandInvoker invoker, JComponent jComponent) {
        this.property = property;
        this.invoker = invoker;
        this.jComponent = jComponent;
        this.keyValueField = keyValueField;
        this.valueAgent = ValueAgents.createAgent(jComponent);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fireCommand(valueAgent.getValue());
    }

    /**
     * Subscribes itself to listen all supported event from a component.
     */
    public abstract void subscribeAll();

    /**
     * Unsubscribes itself from listening all events from a component.
     */
    public abstract void unsubscribeAll();

    protected void fireCommand(String text) {
        Command command = null;
        if (property instanceof Value) {
            command = new SimplePropertyCommand((Value) property, text);
        }
        if (property instanceof KeyValue) {
            switch (keyValueField) {
                case KEY:
                    command = new KeyChangeKeyValueCommand((KeyValue) property, text);
                    break;
                case VALUE:
                    command = new ValueChangeKeyValueCommand((KeyValue) property, text);
                    break;
                default:
                    logger.warn("Unknown field");
            }
        }

        if (command == null) {
            logger.warn("Uknown command type");
        } else {
            invoker.executeCommand(command);
        }
    }

    public JComponent getjComponent() {
        return jComponent;
    }

    public ValueAgent getValueAgent() {
        return valueAgent;
    }

    /**
     * Creates ValueChangeListener for set of supported jComponents.
     *
     * @param value      property
     * @param invoker    command invoker
     * @param jComponent swing component
     * @return property change listener
     * @throws UnsupportedOperationException if jComponent is not supported.
     */
    public static ValueChangeListener createValueListener(Value value, CommandInvoker invoker, JComponent jComponent)
            throws UnsupportedOperationException {
        KeyValueField keyValueField = null;

        return createListenerType(value, invoker, jComponent, keyValueField);

    }

    /**
     * Creates ValueChangeListener for set of supported jComponents.
     *
     * @param keyValue      property
     * @param keyValueField either KEY or VALUE.
     * @param invoker       command invoker
     * @param jComponent    swing component
     * @return property change listener
     * @throws UnsupportedOperationException if jComponent is not supported.
     */
    public static ValueChangeListener createKeyValueListener(KeyValue keyValue, KeyValueField keyValueField,
                                                             CommandInvoker invoker, JComponent jComponent)
            throws UnsupportedOperationException {

        return createListenerType(keyValue, invoker, jComponent, keyValueField);
    }

    protected static ValueChangeListener createListenerType(Property value, CommandInvoker invoker,
                                                            JComponent jComponent, KeyValueField keyValueField) {
        ValueChangeListener listener = null;
        if (jComponent == null) {
            throw new IllegalArgumentException("jComponent cannot be null");
        }
        if (jComponent instanceof JTextField) {
            listener = new TextValueChangeListener((JTextComponent) jComponent, keyValueField, invoker, value);
        }
        if (jComponent instanceof JComboBox) {
            listener = new ComboValueChangeListener(value, keyValueField, invoker, (JComboBox<String>) jComponent);
        }
        if (listener == null) {
            throw new UnsupportedOperationException(String.format("Cannot create ValueChangeListener for %s: unknown type",
                    jComponent.getClass().getSimpleName()));
        }
        return listener;
    }
}
