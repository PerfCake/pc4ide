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

package org.perfcake.ide.editor.swing.icons;

import java.awt.Shape;
import org.perfcake.ide.editor.actions.ActionType;

/**
 * Control icon is a {@link ResizableIcon}, which represent an action.
 *
 * @author Jakub Knetl
 */
public interface ControlIcon extends ResizableIcon {

    /**
     * @return An action of this icon.
     */
    ActionType getAction();

    /**
     * Sets action for this icon.
     *
     * @param action an action.
     */
    void setAction(ActionType action);

    /**
     * Get bounds of this icons. These bounds maintains an area in which is this icon active.
     *
     * @return Shape of bounds. Within this bounds the icon is active.
     */
    Shape getBounds();
}
