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

import java.awt.Color;
import org.perfcake.ide.editor.swing.icons.ResizableIcon;

/**
 * Abstract implementation of component icon.
 *
 * @author Jakub Knetl
 */
public abstract class AbstractIcon implements ResizableIcon {
    /**
     * The width of this icon.
     */
    protected int width;
    /**
     * The height of this icon.
     */
    protected int height;

    /**
     * Color of the icon.
     */
    protected Color color;

    /**
     * Creates new component icon.
     *
     * @param width  width of the icon
     * @param height height of the icon
     * @param color color of the icon.
     */
    public AbstractIcon(int width, int height, Color color) {
        this.height = height;
        this.width = width;
        this.color = color;
    }

    public int getIconHeight() {
        return height;
    }

    public int getIconWidth() {
        return width;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    public void setIconWidth(int width) {
        this.width = width;
    }

    public void setIconHeight(int height) {
        this.height = height;
    }
}
