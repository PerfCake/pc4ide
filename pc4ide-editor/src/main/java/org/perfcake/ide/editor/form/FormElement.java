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

package org.perfcake.ide.editor.form;

/**
 * FormElement represent view of one property in the form.
 *
 * @author Jakub Knetl
 */
public interface FormElement {

    /**
     * @return Current value displayed by this element.
     */
    String getValue();

    /**
     * Sets displayed value by this element.
     *
     * @param value value to be displayed
     */
    void setValue(String value);

    /**
     * @return true if value in the field is valid.
     */
    boolean isValid();

    /**
     * Adds listener.
     *
     * @param listener listener to be added.
     */
    void addListener(FormEventListener listener);

    /**
     * Removes listener.
     *
     * @param listener listener to be removed
     * @return true if the listener has been removed.
     */
    boolean removeListener(FormEventListener listener);
}
