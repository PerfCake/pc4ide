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

import javax.swing.JPanel;

/**
 * Represent page of the form. The page contains input swing controls which
 * are applied to application data.
 * <p>The FormPage has its model object which serves as container for the data.</p>
 *
 * @author jknetl
 */
public interface FormPage {

    /**
     * @return True if the data enterend into the form are valid.
     */
    boolean isValid();

    /**
     * @return {@link FormManager} which manages this FormPage.
     */
    FormManager getFormManager();

    /**
     * @return JPanel with the form.
     */
    JPanel getContentPanel();

    /**
     * @return message which may be used by some {@link FormManager}s to display hint to the user.
     */
    String getMessage();

    /**
     * Update visual representation of the form.
     */
    void updateForm();

    /**
     * Apply changes in this FormPage to the model of the data.
     */
    void applyChanges();

    /**
     * @return Object which is model for the form data.
     */
    Object getModel();

}
