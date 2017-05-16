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
 * RemovePropertyCommand removes property from a {@link org.perfcake.ide.core.model.Model}.
 *
 * @author Jakub Knetl
 */
public class RemovePropertyCommand extends ModelPropertyCommand {

    static final Logger logger = LoggerFactory.getLogger(RemovePropertyCommand.class);

    public RemovePropertyCommand(Model model, Property property, String propertyName) {
        super(model, property, propertyName);
    }

    public RemovePropertyCommand(Model model, Property property, PropertyInfo propertyInfo) {
        super(model, property, propertyInfo);
    }

    @Override
    public void execute() throws CommandException {
        PropertyInfo i = model.getSupportedProperty(propertyName);
        if (i != null) {
            try {
                model.removeProperty(i, property);
            } catch (PropertyLimitException | UnsupportedPropertyException e) {
                throw new CommandException("Cannot remove property.", e);
            }
        } else {
            logger.warn("Model does not support such property: {}!", propertyName);
        }
    }

    @Override
    public void undo() {
        model.addProperty(propertyName, property);
    }
}
