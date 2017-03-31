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

package org.perfcake.ide.editor.form.builder;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.perfcake.ide.core.components.ComponentCatalogue;
import org.perfcake.ide.core.components.ComponentLoader;
import org.perfcake.ide.core.components.ComponentLoaderImpl;
import org.perfcake.ide.core.components.PerfCakeComponent;
import org.perfcake.ide.core.docs.DocsService;
import org.perfcake.ide.core.model.AbstractModel;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.PropertyInfo;
import org.perfcake.ide.core.model.components.MessageModel;
import org.perfcake.ide.core.model.properties.KeyValue;
import org.perfcake.ide.core.model.properties.Value;
import org.perfcake.ide.editor.form.FormBuilder;
import org.perfcake.ide.editor.form.FormController;
import org.perfcake.ide.editor.form.impl.ComponentSelctorFormController;
import org.perfcake.ide.editor.swing.DefaultSwingFactory;
import org.perfcake.ide.editor.swing.SwingFactory;
import org.perfcake.ide.editor.swing.icons.control.CogIcon;
import org.perfcake.ide.editor.swing.icons.control.ListIcon;
import org.perfcake.ide.editor.swing.icons.control.MinusIcon;
import org.perfcake.ide.editor.swing.icons.control.PlusIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link org.perfcake.ide.editor.form.FormBuilder}.
 *
 * @author Jakub Knetl
 */
public class FormBuilderImpl implements FormBuilder {

    static final Logger logger = LoggerFactory.getLogger(FormBuilderImpl.class);
    public static final int ICON_WIDTH = 16;
    public static final int ICON_HEIGHT = 16;

    private boolean useDebugBorders = false;
    private SwingFactory swingFactory = new DefaultSwingFactory();

    private Color iconColor = Color.BLACK;
    private Color addIconColor = Color.BLACK;
    private Color removeIconColor = Color.BLACK;

    @Override
    public void buildForm(JPanel panel, Property property, FormController controller) {
        if (panel == null) {
            throw new IllegalArgumentException("Panel cannot be null.");
        }

        if (property == null) {
            throw new IllegalArgumentException("Property cannot be null.");
        }

        panel.setLayout(new GridBagLayout());

        if (useDebugBorders) {
            panel.setBorder(BorderFactory.createTitledBorder("Whole form content"));
        }

        switch (property.getPropertyType()) {
            case MODEL:
                buildModelForm(controller, panel, property.cast(Model.class));
                break;
            case KEY_VALUE:
                logger.warn("Ignoring request to build form for key-value property: {}.", property);
                break;
            case VALUE:
                if (AbstractModel.IMPLEMENTATION_CLASS_PROPERTY.equals(property.getPropertyInfo().getName())) {
                    buildImplementationChooserForm(panel, property.cast(Value.class), controller.getFormManager().getComponentCatalogue());
                } else {
                    logger.warn("Ignoring request to build form for property: {}. Only class property is supported", property);
                }
                break;
            default:
                break;
        }

    }

    private void buildModelForm(FormController controller, JPanel panel, Model model) {

        // pane for values
        JPanel valuesPanel = createValuesPanel();

        List<JPanel> additionalPanels = new ArrayList<>();

        int valuesPanelSize = 1;
        for (PropertyInfo propertyInfo : model.getSupportedProperties()) {
            if (propertyInfo.getMaxOccurs() == 1) {

                switch (propertyInfo.getType()) {
                    // properties with single value
                    case VALUE:
                        Value value = model.getSingleProperty(propertyInfo.getName(), Value.class);
                        if (AbstractModel.IMPLEMENTATION_CLASS_PROPERTY.equals(propertyInfo.getName())) {
                            JPanel implPanel = createDetailedValuePanel(controller, value);
                            additionalPanels.add(0, implPanel); //add always to the beginning
                        } else {
                            addSimpleValueToPanel(valuesPanel, propertyInfo, value, valuesPanelSize);
                            valuesPanelSize++;
                        }
                        break;
                    case KEY_VALUE:

                        KeyValue keyValue = model.getSingleProperty(propertyInfo.getName(), KeyValue.class);
                        //TODO(jknetl): Display plus icon if key-value is null (e.g. Receiver and correlator)
                        if (keyValue != null) {
                            JPanel keyValuePanel = createKeyValuePanel(propertyInfo.getDisplayName(), keyValue);
                            additionalPanels.add(keyValuePanel);
                        }
                        break;

                    case MODEL:
                        Model childModel = model.getSingleProperty(propertyInfo.getName(), Model.class);
                        //TODO(jknetl): Display plus icon if model is null (e.g. Receiver and correlator)
                        if (childModel != null) {
                            JPanel modelPanel = createModelPanel(childModel);
                            additionalPanels.add(modelPanel);
                        }
                        break;
                    default:
                        logger.warn("Unknown property type: {}", propertyInfo.getType());
                        break;
                }


            } else {

                switch (propertyInfo.getType()) {
                    // properties with single value
                    case VALUE:
                        List<Property> values = model.getProperties(propertyInfo);

                        JPanel valueListPanel = createListOfValuesPanel(propertyInfo, values);
                        additionalPanels.add(valueListPanel);
                        break;

                    case KEY_VALUE:

                        List<Property> keyValues = model.getProperties(propertyInfo);

                        JPanel keyValuesPanel = createListOfKeyValuesPanel(propertyInfo, keyValues);
                        additionalPanels.add(keyValuesPanel);
                        break;

                    case MODEL:
                        List<Property> models = model.getProperties(propertyInfo);

                        JPanel modelsPanel = createListOfModelPanel(propertyInfo, models);
                        additionalPanels.add(modelsPanel);
                        break;
                    default:
                        logger.warn("Unknown property type: {}", propertyInfo.getType());
                        break;

                }


                // properties with multiple values

            }

        }

        // add individual panels
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.PAGE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 0;

        for (JPanel p : additionalPanels) {
            panel.add(p, c);
            c.gridy++;
            addSepartor(panel, c);
            c.gridy++;
        }
        panel.add(valuesPanel, c);
        c.gridy++;

        // to start from the top, we need to set weighty of the last component to 1, so that it eats up all the redundant space
        // therefore we will use dummy component
        // see: http://stackoverflow.com/a/24790318
        JPanel dummy = swingFactory.createPanel();
        c.weightx = 1;
        c.weighty = 1;
        panel.add(dummy, c);

    }

    private void buildImplementationChooserForm(JPanel panel, Value value, ComponentCatalogue catalogue) {

        List<JPanel> panels = new ArrayList<>();

        PerfCakeComponent component = value.getModel().getComponent();

        List<String> components = catalogue.list(component);
        for (String name : components) {

            String docs;
            DocsService docsService = value.getModel().getDocsService();
            ComponentLoader loader = new ComponentLoaderImpl();
            Class<?> implementation = loader.loadComponent(name, component);
            docs = docsService.getDocs(implementation);
            if (docs == null) {
                docs = "Description cannot be found!";
            }

            JPanel componentPanel = createComponentChooserPanel(name, docs);
            panels.add(componentPanel);
        }

        // add individual panels
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.PAGE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 0;

        for (JPanel p : panels) {
            panel.add(p, c);
            c.gridy++;
            addSepartor(panel, c);
            c.gridy++;
        }

        // to start from the top, we need to set weighty of the last component to 1, so that it eats up all the redundant space
        // therefore we will use dummy component
        // see: http://stackoverflow.com/a/24790318
        JPanel dummy = swingFactory.createPanel();
        c.weightx = 1;
        c.weighty = 1;
        panel.add(dummy, c);
    }

    private JPanel createKeyValuePanel(String sectionName, KeyValue keyValue) {

        JPanel keyValuePanel = swingFactory.createPanel();
        keyValuePanel.setLayout(new GridBagLayout());

        JLabel headerLabel = swingFactory.createLabel();
        headerLabel.setFont(getHeaderFont(headerLabel.getFont()));
        headerLabel.setText(sectionName);

        GridBagConstraints c = createGridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridwidth = 2;
        keyValuePanel.add(headerLabel, c);


        JLabel keyLabel = swingFactory.createLabel();
        keyLabel.setText("type: ");
        JLabel valueLabel = swingFactory.createLabel();
        valueLabel.setText("value: ");
        JTextField keyField = swingFactory.createTextField();
        keyField.setText(keyValue.getKey());
        JTextField valueField = swingFactory.createTextField();
        valueField.setText(keyValue.getValue());

        if (useDebugBorders) {
            keyValuePanel.setBorder(BorderFactory.createTitledBorder(sectionName));
        }


        c = createGridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        keyValuePanel.add(keyLabel, c);
        c.gridx = 0;
        c.gridy = 2;
        keyValuePanel.add(valueLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.9;
        c.gridx = 1;
        c.gridy = 1;
        keyValuePanel.add(keyField, c);
        c.gridx = 1;
        c.gridy = 2;
        keyValuePanel.add(valueField, c);


        return keyValuePanel;
    }

    private JPanel createModelPanel(Model model) {
        JPanel panel = swingFactory.createPanel();
        panel.setLayout(new GridBagLayout());


        JLabel header = swingFactory.createLabel();

        header.setText(model.getPropertyInfo().getDisplayName());
        // create font
        Font headerFont = getHeaderFont(header.getFont());
        header.setFont(headerFont);

        String modelName = getModelName(model);

        JLabel idLabel = swingFactory.createLabel();
        idLabel.setText(modelName);

        JButton button = createIconButton(new CogIcon(ICON_WIDTH, ICON_HEIGHT, iconColor), false);

        GridBagConstraints c = createGridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        c.insets = new Insets(5, 5, 5, 5);

        c.gridx = 0;
        c.gridy = 0;
        panel.add(header, c);

        c.gridy = 1;
        c.weightx = 0.9;
        c.fill = GridBagConstraints.BOTH;
        panel.add(idLabel, c);

        c.gridy = 0;
        c.gridheight = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        c.gridheight = 2;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.weightx = 0;
        panel.add(button, c);

        return panel;

    }

    private JPanel createDetailedValuePanel(FormController controller, Value value) {
        JPanel panel = swingFactory.createPanel();
        panel.setLayout(new GridBagLayout());


        JLabel header = swingFactory.createLabel();
        header.setText(value.getValue());
        // create font
        Font headerFont = getHeaderFont(header.getFont());
        header.setFont(headerFont);

        JTextArea docs = swingFactory.createTextArea();
        docs.setBackground(panel.getBackground());
        docs.setEditable(false);
        DocsService docsService = value.getModel().getDocsService();

        ComponentLoader loader = new ComponentLoaderImpl();
        Class<?> impl = loader.loadComponent(value.getValue(), value.getModel().getComponent());
        String docsText = docsService.getDocs(impl);
        if (docsText == null || docsText.isEmpty()) {
            docsText = "Description is not available!";
        }
        docs.setText(docsText);


        GridBagConstraints c = createGridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.9;

        panel.add(header, c);
        c.gridy++;
        c.fill = GridBagConstraints.BOTH;
        panel.add(docs, c);

        JButton button = createIconButton(new ListIcon(ICON_WIDTH, ICON_HEIGHT), true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormController selectorContorller = new ComponentSelctorFormController(controller.getModel());
                controller.getFormManager().addPage(selectorContorller);
            }
        });

        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 2;
        c.anchor = GridBagConstraints.LINE_END;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;

        panel.add(button, c);

        return panel;
    }

    private JPanel createComponentChooserPanel(String name, String docs) {
        JPanel panel = swingFactory.createPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = createGridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        JLabel header = swingFactory.createLabel();
        header.setText(name);
        // create font
        Font headerFont = getHeaderFont(header.getFont());
        header.setFont(headerFont);

        JTextArea docsAre = swingFactory.createTextArea();
        docsAre.setBackground(panel.getBackground());
        docsAre.setEditable(false);

        docsAre.setText(docs);


        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        panel.add(header, c);
        c.gridy++;
        c.fill = GridBagConstraints.BOTH;
        panel.add(docsAre, c);

        JButton button = createIconButton(new ListIcon(ICON_WIDTH, ICON_HEIGHT), true);

        c.gridy = 0;
        c.gridheight = 2;
        c.anchor = GridBagConstraints.LINE_END;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;

        panel.add(button, c);

        return panel;
    }

    private JPanel createValuesPanel() {
        JPanel valuesPanel = swingFactory.createPanel();
        if (useDebugBorders) {
            valuesPanel.setBorder(BorderFactory.createTitledBorder("Simple options"));
        }
        valuesPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = createGridBagConstraints();

        JLabel header = swingFactory.createLabel();
        Font headerFont = getHeaderFont(header.getFont());
        header.setFont(headerFont);
        header.setText("Options: ");

        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.NONE;
        valuesPanel.add(header, constraints);

        return valuesPanel;
    }

    private void addSimpleValueToPanel(JPanel panel, PropertyInfo info, Value value, int columnIndex) {
        // gridbag layout with three columns is expected!
        GridBagConstraints constraints = createGridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = columnIndex;
        JLabel label = swingFactory.createLabel();
        label.setText(info.getDisplayName() + ": ");
        JTextField field = swingFactory.createTextField();
        if (value == null) {
            field.setText("null");
            field.setEnabled(false);
        } else {
            field.setText(value.getValue());
        }

        panel.add(label, constraints);
        constraints.gridx++;
        constraints.weightx = 0.9;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, constraints);

        constraints.gridx++;
        constraints.weightx = 0;

        JButton button = createSwitchValueButton(info, value, field);

        panel.add(button, constraints);


    }

    private JPanel createListOfValuesPanel(PropertyInfo propertyInfo, List<Property> values) {
        JPanel panel = swingFactory.createPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = createGridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridwidth = 2;
        c.ipadx = 0;
        c.ipady = 0;
        c.insets = new Insets(0, 0, 0, 0);

        JLabel headerLabel = swingFactory.createLabel();
        headerLabel.setFont(getHeaderFont(headerLabel.getFont()));
        headerLabel.setText(propertyInfo.getDisplayName());
        panel.add(headerLabel, c);

        if (useDebugBorders) {
            panel.setBorder(BorderFactory.createTitledBorder(propertyInfo.getDisplayName()));
        }

        int gridy = 1;
        for (Property value : values) {
            Value v = value.cast(Value.class);

            JTextField textField = swingFactory.createTextField();
            textField.setText(v.getValue());
            c.weightx = 0.95;
            c.gridx = 0;
            c.gridwidth = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridy = gridy;
            panel.add(textField, c);

            c.gridx = 1;
            c.weightx = 0.05;
            c.fill = GridBagConstraints.NONE;
            JButton icon = createIconButton(new MinusIcon(ICON_WIDTH, ICON_HEIGHT, removeIconColor), false);
            panel.add(icon, c);

            gridy++;

            //c.weightx = 0;
            //c.gridwidth = 2;
            //c.gridy = gridy;
            //c.gridx = 0;
            //c.fill = GridBagConstraints.HORIZONTAL;
            //JSeparator separator = createLightSeparator();
            //panel.add(separator, c);
            //gridy++;
            c.fill = GridBagConstraints.NONE;
        }

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        JButton addButton = createIconButton(new PlusIcon(ICON_WIDTH, ICON_HEIGHT, addIconColor), false);
        panel.add(addButton, c);

        return panel;

    }

    private JPanel createListOfKeyValuesPanel(PropertyInfo propertyInfo, List<Property> keyValues) {
        JPanel panel = swingFactory.createPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = createGridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.ipadx = 0;
        c.ipady = 0;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.LINE_START;
        c.gridwidth = 3;

        JLabel headerLabel = swingFactory.createLabel();
        headerLabel.setFont(getHeaderFont(headerLabel.getFont()));
        headerLabel.setText(propertyInfo.getDisplayName());
        panel.add(headerLabel, c);

        if (useDebugBorders) {
            panel.setBorder(BorderFactory.createTitledBorder(propertyInfo.getDisplayName()));
        }

        int gridy = 1;
        for (Property keyValue : keyValues) {
            KeyValue kv = keyValue.cast(KeyValue.class);

            JTextField keyTextField = swingFactory.createTextField();
            keyTextField.setText(kv.getKey());

            JTextField valueTextField = swingFactory.createTextField();
            valueTextField.setText(kv.getValue());
            c.weightx = 0.45;
            c.gridx = 0;
            c.gridwidth = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridy = gridy;
            panel.add(keyTextField, c);
            c.gridx = 1;
            panel.add(valueTextField, c);

            c.weightx = 0.05;
            c.gridx = 2;
            c.fill = GridBagConstraints.NONE;
            JButton icon = createIconButton(new MinusIcon(ICON_WIDTH, ICON_HEIGHT, removeIconColor), false);
            panel.add(icon, c);

            gridy++;

            //c.weightx = 0;
            //c.gridwidth = 3;
            //c.gridy = gridy;
            //c.gridx = 0;
            //c.fill = GridBagConstraints.HORIZONTAL;
            //JSeparator separator = createLightSeparator();
            //panel.add(separator, c);
            //gridy++;
            //c.fill = GridBagConstraints.NONE;
        }

        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        JButton addButton = createIconButton(new PlusIcon(ICON_WIDTH, ICON_HEIGHT, addIconColor), false);
        panel.add(addButton, c);

        return panel;
    }

    private JPanel createListOfModelPanel(PropertyInfo propertyInfo, List<Property> models) {
        JPanel panel = swingFactory.createPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = createGridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridwidth = 3;
        c.ipadx = 0;
        c.ipady = 0;
        c.insets = new Insets(0, 0, 0, 0);

        JLabel headerLabel = swingFactory.createLabel();
        headerLabel.setFont(getHeaderFont(headerLabel.getFont()));
        headerLabel.setText(propertyInfo.getDisplayName());
        panel.add(headerLabel, c);

        if (useDebugBorders) {
            panel.setBorder(BorderFactory.createTitledBorder(propertyInfo.getDisplayName()));
        }

        int gridy = 1;
        for (Property model : models) {
            Model m = model.cast(Model.class);

            JLabel modelLabel = swingFactory.createLabel();
            c.gridwidth = 1;
            c.weightx = 0.99;
            c.gridy = gridy;
            c.gridx = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            String text = getModelName(m);
            modelLabel.setText(text);
            panel.add(modelLabel, c);

            c.weightx = 0.01;
            c.fill = GridBagConstraints.NONE;

            c.gridx = 1;
            JButton removeButton = createIconButton(new MinusIcon(ICON_WIDTH, ICON_HEIGHT, removeIconColor), false);
            panel.add(removeButton, c);
            c.gridx = 2;
            JButton configureButton = createIconButton(new CogIcon(ICON_WIDTH, ICON_HEIGHT, iconColor), false);
            panel.add(configureButton, c);

            gridy++;

            //c.gridwidth = 3;
            //c.gridy = gridy;
            //c.gridx = 0;
            //c.fill = GridBagConstraints.HORIZONTAL;
            //JSeparator separator = createLightSeparator();
            //panel.add(separator, c);
            //gridy++;
            //c.fill = GridBagConstraints.NONE;
        }

        c.gridwidth = 3;
        c.gridx = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        c.gridy = gridy;
        JButton addButton = createIconButton(new PlusIcon(ICON_WIDTH, ICON_HEIGHT, addIconColor), false);
        panel.add(addButton, c);

        return panel;
    }

    private GridBagConstraints createGridBagConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.ipady = 3;
        int insetsSize = 2;
        constraints.insets = new Insets(insetsSize, insetsSize, insetsSize, insetsSize);
        constraints.weightx = 0.1;
        return constraints;
    }

    private GridBagConstraints createSeparatorConstraints(GridBagConstraints c) {
        GridBagConstraints separatorConstraints = new GridBagConstraints();
        separatorConstraints.fill = GridBagConstraints.HORIZONTAL;
        separatorConstraints.insets = new Insets(5, 5, 5, 5);
        separatorConstraints.gridy = c.gridy;
        return separatorConstraints;
    }

    private void addSepartor(JPanel panel, GridBagConstraints c) {
        JSeparator jSeparator = createSeparator();
        GridBagConstraints separatorConstraints = createSeparatorConstraints(c);
        panel.add(jSeparator, separatorConstraints);
    }

    private String getModelName(Model m) {
        String text = null;

        if (m.getSupportedProperty(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY) != null) {
            text = m.getSingleProperty(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY, Value.class).getValue();
        } else {

            // add special handling for message.
            if (m instanceof MessageModel) {
                String uriPropertyName = MessageModel.PropertyNames.URI.toString();
                String contentPropertyName = MessageModel.PropertyNames.CONTENT.toString();
                Value uri = m.getSingleProperty(uriPropertyName, Value.class);
                Value content = m.getSingleProperty(contentPropertyName, Value.class);
                if (text == null && uri != null) {
                    text = uri.getValue();
                }
                if (text == null && content != null) {
                    text = content.getValue();
                }
                logger.info("Model has not neither uri nor content set. Falling back to: Message");
                text = "Message";

            }

            if (text == null) {
                String className = m.getClass().getSimpleName();
                logger.warn("Cannot derive model name. Falling back to model class name: {}.", className);
                text = className;
            }
        }
        return text;
    }

    /**
     * Creates a switch button which controls if another component is null or not.
     *
     * @param value value of the button
     * @param field field which is controlled by this button
     * @return switch button
     */
    private JButton createSwitchValueButton(PropertyInfo info, final Value value, final JTextField field) {
        JButton button = swingFactory.createButton();
        button.setMargin(new Insets(0, 2, 0, 2));
        button.setContentAreaFilled(false);
        final String nullifyText = "nullify";
        final String enableText = "enable";
        button.addActionListener(new ActionListener() {
            private boolean isNull = value == null;
            private String previousValue = "";

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isNull) {
                    field.setText(previousValue);
                    if (info.getMinOccurs() > 0) {
                        button.setEnabled(false);
                    }
                    field.setEnabled(true);
                    button.setText(nullifyText);
                    isNull = false;
                } else {
                    previousValue = field.getText();
                    field.setText("null");
                    field.setEnabled(false);
                    field.repaint();
                    button.setText(enableText);
                    isNull = true;
                }
            }
        });

        if (value == null) {
            button.setText(enableText);
        } else {
            button.setText(nullifyText);
            if (info.getMinOccurs() > 0) {
                button.setEnabled(false);
            }
        }
        return button;
    }

    private JSeparator createSeparator() {
        JSeparator jSeparator = swingFactory.createSeparator();
        jSeparator.setBackground(Color.darkGray);
        jSeparator.setOrientation(SwingConstants.HORIZONTAL);
        return jSeparator;
    }

    private JSeparator createLightSeparator() {
        JSeparator separator = createSeparator();
        separator.setForeground(Color.LIGHT_GRAY);
        separator.setBackground(Color.LIGHT_GRAY);
        separator.setBorder(BorderFactory.createEmptyBorder());
        return separator;
    }

    /**
     * Creates button with icon
     *
     * @param icon    icon to be drawn
     * @param borders draw button borders
     * @return button.
     */
    private JButton createIconButton(Icon icon, boolean borders) {
        JButton button = swingFactory.createButton();
        button.setIcon(icon);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(borders);


        if (borders == true) {
            Dimension buttonSize = new Dimension(2 * ICON_WIDTH, 2 * ICON_HEIGHT);
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
        } else {
            button.setMargin(new Insets(0, 0, 0, 0));
        }

        if (borders == false && useDebugBorders == true) {
            button.setBorderPainted(true);
        }
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    protected Font getHeaderFont(Font defaultFont) {
        Font font;
        Map<TextAttribute, Object> attributes = new HashMap<>(defaultFont.getAttributes());
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        attributes.put(TextAttribute.SIZE, 14);
        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);

        font = defaultFont.deriveFont(attributes);

        return font;
    }

}
