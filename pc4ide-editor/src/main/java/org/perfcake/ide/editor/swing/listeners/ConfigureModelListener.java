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

package org.perfcake.ide.editor.swing.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.editor.form.FormController;
import org.perfcake.ide.editor.form.impl.FormControllerImpl;

/**
 * Configure model listeners creates new form page and displays configuration of model.
 *
 * @author Jakub Knetl
 */
public class ConfigureModelListener implements ActionListener {

    // model which should be displayed
    private Model model;
    // controller of currently displayed model
    private FormController controller;

    /**
     * Creates new ConfigureModelListener.
     *
     * @param model model which will be configured
     * @param controller current controller.
     */
    public ConfigureModelListener(Model model, FormController controller) {
        if (model == null) {
            throw new IllegalArgumentException("model cannot be null");
        }
        if (controller == null) {
            throw new IllegalArgumentException("controller cannot be null");
        }
        this.model = model;
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.getFormManager().addPage(new FormControllerImpl(model, controller.getModelFactory()));
    }
}
