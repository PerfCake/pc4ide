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

import org.perfcake.ide.core.exception.CommandException;
import org.perfcake.ide.core.exception.PropertyLimitException;
import org.perfcake.ide.core.exception.UnsupportedPropertyException;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.PropertyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AddPropertyCommand adds a property to a model.
 *
 * @author Jakub Knetl
 */
public class AddPropertyCommand extends ModelPropertyCommand {

    static final Logger logger = LoggerFactory.getLogger(AddPropertyCommand.class);

    public AddPropertyCommand(Model model, Property property, String propertyName) {
        super(model, property, propertyName);
    }

    public AddPropertyCommand(Model model, Property property, PropertyInfo propertyInfo) {
        super(model, property, propertyInfo);
    }

    @Override
    public void execute() throws CommandException {
        try {
            model.addProperty(propertyName, property);
        } catch (UnsupportedPropertyException | PropertyLimitException e) {
            throw new CommandException("Cannot execute command", e);
        }
    }

    @Override
    public void undo() {
        PropertyInfo i = model.getSupportedProperty(propertyName);
        model.removeProperty(i, property);
    }
}
