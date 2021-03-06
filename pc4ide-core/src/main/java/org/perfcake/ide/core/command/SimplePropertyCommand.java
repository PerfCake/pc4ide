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

import org.perfcake.ide.core.model.properties.Value;

/**
 * Represents commands which sets value of a simple property.
 *
 * @author Jakub Knetl
 */
public class SimplePropertyCommand implements Command {

    private Value property;
    private String oldValue;
    private String newValue;

    /**
     * Creates new simple property command.
     * @param property property to be changed
     * @param newValue new value of the property
     */
    public SimplePropertyCommand(Value property, String newValue) {
        this.property = property;
        this.newValue = newValue;
    }

    @Override
    public void execute() {
        oldValue = property.getValue();
        property.setValue(newValue);
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
