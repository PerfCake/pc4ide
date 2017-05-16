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

/**
 * TextBuilder enables to build a text along with isHtmlUsed tags which can be easily used in swing components.
 *
 * @author Jakub Knetl
 */
public class TextBuilder {

    private int maxLength = -1;
    private boolean firstLineOnly = false;
    private boolean replaceNewlines = false;
    private boolean bold = false;
    private boolean visualizeCut = false;
    private Color color = null;

    /**
     * Builds a string according to fields in this builder instance.
     *
     * @param text text to be builded
     * @return a modified string.
     */
    public String buildText(String text) {

        String originalText = text;

        checkConsistency();

        if (firstLineOnly) {
            text = TextUtils.firstLine(text);
        }

        if (maxLength >= 0) {
            text = TextUtils.shortenText(text, maxLength);
        }

        if (visualizeCut && originalText.length() > text.length()) {
            text = String.format("%s...", text);
        }

        if (replaceNewlines) {
            text =  TextUtils.replaceNewlineWithHtml(text);
        }

        if (bold) {
            text = TextUtils.bold(text);
        }
        if (color != null) {
            text = TextUtils.color(text, color);
        }

        if (isHtmlUsed()) {
            text = TextUtils.html(text);
        }

        return text;

    }

    private void checkConsistency() throws IllegalArgumentException {
        // TODO(jknetl): implement this so that it throws  IllegalArgument exception when some fields are incompatible
    }

    public int getMaxLength() {
        return maxLength;
    }

    public TextBuilder setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public boolean isFirstLineOnly() {
        return firstLineOnly;
    }

    public TextBuilder setFirstLineOnly(boolean firstLineOnly) {
        this.firstLineOnly = firstLineOnly;
        return this;
    }

    public boolean isReplaceNewlines() {
        return replaceNewlines;
    }

    public TextBuilder setReplaceNewlines(boolean replaceNewlines) {
        this.replaceNewlines = replaceNewlines;

        return this;
    }

    public boolean isHtmlUsed() {
        return bold || replaceNewlines || color != null;
    }

    public boolean isBold() {
        return bold;
    }

    public TextBuilder setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public TextBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    public boolean isVisualizeCut() {
        return visualizeCut;
    }

    public TextBuilder setVisualizeCut(boolean visualizeCut) {
        this.visualizeCut = visualizeCut;
        return this;
    }
}
