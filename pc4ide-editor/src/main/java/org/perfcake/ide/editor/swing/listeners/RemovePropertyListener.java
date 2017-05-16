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
import org.perfcake.ide.core.command.RemovePropertyCommand;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.editor.form.FormController;

/**
 * Remove property listeners removes property when it is invoked.
 *
 * @author Jakub Knetl
 */
public class RemovePropertyListener implements ActionListener {

    protected FormController controller;
    protected Property property;

    /**
     * Creates new RemovePropertyListener.
     *
     * @param controller controller of the form
     * @param property   property
     */
    public RemovePropertyListener(FormController controller, Property property) {
        if (controller == null) {
            throw new IllegalArgumentException("Controller cannot be null");
        }
        if (property == null) {
            throw new IllegalArgumentException("propeprty cannot be null");
        }
        this.controller = controller;
        this.property = property;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Command command = new RemovePropertyCommand(controller.getModel(), property, property.getPropertyInfo().getName());
        controller.getFormManager().getCommandInvoker().executeCommand(command);
    }
}
