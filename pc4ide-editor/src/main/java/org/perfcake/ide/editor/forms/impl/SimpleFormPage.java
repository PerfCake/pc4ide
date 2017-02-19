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

package org.perfcake.ide.editor.forms.impl;

import javax.swing.JPanel;

import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.editor.forms.FormGenerator;
import org.perfcake.ide.editor.forms.FormManager;
import org.perfcake.ide.editor.forms.FormPage;
import org.perfcake.ide.editor.forms.FormPageDirector;

/**
 * Default implementation for {@link FormPage}.
 *
 * @author jknetl
 */
public class SimpleFormPage implements FormPage {

    private Model model;
    private FormPageDirector pageDirector;
    private JPanel form;
    private FormGenerator formGenerator;
    private FormManager formManager;

    /**
     * Creates new properties form page.
     *
     * @param formManager form manager which manages the page
     * @param model model model of model edited by this form page
     */
    public SimpleFormPage(FormManager formManager, Model model) {
        super();
        if (formManager == null) {
            throw new IllegalArgumentException("formManager cannot be null");
        }
        if (model == null) {
            throw new IllegalArgumentException("model cannot be null");
        }
        this.formManager = formManager;
        this.model = model;

        form = new JPanel();
        formGenerator = new FormGeneratorImpl(model, pageDirector, formManager.getComponentManager(), form);
        formGenerator.createForm();
    }

    @Override
    public boolean isValid() {
        return pageDirector.isValid();
    }

    @Override
    public FormManager getFormManager() {
        return formManager;
    }

    @Override
    public JPanel getContentPanel() {
        return form;
    }

    @Override
    public String getMessage() {
        return pageDirector.getMessage();
    }

    @Override
    public void updateForm() {
        // TODO Auto-generated method stub
    }

    @Override
    public void applyChanges() {
        // TODO Auto-generated method stub

    }

    @Override
    public Object getModel() {
        return model;
    }

}
