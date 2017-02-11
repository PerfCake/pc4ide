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

package org.perfcake.ide.editor.forms;

/**
 * FormBuilder incrementally builds a from by adding/removing the form elements. It should manage layout
 * of the components by its own.
 *
 * @author jknetl
 */
public interface FormBuilder {

    /**
     * Adds form element into the form.
     *
     * @param element element to be added
     */
    void addElement(FormElement element);

    /**
     * Remove form element from the form.
     *
     * @param element element to be removed.
     * @return true if the element was removed, false if wasn't part of form.
     */
    boolean removeElement(FormElement element);
}
