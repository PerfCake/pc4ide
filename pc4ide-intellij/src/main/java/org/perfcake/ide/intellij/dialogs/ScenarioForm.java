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

package org.perfcake.ide.intellij.dialogs;

import com.intellij.openapi.ui.ComboBox;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Conetn panel of the new Scenario Form.
 *
 * @author Jakub Knetl
 */
public class ScenarioForm extends JPanel {

    private JTextField nameTextField;
    private JComboBox typeComboBox;
    private JLabel nameLabel;
    private JLabel typeLabel;

    public ScenarioForm() {
        createUiComponents();
    }

    protected void createUiComponents() {
        setLayout(new GridBagLayout());

        nameLabel = new JLabel("Scenario name:");
        nameTextField = new JTextField(20);
        typeComboBox = new ComboBox(new String[] {"xml", "dsl"});
        typeLabel = new JLabel("Type:");

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(2, 10, 2, 10);
        c.fill = GridBagConstraints.NONE;

        add(nameLabel, c);
        c.gridy++;
        add(typeLabel, c);

        c.gridy = 0;
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(nameTextField, c);
        c.gridy++;
        add(typeComboBox, c);
    }

    public JTextField getNameTextField() {
        return nameTextField;
    }

    public JComboBox getTypeComboBox() {
        return typeComboBox;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public JLabel getTypeLabel() {
        return typeLabel;
    }

    public String getScenarioName() {
        return nameTextField.getText();
    }

    public String getScenarioFileName() {
        return String.format("%s.%s", getScenarioName(), getType());
    }

    public String getType() {
        return typeComboBox.getSelectedItem().toString();
    }
}
