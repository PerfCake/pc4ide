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

package org.perfcake.ide.editor.form.impl;

import javax.swing.JPanel;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.factory.ModelFactory;

/**
 * Basic implementation of {@link org.perfcake.ide.editor.form.FormController}  interface.
 *
 * @author Jakub Knetl
 */
public class FormControllerImpl extends AbstractFormController {

    /**
     * Creates new Form controller.
     *
     * @param model     model which is controlled by the controller
     * @param modelFactory model factory
     */
    public FormControllerImpl(Model model, ModelFactory modelFactory) {
        super(model, modelFactory);
    }

    @Override
    public void drawForm() {
        JPanel panel = formManager.getContentPanel();
        formBuilder.buildForm(panel, model, this);
    }

}
