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

package org.perfcake.ide.editor.colors;

import java.awt.Color;

/**
 * <p>Color scheme in pc4ide-editor is represented as 25 colors. It is supposed that first 8 colors serves as base colors, next 8
 * colors serves as accent colors, and last 9 colors as component colors (each component has dedicated color).</p>
 *
 * <p>Colorscheme was inspired by <a href="https://github.com/chriskempson/base16">base16 colorschemes</a>.</p>
 *
 * @author Jakub Knetl
 *
 */
public interface ColorScheme {

    /**
     * Total number of colors used by colorscheme.
     */
    public static int NUM_OF_COLORS = 25;

    /**
     * @return Palette of all colors.
     */
    Color[] getPalette();

    /**
     *
     * @param color Color from palette.
     * @return Color from current scheme.
     * @see {@link NamedColor}
     */
    Color getColor(NamedColor color);
}
