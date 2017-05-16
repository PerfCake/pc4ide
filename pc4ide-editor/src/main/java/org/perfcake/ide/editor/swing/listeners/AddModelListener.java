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
import org.perfcake.ide.core.command.AddPropertyCommand;
import org.perfcake.ide.core.command.Command;
import org.perfcake.ide.core.model.AbstractModel;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.PropertyInfo;
import org.perfcake.ide.core.model.factory.ModelFactory;
import org.perfcake.ide.editor.form.FormController;
import org.perfcake.ide.editor.form.impl.ComponentSelctorFormController;
import org.perfcake.ide.editor.form.impl.FormControllerImpl;

/**
 * Listeners which adds a model.
 *
 * @author Jakub Knetl
 */
public class AddModelListener extends AddPropertyListener {

    /**
     * Creates new AddPropertyListener.
     *
     * @param controller   controller which owns model from which property should be removed.
     * @param propertyInfo metadata about property.
     */
    public AddModelListener(FormController controller, PropertyInfo propertyInfo) {
        super(controller, propertyInfo);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Model model = super.createProperty().cast(Model.class);
        Command command = new AddPropertyCommand(controller.getModel(), model, propertyInfo);
        controller.getFormManager().getCommandInvoker().executeCommand(command);


        ModelFactory modelFactory = controller.getModelFactory();
        if (model.getSupportedProperty(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY) != null) {
            FormController modelController = new FormControllerImpl(model, modelFactory);
            FormController implChooserController = new ComponentSelctorFormController(model, modelFactory);
            controller.getFormManager().addPages(modelController, implChooserController);
        } else {
            FormController modelController = new FormControllerImpl(model, modelFactory);
            controller.getFormManager().addPages(modelController);
        }
    }
}
