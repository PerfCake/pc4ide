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

package org.perfcake.ide.editor.form.impl;

import javax.swing.JPanel;
import org.perfcake.ide.core.model.AbstractModel;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.factory.ModelFactory;
import org.perfcake.ide.core.model.properties.Value;

/**
 * Component selector form controller manages a choosing implementation of some component.
 *
 * @author Jakub Knetl
 */
public class ComponentSelctorFormController extends AbstractFormController {

    public ComponentSelctorFormController(Model model, ModelFactory modelFactory) {
        super(model, modelFactory);
    }

    @Override
    public void drawForm() {
        JPanel panel = formManager.getContentPanel();
        formBuilder.buildForm(panel, model.getSingleProperty(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY, Value.class), this);
    }
}
