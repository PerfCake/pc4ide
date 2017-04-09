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

package org.perfcake.ide.intellij.settings;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.nio.file.Paths;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.perfcake.ide.core.exec.PerfCakeInstallationValidator;
import org.perfcake.ide.editor.ServiceManager;

/**
 * Forms with PerfCake settings.
 *
 * @author Jakub Knetl
 */
public class PerfCakeSettingsForm extends JPanel {

    public static final String INVALID_PATH_MESSAGE = "Entered path is not valid. Please enter correct path.";
    private TextFieldWithBrowseButton installationField;
    private JLabel installationLabel;
    private JLabel wrongLocationLabel;

    /**
     * Creates new PerfCake settings form.
     */
    public PerfCakeSettingsForm() {
        installationLabel = new JLabel("PerfCake location:");
        installationField = new TextFieldWithBrowseButton();
        FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(false, true, false,
                false, false, false);
        TextBrowseFolderListener browseFolderListener = new TextBrowseFolderListener(fileChooserDescriptor);
        installationField.addBrowseFolderListener(browseFolderListener);
        wrongLocationLabel = new JLabel();
        wrongLocationLabel.setForeground(Color.red);
        String storedInstallation = PropertiesComponent.getInstance().getValue(Pc4ideSettings.INSTALLATION_DIR_KEY, "");
        PerfCakeInstallationValidator validator = ApplicationManager.getApplication()
                .getComponent(ServiceManager.class).getInstallationValidator();

        if (storedInstallation != null && !validator.isValid(Paths.get(storedInstallation))) {
            wrongLocationLabel.setText(INVALID_PATH_MESSAGE);
        }

        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(2, 10, 2, 10);
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.NONE;

        add(installationLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        c.gridx = 1;

        add(installationField, c);


        installationField.getTextField().getDocument().addDocumentListener(new DocumentListener() {
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
                if (validator.isValid(Paths.get(getInstallationFolder()))) {
                    wrongLocationLabel.setText("");
                } else {
                    wrongLocationLabel.setText(INVALID_PATH_MESSAGE);
                }
            }
        });


        c.gridy = 2;
        c.weightx = 0;
        c.weighty = 0;
        add(wrongLocationLabel, c);

        c.weighty = 1;
        c.gridy = 3;
        add(new JPanel(), c);
    }

    public String getInstallationFolder() {
        return installationField.getText();
    }

    public TextFieldWithBrowseButton getInstallationField() {
        return installationField;
    }

    public JLabel getInstallationLabel() {
        return installationLabel;
    }
}
