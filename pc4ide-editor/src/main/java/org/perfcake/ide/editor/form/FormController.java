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

import org.perfcake.ide.core.model.Model;

/**
 * Controls the form.
 *
 * @author Jakub Knetl
 */
public interface FormController {

    /**
     * Draws the form on the surface.
     */
    void drawForm();

    /**
     * Sets form manager of this form controller.
     * @param formManager formManager which contains this controller.
     */
    void setFormManager(FormManager formManager);

    /**
     * @return Form manager which contains this controller. If no Form manager contains this controller then null is returned.
     */
    FormManager getFormManager();

    /**
     * @return Model which is managed by this controller.
     */
    Model getModel();

    /**
     * Sets form builder to the form controller.
     * @param formBuilder form builder to be used
     */
    void setFormBuilder(FormBuilder formBuilder);

    /**
     * @return Form builder of this controller.
     */
    FormBuilder getFormBuilder();
}
