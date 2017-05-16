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

package org.perfcake.ide.editor.swing.icons.control;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import org.perfcake.ide.editor.actions.ActionType;
import org.perfcake.ide.editor.swing.icons.AbstractIcon;
import org.perfcake.ide.editor.swing.icons.ControlIcon;

/**
 * Abstract implementation of Control icon.
 *
 * @author Jakub Knetl
 */
public abstract class AbstractControlIcon extends AbstractIcon implements ControlIcon {

    protected ActionType action;

    protected int x; // x coordinate of upper left corner
    protected int y; // y coordinate of upper left corner

    /**
     * Creates new component icon.
     *
     * @param width         width of the icon
     * @param height        height of the icon
     * @param color         color of the icon.
     */
    public AbstractControlIcon(int width, int height, Color color) {
        this(width, height, color, ActionType.NONE);
    }

    /**
     * Creates new component icon.
     *
     * @param width         width of the icon
     * @param height        height of the icon
     * @param color         color of the icon.
     * @param action        action of this control icon
     */
    public AbstractControlIcon(int width, int height, Color color, ActionType action) {
        super(width, height, color);
        this.action = action;
    }

    @Override
    public ActionType getAction() {
        return action;
    }

    @Override
    public void setAction(ActionType action) {
        this.action = action;
    }

    @Override
    public Shape getBounds() {
        Rectangle2D bounds = new Rectangle2D.Double(x, y, width, height);
        return bounds;
    }

}
