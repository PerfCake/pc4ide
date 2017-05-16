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
 * KeyValueCommand is able to change KeyValue property.
 *
 * @author Jakub Knetl
 */
public class KeyValueCommand implements Command {

    private KeyValue property;
    private String key;
    private String value;
    private String oldKey;
    private String oldValue;

    /**
     * Creates new KeyValueCommand.
     *
     * @param property key value property.
     * @param key      new value of a key.
     * @param value    new value of a value.
     */
    public KeyValueCommand(KeyValue property, String key, String value) {
        if (property == null) {
            throw new IllegalArgumentException("Property is null.");
        }
        this.property = property;
        this.key = key;
        this.value = value;
    }

    @Override
    public void execute() {
        oldKey = property.getKey();
        oldValue = property.getValue();

        property.setKey(key);
        property.setValue(value);
    }

    @Override
    public void undo() {
        property.setKey(oldKey);
        property.setValue(oldValue);
    }

    @Override
    public boolean isUndoable() {
        return true;
    }
}
