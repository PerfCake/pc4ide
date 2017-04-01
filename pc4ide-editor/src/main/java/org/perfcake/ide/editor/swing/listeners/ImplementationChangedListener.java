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

package org.perfcake.ide.editor.swing.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.perfcake.ide.core.command.Command;
import org.perfcake.ide.core.command.SimplePropertyCommand;
import org.perfcake.ide.core.model.PropertyInfo;
import org.perfcake.ide.core.model.properties.Value;
import org.perfcake.ide.editor.form.FormController;

/**
 * Implementation changed listener changes propertyInfo of implementation property and removes
 * current page (the implementation chooser page) from the form manager.
 *
 * @author Jakub Knetl
 */
public class ImplementationChangedListener implements ActionListener {

    protected FormController controller;
    protected PropertyInfo propertyInfo;
    protected String newImplementation;

    /**
     * Creates new implementation changed listener.
     * @param controller controller of current page
     * @param propertyInfo metadata about implementation property
     * @param newImplementation name of the implementation
     */
    public ImplementationChangedListener(FormController controller, PropertyInfo propertyInfo, String newImplementation) {
        if (controller == null) {
            throw new IllegalArgumentException("controller cannot be null");
        }
        if (propertyInfo == null) {
            throw new IllegalArgumentException("propertyInfo cannot be null");
        }

        if (newImplementation == null) {
            throw new IllegalArgumentException("newImplementation cannot be null");
        }
        this.newImplementation = newImplementation;
        this.controller = controller;
        this.propertyInfo = propertyInfo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Value implementationProperty = propertyInfo.getModel().getSingleProperty(propertyInfo.getName(), Value.class);
        Command command = new SimplePropertyCommand(implementationProperty, newImplementation);
        controller.getFormManager().getCommandInvoker().executeCommand(command);

        controller.getFormManager().removePage();
    }
}
