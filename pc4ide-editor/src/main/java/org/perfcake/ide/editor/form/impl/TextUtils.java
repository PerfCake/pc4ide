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

package org.perfcake.ide.editor.form.impl;

import java.awt.Color;
import org.apache.commons.lang3.StringUtils;

/**
 * Text utils class provides helper methods for setting a text in Swing components.
 *
 * @author Jakub Knetl
 */
public class TextUtils {

    private TextUtils() {
    }

    /**
     * Replaces line separtors with HTML &lt;br /&gt; tag.
     *
     * @param text text to be replaced
     * @return text which has &lt;br /&gt; tags instead of newlines
     */
    public static String replaceNewlineWithHtml(String text) {
        return StringUtils.replace(text, "\n", "<br>");
    }

    /**
     * @param text      text to be shortened
     * @param maxLength max length of the text
     * @return The first line of the text which is first line of the text argument, which has at most maxLength character.
     */
    public static String shortenText(String text, int maxLength) {
        String shortenedText = text;
        if (shortenedText.length() > maxLength) {
            shortenedText = shortenedText.substring(0, maxLength);
        }

        return shortenedText;
    }

    /**
     * @param text text
     * @return The first line of text argument.
     */
    public static String firstLine(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }

        int newLineIndex = text.indexOf("\n");
        if (newLineIndex == -1) {
            return text;
        } else {
            return text.substring(0, newLineIndex);
        }
    }

    /**
     * Make the text colored by surronding with a html tag.
     *
     * @param text  text
     * @param color color
     * @return colored text
     */
    public static String color(String text, Color color) {
        float[] colors = color.getRGBColorComponents(null);
        String colorCode = String.format("#%x%x%x", colors[0], colors[1], colors[2]);
        text = String.format("<p style=\"%s\">%s</p>", colorCode, text);
        return text;
    }

    /**
     * Makes a text bold by surronding it with a html tag.
     *
     * @param text text
     * @return bold text
     */
    public static String bold(String text) {
        return String.format("<b>%s</b>", text);
    }

    public static String html(String text) {
        return String.format("<html>%s</html>", text);
    }

    /**
     * @param text text
     * @return True if the text is wrapped in the html tag.
     */
    public static boolean hasHtml(String text) {
        if (text.startsWith("<html>") && text.endsWith("</html>")) {
            return true;
        }
        return false;
    }

}
