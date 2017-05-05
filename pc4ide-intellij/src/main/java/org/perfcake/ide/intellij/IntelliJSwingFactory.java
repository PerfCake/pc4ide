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

package org.perfcake.ide.intellij;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBScrollPane;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.perfcake.ide.editor.swing.SwingFactory;
import org.perfcake.ide.editor.utils.FontUtils;

/**
 * Swing factory for intellij idea.
 *
 * @author Jakub Knetl
 */
public class IntelliJSwingFactory implements SwingFactory {

    private Font font;

    private static final Color LIGHTER_FOREGROUND_COLOR = new Color(117, 117, 117);

    public IntelliJSwingFactory() {
        font = FontUtils.getSansFont();
    }

    @Override
    public JLabel createLabel() {
        JLabel label = new JLabel();
        label.setFont(font);
        return label;
    }

    @Override
    public JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(font);
        field.setColumns(5);
        return field;
    }

    @Override
    public JPanel createPanel() {
        return new JPanel();
    }

    @Override
    public JScrollPane createSrollPane() {
        JBScrollPane jbScrollPane = new JBScrollPane();
        return jbScrollPane;
    }

    @Override
    public JTextArea createTextArea() {
        JTextArea area = new JTextArea();
        area.setFont(font);
        area.setLineWrap(true);
        area.setColumns(5);
        area.setForeground(LIGHTER_FOREGROUND_COLOR);
        return area;
    }

    @Override
    public JSeparator createSeparator() {
        JSeparator jSeparator = new JSeparator();
        return jSeparator;
    }

    @Override
    public JButton createButton() {
        JButton button = new JButton();
        button.setFont(font);
        return button;
    }

    @Override
    public JComboBox<String> createComboBox() {
        JComboBox<String> comboBox = new ComboBox<>();
        comboBox.setFont(font);
        return comboBox;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
