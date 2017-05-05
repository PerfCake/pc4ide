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

/**
 * Debug handlers handles a debug action.
 *
 * @author Jakub Knetl
 */
public class DebugHandler extends RunHandler {

    public DebugHandler() {
        super();
        actionType = ActionType.DEBUG;
    }

    @Override
    public void handleAction(Point2D location) {
        super.handleAction(location);
        //TODO: forcibly set debug option to true
    }
}
