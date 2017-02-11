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

package org.perfcake.ide.editor.forms;

import java.util.List;
import javax.swing.JComponent;

/**
 * FormElement represents element of the form. It usually contains multiple swing elements which
 * are related to one model element.
 * <p>E.g. name of the element (JLabel) + inputField (JTextField) + description</p>
 *
 * @author jknetl
 */
public interface FormElement {

    /**
     * Get all grapphical components representing the element.
     *
     * @return Graphical inspector representing the element or EMPTY_LIST
     */
    List<JComponent> getGraphicalComponents();

    /**
     * This methods is hint for layout manager. If the manager needs to enlarge some inspector it may
     * take a hint from this method.
     *
     * @return index of inspector in the {@link #getGraphicalComponents()} list which should be
     *     expanded by the layout manager if required.
     */
    int getMainComponent();
}
