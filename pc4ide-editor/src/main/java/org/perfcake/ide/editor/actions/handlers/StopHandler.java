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
import org.perfcake.ide.editor.actions.ActionType;
import org.perfcake.ide.editor.controller.RootController;

/**
 * Stops scenario execution.
 *
 * @author Jakub Knetl
 */
public class StopHandler extends AbstractHandler {

    public StopHandler() {
        super(ActionType.STOP);
    }

    @Override
    public void handleAction(Point2D location) {
        if (getController() instanceof RootController) {
            RootController root = (RootController) getController();

            if (root.getExecutionManager() != null) {
                try {
                    root.getExecutionManager().getProcess().destroyForcibly();
                } catch (NullPointerException e) {
                    /* we need to catch null pointer exception since root.getExecutionManager() could be non null in if condition, however
                     * it may become null any time in other trhead if execution has stopped..
                     */
                }
            }
        }
    }
}
