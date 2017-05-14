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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
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
public class EnabledSwitchListener extends MouseAdapter implements ActionListener {
    private final JComponent field;
    private final PropertyInfo info;
    private final JLabel switchLabel;
    private final String deleteText = "Delete the value";
    private final String editText = "Change the value";
    private final FormController controller;
    private ValueAgent valueAgent;
    private boolean isNull;
    private String previousValue;
    private ValueChangeListener listener;
    private Icon deleteIcon;
    private Icon editIcon;

    /**
     * Creates new enabled switch listener.
     *
     * @param value      propertyInfo represented by controlled field
     * @param controller form controller which contains field and switchLabel
     * @param field      controlled field
     * @param info       property info
     * @param switchLabel     switchLabel which controls the field.
     * @param deleteIcon icon to delete a value
     * @param editIcon   icon to edit a value
     */
    public EnabledSwitchListener(Value value, FormController controller, JComponent field, PropertyInfo info, JLabel switchLabel,
                                 Icon deleteIcon, Icon editIcon) {
        if (controller == null) {
            throw new IllegalArgumentException("cotnroller cannot be null");
        }
        if (field == null) {
            throw new IllegalArgumentException("field cannot be null");
        }
        if (info == null) {
            throw new IllegalArgumentException("info cannot be null");
        }
        if (switchLabel == null) {
            throw new IllegalArgumentException("switchLabel cannot be null");
        }
        if (deleteIcon == null) {
            throw new IllegalArgumentException("deleteIcon cannot be null");
        }
        if (editIcon == null) {
            throw new IllegalArgumentException("editIcon cannot be null");
        }
        this.field = field;
        this.info = info;
        this.switchLabel = switchLabel;
        this.controller = controller;
        this.valueAgent = ValueAgents.createAgent(field);
        this.deleteIcon = deleteIcon;
        this.editIcon = editIcon;
        isNull = value == null;
        previousValue = getDefaultValue();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switchComponentState();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switchComponentState();
    }

    private void switchComponentState() {
        Model model = info.getModel();
        if (isNull) {
            valueAgent.setValue(previousValue);
            switchLabel.setIcon(deleteIcon);
            switchLabel.setToolTipText(deleteText);
            if (info.getMinOccurs() > 0) {
                switchLabel.setEnabled(false);
                switchLabel.setVisible(false);
            }
            field.setEnabled(true);
            isNull = false;

            // execute command changing parent model by adding the propertyInfo property
            if (model != null) {
                Value value = new SimpleValue(valueAgent.getValue());
                Command command = new AddPropertyCommand(model, value, info);
                controller.getFormManager().getCommandInvoker().executeCommand(command);

                // add listener to switchLabel which will control the propertyInfo
                listener = ValueChangeListener.createValueListener(value, controller.getFormManager().getCommandInvoker(), field);
                listener.subscribeAll();
            }
            field.grabFocus();
        } else {
            previousValue = valueAgent.getValue();
            switchLabel.setEnabled(true);
            switchLabel.setVisible(true);
            switchLabel.setIcon(editIcon);
            switchLabel.setToolTipText(editText);
            field.setEnabled(false);
            valueAgent.setValue(getDefaultValue());
            field.repaint();
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

    public String getDeleteText() {
        return deleteText;
    }

    public String getEditText() {
        return editText;
    }
}
