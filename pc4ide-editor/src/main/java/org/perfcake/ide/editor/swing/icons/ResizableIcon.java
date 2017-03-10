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
import javax.swing.Icon;

/**
 * Swing icon which can change its size.
 *
 * @author jknetl
 */
public interface ResizableIcon extends Icon {

    /**
     * @return Color used for icon.
     */
    Color getColor();

    /**
     * Sets color which will be used for drawing icon.
     * @param color color
     */
    void setColor(Color color);

    void setIconWidth(int width);

    void setIconHeight(int height);

}
