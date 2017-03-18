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

package org.perfcake.ide.editor.actions.handlers;

import org.apache.commons.math3.analysis.function.Abs;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.factory.ModelFactory;
import org.perfcake.ide.editor.actions.ActionType;
import org.perfcake.ide.editor.controller.Controller;

/**
 * Handle adding same component.
 *
 * @author Jakub Knetl
 */
public class AddSiblingHandler extends AbstractHandler {

    /**
     * Creates new AddSiblingHandler.
     */
    public AddSiblingHandler() {
        super(ActionType.ADD);
    }

    @Override
    public void handleAction() {
        Model currentModel = controller.getModel();
        Model newSibling = controller.getModelFactory().createModel(currentModel.getComponent());
        Controller parentController = controller.getParent();
        if (parentController != null) {
            Model parentModel = parentController.getModel();
            parentModel.addProperty(currentModel.getPropertyInfo(), newSibling);
        }
    }
}
