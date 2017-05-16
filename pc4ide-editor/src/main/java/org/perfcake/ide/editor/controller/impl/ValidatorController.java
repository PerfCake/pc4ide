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

package org.perfcake.ide.editor.controller.impl;

import org.perfcake.ide.core.exec.ExecutionManager;
import org.perfcake.ide.core.exec.MBeanSubscription;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.components.ValidatorModel;
import org.perfcake.ide.core.model.factory.ModelFactory;
import org.perfcake.ide.core.model.properties.Value;
import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.view.factory.ViewFactory;
import org.perfcake.ide.editor.view.impl.ValidatorView;

/**
 * Validator controller.
 *
 * @author Jakub Knetl
 */
public class ValidatorController extends AbstractController {
    /**
     * Creates abstract controller which will manage a model.
     *
     * @param model        model to be managed
     * @param modelFactory model factory.
     * @param viewFactory  viewFactory which may be used to create views.
     */
    public ValidatorController(Model model, ModelFactory modelFactory, ViewFactory viewFactory) {
        super(model, modelFactory, viewFactory);
        updateViewData();
    }

    @Override
    public boolean updateViewData() {
        ValidatorView view = (ValidatorView) this.view;
        boolean modified = false; // was view modified during execution of this method?

        Value implementation = model.getSingleProperty(ValidatorModel.PropertyNames.IMPLEMENTATION.toString(), Value.class);
        Value id = model.getSingleProperty(ValidatorModel.PropertyNames.ID.toString(), Value.class);

        if (implementation != null && !implementation.getValue().equals(view.getHeader())) {
            view.setHeader(implementation.getValue());
            modified = true;
        }


        if (id != null && !id.getValue().equals(view.getId())) {
            view.setId(id.getValue());
            modified = true;
        }

        return modified;
    }

    @Override
    public void subscribeToDebugManager(ExecutionManager manager) {
        String id = getModel().getSingleProperty(ValidatorModel.PropertyNames.ID.toString(), Value.class).getValue();
        String mbean = manager.createCounterMBeanQuery("Validation", id);

        manager.addListener(this, new MBeanSubscription(mbean));
    }
}
