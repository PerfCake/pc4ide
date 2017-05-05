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

package org.perfcake.ide.editor.controller.visitor;

import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.editor.actions.handlers.SelectionHandler;
import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.form.FormManager;

/**
 * This visitor selects view of controller which contains particular model.
 *
 * @author Jakub Knetl
 */
public class SelectModelVisitor extends SearchModelVisitor {

    /**
     * Creates new search model visitor.
     *
     * @param model model for whose controller is being searched
     */
    public SelectModelVisitor(Model model) {
        super(model);
    }

    @Override
    protected void performAction(Controller controller) {
        controller.getView().setSelected(true);
        FormManager formManger = controller.getRoot().getFormManger();
        if (formManger.getCurrentPageController().getModel() != controller.getModel()) {
            //update form controller
            SelectionHandler hanlder = new SelectionHandler();
            hanlder.setController(controller);
            hanlder.handleAction(null);
        }
    }
}
