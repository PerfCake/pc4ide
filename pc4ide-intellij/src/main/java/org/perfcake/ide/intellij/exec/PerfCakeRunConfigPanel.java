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

package org.perfcake.ide.intellij.exec;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.table.JBTable;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import org.jetbrains.annotations.NotNull;
import org.perfcake.ide.core.exec.SystemProperty;

/**
 * Represents PerfCake run configuration panel.
 *
 * @author Jakub Knetl
 */
public class PerfCakeRunConfigPanel extends JPanel {

    private boolean isDebugConfigByDefault = false;
    private Project project;

    private JLabel scenarioPathLabel;
    private JLabel javaHomeLabel;
    private JLabel perfcakeHomeLabel;
    private JLabel messageDirLabel;
    private JLabel pluginDirLabel;
    private JLabel debugLabel;
    private JLabel debugNameLabel;
    private JLabel systemPropertiesLabel;

    private TextFieldWithBrowseButton scenarioPathField;
    private TextFieldWithBrowseButton javaHomeField;
    private TextFieldWithBrowseButton perfcakeHomeField;
    private TextFieldWithBrowseButton messageDirField;
    private TextFieldWithBrowseButton pluginDirField;
    private JBTable systemPropertiesField;
    private JCheckBox debugCheckBox;
    private JTextField debugNameField;
    private DefaultTableModel systemPropertiesTableModel;

    public PerfCakeRunConfigPanel(Project project, boolean isDebugConfigByDefault) {
        this.isDebugConfigByDefault = isDebugConfigByDefault;
        this.project = project;
        createUiComponents();
    }

    private void createUiComponents() {

        scenarioPathLabel = new JLabel("Scenario path:");
        javaHomeLabel = new JLabel("Java home:");
        perfcakeHomeLabel = new JLabel("PerfCake home:");
        messageDirLabel = new JLabel("Messages dir:");
        pluginDirLabel = new JLabel("Plugins dir:");
        systemPropertiesLabel = new JLabel("System properties:");


        debugLabel = new JLabel("Start debug agent:");
        debugNameLabel = new JLabel("Debug JMX agent name:");

        List<JComponent> leftColumnComponents = new ArrayList<>(
                Arrays.asList(scenarioPathLabel, javaHomeLabel, perfcakeHomeLabel, messageDirLabel, pluginDirLabel, systemPropertiesLabel,
                        debugLabel, debugNameLabel));


        FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(true, false, false,
                false, false, false);
        FileChooserDescriptor dirChooserDescriptor = new FileChooserDescriptor(false, true, false,
                false, false, false);

        scenarioPathField = new TextFieldWithBrowseButton();
        scenarioPathField.addBrowseFolderListener(new TextBrowseFolderListener(fileChooserDescriptor));

        Sdk sdk = ProjectRootManager.getInstance(project).getProjectSdk();
        javaHomeField = new TextFieldWithBrowseButton();
        if (sdk != null) {
            javaHomeField.setText(sdk.getHomePath());
        }
        javaHomeField.addBrowseFolderListener(new TextBrowseFolderListener(dirChooserDescriptor));

        perfcakeHomeField = new TextFieldWithBrowseButton();
        perfcakeHomeField.addBrowseFolderListener(new TextBrowseFolderListener(dirChooserDescriptor));

        messageDirField = new TextFieldWithBrowseButton();
        messageDirField.addBrowseFolderListener(new TextBrowseFolderListener(dirChooserDescriptor));

        pluginDirField = new TextFieldWithBrowseButton();
        pluginDirField.addBrowseFolderListener(new TextBrowseFolderListener(dirChooserDescriptor));

        JPanel propertiesPanel = createPropertiesPanel();


        debugCheckBox = new JCheckBox();
        debugNameField = new JTextField("perfcake-1");
        List<JComponent> rightColumnComponents = new ArrayList<>(
                Arrays.asList(scenarioPathField, javaHomeField, perfcakeHomeField, messageDirField, pluginDirField, propertiesPanel,
                        debugCheckBox, debugNameField));
        if (isDebugConfigByDefault) {
            debugCheckBox.setSelected(true);
        } else {
            debugCheckBox.setSelected(false);
            debugNameField.setEnabled(false);
        }

        debugCheckBox.addActionListener(e -> {
            debugNameField.setEnabled(debugCheckBox.isSelected());
        });

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.2;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(2, 5, 2, 5);

        JLabel header = new JLabel("<isHtmlUsed><h3>PerfCake run configuration</h3></isHtmlUsed>");

        add(header, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridy = 1;

        for (JComponent component : leftColumnComponents) {
            this.add(component, c);
            c.gridy++;
        }

        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.8;

        c.fill = GridBagConstraints.HORIZONTAL;

        for (JComponent component : rightColumnComponents) {
            this.add(component, c);
            c.gridy++;
        }
    }

    @NotNull
    private JPanel createPropertiesPanel() {
        JPanel propertiesPanel = new JPanel(new GridBagLayout());

        systemPropertiesTableModel = new DefaultTableModel();
        systemPropertiesTableModel.addColumn("Property name");
        systemPropertiesTableModel.addColumn("Property property");

        systemPropertiesField = new JBTable(systemPropertiesTableModel);
        systemPropertiesField.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        systemPropertiesField.setColumnSelectionAllowed(false);
        systemPropertiesField.setRowSelectionAllowed(true);
        systemPropertiesField.setCellSelectionEnabled(false);
        systemPropertiesField.setFillsViewportHeight(true);


        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            systemPropertiesTableModel.addRow(new String[] {"", ""});
        });

        JButton removeButton = new JButton("DeleteIcon");
        removeButton.addActionListener(e -> {
            int[] rows = systemPropertiesField.getSelectedRows();

            int selectedRow;
            while ((selectedRow = systemPropertiesField.getSelectedRow()) != -1) {
                systemPropertiesTableModel.removeRow(selectedRow);
            }
        });

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.9;
        c.gridheight = 2;
        c.insets = new Insets(2, 5, 2, 5);
        c.fill = GridBagConstraints.BOTH;
        propertiesPanel.add(systemPropertiesField, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.1;
        c.gridheight = 1;

        propertiesPanel.add(addButton, c);
        c.gridy++;
        propertiesPanel.add(removeButton, c);

        return propertiesPanel;
    }

    /* ******************************* Getters and setters for field content **************************************/

    /**
     * @return Scenario path.
     */
    public String getScenarioPath() {
        return scenarioPathField.getText();
    }

    /**
     * Sets scenario path.
     *
     * @param path path to scenario
     */
    public void setScenarioPath(String path) {
        scenarioPathField.setText(path);
    }

    public String getJavaHome() {
        return javaHomeField.getText();
    }

    public void setJavaHome(String javaHome) {
        javaHomeField.setText(javaHome);
    }

    public String getPerfcakeHome() {
        return perfcakeHomeField.getText();
    }

    public void setPerfcakeHomeField(String perfcakeHome) {
        this.perfcakeHomeField.setText(perfcakeHome);
    }

    public String getMessageDir() {
        return messageDirField.getText();
    }

    public void setMessageDir(String messageDir) {
        messageDirField.setText(messageDir);
    }

    public String getPluginDir() {
        return pluginDirField.getText();
    }

    public void setPluginDir(String pluginDir) {
        pluginDirField.setText(pluginDir);
    }

    /**
     * @return List of system properties.
     */
    public List<SystemProperty> getSystemProperties() {
        List<SystemProperty> properties = new ArrayList<>();

        for (int i = 0; i < systemPropertiesTableModel.getRowCount(); i++) {
            properties.add(
                    new SystemProperty(String.valueOf(systemPropertiesTableModel.getValueAt(i, 0)),
                            String.valueOf(systemPropertiesTableModel.getValueAt(i, 1))));

        }

        return properties;
    }

    /**
     * Sets system properties to the table.
     *
     * @param systemProperties system properties to be set.
     */
    public void setSystemProperties(List<SystemProperty> systemProperties) {
        for (SystemProperty property : systemProperties) {
            systemPropertiesTableModel.addRow(new String[] {property.getKey(), property.getValue()});
        }
    }

    public boolean isDebugMode() {
        return debugCheckBox.isSelected();
    }

    /**
     * Determines whether this configuration represent debug configuration.
     *
     * @param isDebugConfiguration is debug configuration?
     */
    public void setDebugMode(boolean isDebugConfiguration) {
        if (isDebugConfiguration != debugCheckBox.isEnabled()) {
            debugCheckBox.setEnabled(isDebugConfiguration);
            this.removeAll();
            this.revalidate();
            this.repaint();
            createUiComponents();
        }
        this.isDebugConfigByDefault = isDebugConfiguration;
    }

    /**
     * @return Debug agent name.
     */
    public String getDebugAgentName() {
        return debugNameField.getText();
    }

    /**
     * Sets debug agent name.
     *
     * @param debugAgentName name of the agent
     */
    public void setDebugAgentName(String debugAgentName) {
        debugNameField.setText(debugAgentName);
    }

    /* ******************************* GETTERS FOR UI FIELDS **************************************/

    public TextFieldWithBrowseButton getScenarioPathField() {
        return scenarioPathField;
    }

    public TextFieldWithBrowseButton getJavaHomeField() {
        return javaHomeField;
    }

    public TextFieldWithBrowseButton getPerfcakeHomeField() {
        return perfcakeHomeField;
    }

    public TextFieldWithBrowseButton getMessageDirField() {
        return messageDirField;
    }

    public TextFieldWithBrowseButton getPluginDirField() {
        return pluginDirField;
    }

    public JTextField getDebugNameField() {
        return debugNameField;
    }

    public JTable getSystemPropertiesField() {
        return systemPropertiesField;
    }
}
