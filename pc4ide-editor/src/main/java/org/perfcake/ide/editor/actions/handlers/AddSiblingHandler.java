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

import java.awt.geom.Point2D;
import org.perfcake.ide.core.command.AddPropertyCommand;
import org.perfcake.ide.core.command.Command;
import org.perfcake.ide.core.model.Model;
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
    public void handleAction(Point2D location) {
        Model currentModel = controller.getModel();
        Model newSibling = controller.getModelFactory().createModel(currentModel.getComponent());
        Controller parentController = controller.getParent();

        Command addCommand = new AddPropertyCommand(parentController.getModel(), newSibling, currentModel.getPropertyInfo());
        controller.getCommandInvoker().executeCommand(addCommand);
    }
}
