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

import org.perfcake.ide.editor.actions.ActionType;
import org.perfcake.ide.editor.controller.Controller;

/**
 * Represents abstract handler.
 *
 * @author Jakub Knetl
 */
public abstract class AbstractHandler implements ActionHandler {

    protected ActionType actionType;
    protected Controller controller;

    public AbstractHandler(ActionType actionType) {
        this.actionType = actionType;
    }

    @Override
    public ActionType getEventType() {
        return actionType;
    }

    @Override
    public Controller getController() {
        return controller;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }
}

