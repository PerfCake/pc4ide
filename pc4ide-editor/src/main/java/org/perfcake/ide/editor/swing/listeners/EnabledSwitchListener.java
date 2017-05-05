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
import javax.swing.JButton;
import javax.swing.JComponent;
import org.perfcake.ide.core.command.AddPropertyCommand;
import org.perfcake.ide.core.command.Command;
import org.perfcake.ide.core.command.RemovePropertyCommand;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.PropertyInfo;
import org.perfcake.ide.core.model.properties.SimpleValue;
import org.perfcake.ide.core.model.properties.Value;
import org.perfcake.ide.editor.form.FormController;

/**
 * Enabled switch listener controls a field and manages if it is enabled or disabled. The field must control a property,
 * which implements Value interface and the model can contain at most 1 property of this type (so propertyInfo.getMaxOccurs() == 1).
 *
 * @author Jakub Knetl
 */
public class EnabledSwitchListener implements ActionListener {
    private final JComponent field;
    private final PropertyInfo info;
    private final JButton button;
    private final String nullifyText = "default";
    private final String enableText = "edit";
    private final FormController controller;
    private ValueAgent valueAgent;
    private boolean isNull;
    private String previousValue;
    private ValueChangeListener listener;

    /**
     * Creates new enabled switch listener.
     *
     * @param value      propertyInfo represented by controlled field
     * @param controller form controller which contains field and button
     * @param field      controlled field
     * @param info       property info
     * @param button     button which controls the field.
     */
    public EnabledSwitchListener(Value value, FormController controller, JComponent field, PropertyInfo info, JButton button) {
        if (controller == null) {
            throw new IllegalArgumentException("cotnroller cannot be null");
        }
        if (field == null) {
            throw new IllegalArgumentException("field cannot be null");
        }
        if (info == null) {
            throw new IllegalArgumentException("info cannot be null");
        }
        if (button == null) {
            throw new IllegalArgumentException("button cannot be null");
        }
        this.field = field;
        this.info = info;
        this.button = button;
        this.controller = controller;
        this.valueAgent = ValueAgents.createAgent(field);
        isNull = value == null;
        previousValue = getDefaultValue();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Model model = info.getModel();
        if (isNull) {
            valueAgent.setValue(previousValue);
            if (info.getMinOccurs() > 0) {
                button.setEnabled(false);
            }
            button.setText(nullifyText);
            field.setEnabled(true);
            isNull = false;
            if (info.getMinOccurs() > 0) {
                button.setEnabled(false);
            } else {
                button.setEnabled(true);
            }

            // execute command changing parent model by adding the propertyInfo property
            if (model != null) {
                Value value = new SimpleValue(valueAgent.getValue());
                Command command = new AddPropertyCommand(model, value, info);
                controller.getFormManager().getCommandInvoker().executeCommand(command);

                // add listener to button which will control the propertyInfo
                listener = ValueChangeListener.createValueListener(value, controller.getFormManager().getCommandInvoker(), field);
                listener.subscribeAll();
            }
        } else {
            previousValue = valueAgent.getValue();
            field.setEnabled(false);
            valueAgent.setValue(getDefaultValue());
            field.repaint();
            button.setText(enableText);
            isNull = true;

            // execute command changing parent model by removing the propertyInfo property
            if (model != null) {
                Value value = model.getSingleProperty(info.getName(), Value.class);
                Command command = new RemovePropertyCommand(model, value, info);
                controller.getFormManager().getCommandInvoker().executeCommand(command);

                if (listener != null) {
                    listener.unsubscribeAll();
                    listener = null;
                }
            }
        }
    }

    private String getDefaultValue() {
        Value value = info.getDefaultValue(Value.class);
        String defaultValue;
        if (value == null) {
            defaultValue = "";
        } else {
            defaultValue = value.getValue();

        }

        return defaultValue;
    }

    public String getNullifyText() {
        return nullifyText;
    }

    public String getEnableText() {
        return enableText;
    }
}
