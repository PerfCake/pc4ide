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

package org.perfcake.ide.core.command.impl;

import org.perfcake.ide.core.Field;
import org.perfcake.ide.core.command.Command;
import org.perfcake.ide.core.exception.ModelDirectorException;
import org.perfcake.ide.core.model.director.ModelDirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DirectedSetCommand uses {@link org.perfcake.ide.core.model.director.ModelDirector} to set field to new value;
 */
public class DirectedSetCommand implements Command {
    static final Logger logger = LoggerFactory.getLogger(DirectedSetCommand.class);

    private ModelDirector director;
    private Field field;
    private Object oldValue;
    private Object newValue;

    /**
     * Creates new DirectedSetCommand
     * @param director Director used to set value.
     * @param field Field to set
     * @param newValue value to set
     */
    public DirectedSetCommand(ModelDirector director, Field field, Object newValue) {
        this.director = director;
        this.field = field;
        this.newValue = newValue;
    }

    @Override
    public void execute() {
        try {
            oldValue = director.getFieldValue(field);
            director.setField(field, newValue);
        } catch (ModelDirectorException e) {
            logger.info("Cannot execute command: " + this, e);
        }
    }

    @Override
    public void undo() {
        try {
            director.setField(field, oldValue);
        } catch (ModelDirectorException e) {
            logger.info("Cannot execute command: " + this, e);
        }
    }

    @Override
    public boolean isUndoable() {
        return false;
    }

    @Override
    public String toString() {
        return "DirectedSetCommand{"
                + "director=" + director
                + ", field=" + field
                + ", oldValue=" + oldValue
                + ", newValue=" + newValue
                + '}';
    }
}
