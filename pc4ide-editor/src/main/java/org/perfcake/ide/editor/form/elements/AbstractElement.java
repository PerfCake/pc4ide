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

package org.perfcake.ide.editor.form.elements;

import java.util.HashSet;
import java.util.Set;
import org.perfcake.ide.editor.form.FormElement;
import org.perfcake.ide.editor.form.FormEvent;
import org.perfcake.ide.editor.form.FormEventListener;

/**
 * Base class for elements.
 *
 * @author Jakub Knetl
 */
public abstract class AbstractElement implements FormElement {

    protected boolean valid = true;

    private Set<FormEventListener> listeners = new HashSet<>();

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void addListener(FormEventListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    @Override
    public boolean removeListener(FormEventListener listener) {
        boolean removed = false;
        if (listener != null) {
            removed = listeners.remove(listener);
        }

        return removed;
    }

    /**
     * Delivers event to all listeners
     *
     * @param event event.
     */
    protected void fireEvent(FormEvent event) {
        for (FormEventListener l : listeners) {
            l.formChanged(event);
        }
    }
}
