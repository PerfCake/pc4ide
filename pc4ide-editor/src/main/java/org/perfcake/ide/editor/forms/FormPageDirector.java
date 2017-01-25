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
 * FormPageDirector may be used within the form page. It manages application data and input from the
 * user and coordinates operations which requires both types the data.
 *
 * @author jknetl
 */
public interface FormPageDirector {

    /**
     * @return True if data in the form are valid according to underlying application data model.
     */
    boolean isValid();

    /**
     * @return True if the data in the model and data in the form are in sync.
     */
    boolean isSynced();

    /**
     * Adds {@link EventHandler}.
     *
     * @param handler handler to be added
     */
    void addHandler(EventHandler handler);

    /**
     * Remove {@link EventHandler}.
     *
     * @param handler handler to be removed
     * @return True if the handler was successfuly removed. False if it was not found in the handlers.
     */
    boolean removeHandler(EventHandler handler);

    /**
     * Applies changes from the form to the underlying data model.
     */
    void applyChanges();

    /**
     * @return Message which may be used as a hint for the user.
     */
    String getMessage();
}
