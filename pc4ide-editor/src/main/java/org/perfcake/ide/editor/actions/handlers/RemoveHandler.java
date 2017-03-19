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

import org.perfcake.ide.core.command.Command;
import org.perfcake.ide.core.command.RemovePropertyCommand;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.editor.actions.ActionType;
import org.perfcake.ide.editor.controller.Controller;

/**
 * Handler which removes his owning controller from the model.
 *
 * @author Jakub Knetl
 */
public class RemoveHandler extends AbstractHandler {

    public RemoveHandler() {
        super(ActionType.REMOVE);
    }

    @Override
    public void handleAction() {
        if (controller == null) {
            throw new IllegalStateException("controller must not be null");
        }
        if (controller.getParent() == null) {
            throw new IllegalStateException("Controller must have parent!");
        }

        Controller parentController = controller.getParent();
        Model model = controller.getModel();
        Model parentModel = parentController.getModel();

        Command removeCommand = new RemovePropertyCommand(parentModel, model, model.getPropertyInfo());
        controller.getCommandInvoker().executeCommand(removeCommand);
    }
}

