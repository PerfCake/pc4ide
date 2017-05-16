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
import org.perfcake.ide.editor.form.FormController;
import org.perfcake.ide.editor.form.impl.ComponentSelctorFormController;

/**
 * Choose implementation listener displays an implementation chooser form.
 *
 * @author Jakub Knetl
 */
public class ChooseImplementationListener implements ActionListener {

    // controller of component whose implementation should be chosen
    private FormController controller;

    /**
     * Creates new ChooseImplementationListener.
     *
     * @param controller controller of the component
     */
    public ChooseImplementationListener(FormController controller) {
        if (controller == null) {
            throw new IllegalArgumentException("controller cannot be null");
        }
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.getFormManager().addPage(new ComponentSelctorFormController(controller.getModel(), controller.getModelFactory()));
    }
}
