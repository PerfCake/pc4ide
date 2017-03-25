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

package org.perfcake.ide.editor.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Font utils is class which provides static helper methods for fonts.
 *
 * @author Jakub Knetl
 */
public final class FontUtils {

    static final Logger logger = LoggerFactory.getLogger(FontUtils.class);

    public static final String FONTS_OPENSANS_OPEN_SANS_REGULAR_TTF = "/fonts/opensans/OpenSans-Regular.ttf";

    private FontUtils() {
    }

    /**
     * @return rendering hints for the text.
     */
    public static Map<Object, Object> getRenderingHints() {
        final Map<Object, Object> hints = new HashMap<>();
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        hints.put(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);

        return hints;
    }

    /**
     *
     * @return Sans font.
     */
    public static Font getSansFont() {
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, FontUtils.class.getResourceAsStream(FONTS_OPENSANS_OPEN_SANS_REGULAR_TTF));
        } catch (FontFormatException | IOException e) {
            logger.warn("Cannot load font", e);
        }

        if (font == null) {
            font = Font.decode(Font.SANS_SERIF);
        }
        font = font.deriveFont(13f);

        return font;


    }
}
