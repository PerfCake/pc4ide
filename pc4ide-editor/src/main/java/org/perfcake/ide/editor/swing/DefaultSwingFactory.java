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

package org.perfcake.ide.editor.swing;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.perfcake.ide.editor.utils.FontUtils;

/**
 * Default swing factory is default implementation of {@link SwingFactory}.
 *
 * @author Jakub Knetl
 */
public class DefaultSwingFactory implements SwingFactory {

    private Font font;

    public DefaultSwingFactory() {
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
        return new JScrollPane();
    }

    @Override
    public JTextArea createTextArea() {
        JTextArea area = new JTextArea();
        area.setFont(font);
        area.setLineWrap(true);
        area.setForeground(Color.GRAY);
        area.setColumns(5);
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
        JComboBox<String> comboBox = new JComboBox<String>() {
            @Override
            public String getPrototypeDisplayValue() {
                //this effectively sets minimum size (in conjunction of GridBagLayout horizontal fill)
                return "xxxxxxx";
            }
        };
        comboBox.setFont(font);
        return comboBox;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public JMenuItem createMenuItem() {
        JMenuItem menuItem = new JMenuItem();
        return  menuItem;
    }

    @Override
    public JPopupMenu createPopupMenu() {
        JPopupMenu jPopupMenu = new JPopupMenu();
        return jPopupMenu;
    }
}
