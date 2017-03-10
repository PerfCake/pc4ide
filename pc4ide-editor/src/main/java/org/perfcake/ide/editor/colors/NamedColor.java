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

/**
 * <p>Named color represent color in {@link ColorScheme}. This palette was inspired by <a href="https://github.com/chriskempson/base16">
 * base16</a> colorschemes.</p>
 *
 * <p>BASE_1 - BASE_8 are supposed to be used as standard background and foreground colors.</p>
 *
 * <p>ACCENT_1 - ACCENT_8 are supposed to be used for highlighting.</p>
 *
 * <p>COMPONENT_* is used for colors of components.</p>
 *
 * @author Jakub Knetl
 */
public enum NamedColor {
    /**
     * Default background.
     */
    BASE_1,

    /**
     * Lighter background.
     */
    BASE_2,

    /**
     * Selection background.
     */
    BASE_3,

    /**
     * Invisibles.
     */
    BASE_4,

    /**
     * Dark foreground.
     */
    BASE_5,

    /**
     * Default foreground.
     */
    BASE_6,

    /**
     * Light foreground.
     */
    BASE_7,

    /**
     * Light background.
     */
    BASE_8,

    /**
     * Accent color 1. Used for warnings. Usually a red hue.
     */
    ACCENT_1,
    /**
     * Accent color 2.
     */
    ACCENT_2,
    /**
     * Accent color 3.
     */
    ACCENT_3,
    /**
     * Accent color 4. Used for adding component or indicating of good state. Usually a green hue.
     */
    ACCENT_4,
    /**
     * Accent color 5.
     */
    ACCENT_5,
    /**
     * Accent color 6.
     */
    ACCENT_6,
    /**
     * Accent color 7.
     */
    ACCENT_7,

    /**
     * Accent color 8.
     */
    ACCENT_8,

    /**
     * Generator color.
     */
    COMPONENT_GENERATOR,

    /**
     * Sender color.
     */
    COMPONENT_SENDER,

    /**
     * Receiver color.
     */
    COMPONENT_RECEIVER,

    /**
     * Correlator color.
     */
    COMPONENT_CORRELATOR,

    /**
     * Sequence color.
     */
    COMPONENT_SEQUENCE,

    /**
     * Validator color.
     */
    COMPONENT_VALIDATOR,

    /**
     * Reporter color.
     */
    COMPONENT_REPORTER,
    /**
     * Destination color.
     */
    COMPONENT_DESTINATION,

    /**
     * Message color.
     */
    COMPONENT_MESSAGE
}
