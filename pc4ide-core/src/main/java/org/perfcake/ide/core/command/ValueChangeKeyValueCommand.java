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

package org.perfcake.ide.core.command;

import org.perfcake.ide.core.model.properties.KeyValue;

/**
 * Represents change of a value in KeyValue property.
 *
 * @author Jakub Knetl
 */
public class ValueChangeKeyValueCommand implements Command {
    private KeyValue property;
    private String value;
    private String oldValue;

    /**
     * Creates new KeyValueCommand.
     *
     * @param property key value property.
     * @param value    new value of a value.
     */
    public ValueChangeKeyValueCommand(KeyValue property, String value) {
        if (property == null) {
            throw new IllegalArgumentException("Property is null.");
        }
        this.property = property;
        this.value = value;
    }

    @Override
    public void execute() {
        oldValue = property.getValue();

        property.setValue(value);
    }

    @Override
    public void undo() {
        property.setValue(oldValue);
    }

    @Override
    public boolean isUndoable() {
        return true;
    }
}
