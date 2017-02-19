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
import javax.swing.JPanel;
import org.perfcake.ide.core.components.ComponentCatalogue;

/**
 * FormManger serves as container for the actual forms. Actual forms with text fields and buttons
 * are represented by {@link FormPage}. FormManager aggregates these pages and adds control for whole
 * forms.
 * <p>The FormManager has ContainerPanel which contains all swing components. It has also content panel which is
 * part of ContainerPanel. ContentPanel contains only the swing components whose user will fill in (not the general form
 * control buttons).</p>
 *
 * @author jknetl
 */
public interface FormManager {

    /**
     * Return true if the form is valid.
     *
     * @return True if the data in the form are valid.
     */
    boolean isValid();

    /**
     * Returns inspector manager.
     *
     * @return Component manager used in program.
     */
    ComponentCatalogue getComponentManager();

    /**
     * ContainerPanel contains all form including its control and its content.
     *
     * @return JPanel including all visuals in the form.
     */
    JPanel getContainerPanel();

    /**
     * Content panel contains actual form. It does not include pane with form control buttons.
     *
     * @return Content pane of the form.
     */
    JPanel getContentPanel();

    /**
     * Adds page of the form.
     *
     * @param page Page to be added
     */
    void addFormPage(FormPage page);

    /**
     * Remove page from the form
     *
     * @param page page to be removed
     * @return true if the page was successfuly removed or false if it was not found in the form.
     */
    boolean removePage(FormPage page);

    /**
     * Removes all pages from the manager.
     */
    void removeAllPages();

    /**
     * Return <b>unmodifiable</b> collection of form pages.
     *
     * @return list of pages in the form
     */
    List<FormPage> getFormPages();


    /**
     * Apply changes in the all form pages to the model of the data.
     */
    void applyChanges();
}
