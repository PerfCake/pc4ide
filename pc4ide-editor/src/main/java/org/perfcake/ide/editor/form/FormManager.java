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

package org.perfcake.ide.editor.form;

import javax.swing.JPanel;
import org.perfcake.ide.core.command.invoker.CommandInvoker;
import org.perfcake.ide.core.components.ComponentCatalogue;

/**
 * Form manager manages form which is used to modify component properties. Form manager can go back between multiple pages. It manages
 * pages in a stack so only the top page of the stack can be displayed.
 */
public interface FormManager {

    /**
     * @return Content page for form components.
     */
    JPanel getContentPanel();

    /**
     *
     * @return Master panel which contains all parts of the form (including content panel).
     */
    JPanel getMasterPanel();

    /**
     * Deletes all form controls from content panel.
     */
    void cleanContentPanel();

    /**
     * Adds, creates and displayes page using controller.
     *
     * @param controller controller of the form page
     */
    void addPage(FormController controller);

    /**
     * Removes latest page from this manager.
     *
     * @return True if a page was removed, or false if there was no page in this manager.
     */
    boolean removePage();

    /**
     * Removes all pages from current manager.
     */
    void removeAllPages();

    /**
     * @return Number of pages int this manager.
     */
    int getNumOfPages();


    /**
     * @return Controller of current page or null, if there is no page.
     */
    FormController getCurrentPageController();

    /**
     * @return Command invoker which can be used for executing commands.
     */
    CommandInvoker getCommandInvoker();

    /**
     * @return Component catalogue.
     */
    ComponentCatalogue getComponentCatalogue();
}
