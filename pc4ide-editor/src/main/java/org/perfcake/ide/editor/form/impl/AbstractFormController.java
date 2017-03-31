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

import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.editor.form.FormBuilder;
import org.perfcake.ide.editor.form.FormController;
import org.perfcake.ide.editor.form.FormManager;

/**
 * Base class which implements majority of {@link FormController} methods.
 * @author Jakub Knetl
 */
public abstract class AbstractFormController implements FormController {
    protected FormBuilder formBuilder;
    protected FormManager formManager;
    protected Model model;

    public AbstractFormController(Model model) {
        this.model = model;
    }

    public void setFormManager(FormManager formManager) {
        this.formManager = formManager;
    }

    public FormManager getFormManager() {
        return formManager;
    }

    public Model getModel() {
        return model;
    }

    public void setFormBuilder(FormBuilder formBuilder) {
        this.formBuilder = formBuilder;
    }

    public FormBuilder getFormBuilder() {
        return formBuilder;
    }
}
