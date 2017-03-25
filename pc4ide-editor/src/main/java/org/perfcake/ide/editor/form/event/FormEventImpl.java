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

package org.perfcake.ide.editor.form.event;

import java.awt.event.ActionEvent;
import org.perfcake.ide.editor.form.FormElement;
import org.perfcake.ide.editor.form.FormEvent;

/**
 * Implementation of FormEvent.
 * @author Jakub Knetl
 */
public class FormEventImpl implements FormEvent {

    private FormEvent.Type eventType;
    private FormElement element;
    private ActionEvent swingEvent;

    public FormEventImpl(FormEvent.Type eventType, FormElement element) {
        this(eventType,element, null);
    }

    /**
     * Creates new form event.
     * @param eventType event type
     * @param element element which caused the event
     * @param swingEvent underlying swing event
     */
    public FormEventImpl(FormEvent.Type eventType, FormElement element, ActionEvent swingEvent) {
        if (eventType == null) {
            throw new IllegalArgumentException("event type cannot be null.");
        }
        if (element == null) {
            throw new IllegalArgumentException("element cannot be null.");
        }
        this.eventType = eventType;
        this.element = element;
        this.swingEvent = swingEvent;
    }

    @Override
    public Type getEventType() {
        return eventType;
    }

    @Override
    public FormElement getFormElement() {
        return element;
    }

    @Override
    public ActionEvent getUnderlyingEvent() {
        return swingEvent;
    }
}
