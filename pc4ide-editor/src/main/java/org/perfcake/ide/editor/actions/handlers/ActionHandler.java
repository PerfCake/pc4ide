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
 * Action handler is executed by {@link org.perfcake.ide.editor.controller.AbstractController} on action event.
 *
 * @author Jakub Knetl
 */
public interface ActionHandler {


    /**
     * @return An action type which is handled by this handler.
     */
    ActionType getEventType();

    void handleAction();

    /**
     * @return Controller which owns this handler. If this handler is not assigned to a controller, then getController return null.
     */
    Controller getController();

    /**
     * Sets controller which owns this handler.
     *
     * @param controller controler which will own this handler.
     */
    void setController(Controller controller);
}
