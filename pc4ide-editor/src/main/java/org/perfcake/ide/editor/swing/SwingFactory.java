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

/**
 * Component factory manages creating of various swing components, which are used in the editor.
 *
 * @author Jakub Knetl
 */
public interface SwingFactory {

    /**
     * @return Swing label.
     */
    JLabel createLabel();

    /**
     * @return Textfield.
     */
    JTextField createTextField();

    /**
     * @return JPanel.
     */
    JPanel createPanel();

    /**
     * @return JScrollPane.
     */
    JScrollPane createSrollPane();

    /**
     * @return Text area.
     */
    JTextArea createTextArea();

    /**
     * @return JSeparator.
     */
    JSeparator createSeparator();

    /**
     * @return JButton.
     */
    JButton createButton();

    /**
     * @return JComboBox&lt;String&gt;.
     */
    JComboBox<String> createComboBox();

    /**
     * @return JMenuItem.
     */
    JMenuItem createMenuItem();

    /**
     *
     * @return JPopupMenu.
     */
    JPopupMenu createPopupMenu();
}
