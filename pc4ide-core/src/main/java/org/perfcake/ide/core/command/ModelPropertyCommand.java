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

import org.perfcake.ide.core.exception.PropertyLimitException;
import org.perfcake.ide.core.exception.UnsupportedPropertyException;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.PropertyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ModelProperty commands represents modifying model properties.
 *
 * @author Jakub Knetl
 */
public abstract class ModelPropertyCommand implements Command {

    static final Logger logger = LoggerFactory.getLogger(ModelPropertyCommand.class);

    protected Model model;
    protected Property property;
    protected String propertyName;
    protected boolean undoable = true;

    /**
     * Creates new Add property command.
     *
     * @param model        model to which property will be added
     * @param property     property to be added
     * @param propertyName name of the property
     */
    public ModelPropertyCommand(Model model, Property property, String propertyName) {
        if (model == null) {
            throw new IllegalArgumentException("Model is null.");
        }
        if (property == null) {
            throw new IllegalArgumentException("Property is null");
        }
        if (propertyName == null) {
            throw new IllegalArgumentException("Property name is null.");
        }
        this.model = model;
        this.property = property;
        this.propertyName = propertyName;
    }

    /**
     * Creates new Add property command.
     *
     * @param model        model to which property will be added
     * @param property     property to be added
     * @param propertyInfo metadata of a model supported property
     */
    public ModelPropertyCommand(Model model, Property property, PropertyInfo propertyInfo) {
        this(model, property, propertyInfo.getName());
    }

    @Override
    public boolean isUndoable() {
        return undoable;
    }
}
