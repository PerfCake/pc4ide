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

package org.perfcake.ide.editor.forms.impl;

import org.perfcake.ide.editor.forms.EventHandler;
import org.perfcake.ide.editor.forms.FormPageDirector;

/**
 * TODO: implement this.
 * @author Jakub Knetl
 */
public class FormDirectorImpl implements FormPageDirector {
    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isSynced() {
        return true;
    }

    @Override
    public void addHandler(EventHandler handler) {

    }

    @Override
    public boolean removeHandler(EventHandler handler) {
        return false;
    }

    @Override
    public void applyChanges() {

    }

    @Override
    public String getMessage() {
        return null;
    }
}
