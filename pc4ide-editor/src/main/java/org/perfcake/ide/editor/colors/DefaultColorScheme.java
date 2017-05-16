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
 * Default colorscheme.
 *
 * @author Jakub Knetl
 */
public class DefaultColorScheme implements ColorScheme {

    private Color[] palette;

    /**
     * Creates default colorscheme.
     */
    public DefaultColorScheme() {
        palette = new Color[NUM_OF_COLORS];

        // base colors
        palette[0] = new Color(255, 255, 255);
        palette[1] = new Color(214, 214, 214);
        palette[2] = new Color(170, 170, 170);
        palette[3] = new Color(153, 153, 153);
        palette[4] = new Color(126, 126, 126);
        palette[5] = new Color(90, 90, 90);
        palette[6] = new Color(75, 75, 75);
        palette[7] = new Color(53, 53, 53);


        // accent colors comes from base16 shapeshifter theme:
        // https://github.com/chriskempson/base16-xresources/blob/master/xresources/base16-shapeshifter-256.Xresources
        palette[8] = new Color(0xe92f2f);
        palette[9] = new Color(0xe09448);
        palette[10] = new Color(0xdddd13);
        palette[11] = new Color(0x0ed839);
        palette[12] = new Color(0x23edda);
        palette[13] = new Color(0x3b48e3);
        palette[14] = new Color(0xf996e2);
        palette[15] = new Color(0x69542d);


        // accent colors
        palette[16] = new Color(0xFF6FAF);
        palette[17] = new Color(0xFFC73A);
        palette[18] = new Color(0x6FA0FF);
        palette[19] = new Color(0x47C30C);
        palette[20] = new Color(0xFF8054);
        palette[21] = new Color(0x7BDDD9);
        palette[22] = new Color(0xFF535F);
        palette[23] = new Color(0x9C81F5);
        palette[24] = new Color(0x6AD299);

    }

    @Override
    public Color[] getPalette() {
        return palette;
    }

    @Override
    public Color getColor(NamedColor color) {
        return palette[color.ordinal()];
    }
}

