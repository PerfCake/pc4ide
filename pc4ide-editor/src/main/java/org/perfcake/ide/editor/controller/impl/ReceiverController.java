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
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.components.ReceiverModel;
import org.perfcake.ide.core.model.factory.ModelFactory;
import org.perfcake.ide.core.model.properties.Value;
import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.view.factory.ViewFactory;
import org.perfcake.ide.editor.view.impl.ReceiverView;

/**
 * Receiver controller.
 *
 * @author Jakub Knetl
 */
public class ReceiverController extends AbstractController {

    /**
     * Creates abstract controller which will manage a model.
     *
     * @param model        model to be managed
     * @param modelFactory model factory.
     * @param viewFactory  viewFactory which may be used to create views.
     */
    public ReceiverController(Model model, ModelFactory modelFactory, ViewFactory viewFactory) {
        super(model, modelFactory, viewFactory);
        updateViewData();

        Model correlator = model.getSingleProperty(ReceiverModel.PropertyNames.CORRELATOR.toString(), Model.class);

        createChildrenControllers();
    }

    @Override
    public boolean updateViewData() {
        ReceiverView view = (ReceiverView) this.view;
        boolean modified = false; // was view modified during execution of this method?

        Value implementation = model.getSingleProperty(ReceiverModel.PropertyNames.IMPLEMENTATION.toString(), Value.class);
        Value threads = model.getSingleProperty(ReceiverModel.PropertyNames.THREADS.toString(), Value.class);
        Value source = model.getSingleProperty(ReceiverModel.PropertyNames.SOURCE.toString(), Value.class);

        if (implementation != null && !implementation.getValue().equals(view.getHeader())) {
            view.setHeader(implementation.getValue());
            modified = true;
        }

        if (source != null && !source.getValue().equals(view.getSource())) {
            view.setSource(source.getValue());
            modified = true;
        }

        if (threads != null && !threads.getValue().equals(view.getThreads())) {
            view.setThreads(threads.getValue());
            modified = true;
        }

        return modified;
    }

    @Override
    public Controller createChildController(Model model) {
        Controller child = super.createChildController(model);

        if (child == null) {
            if (ReceiverModel.PropertyNames.CORRELATOR.toString().equals(model.getPropertyInfo().getName())) {
                child = new CorrelatorController(model, modelFactory, viewFactory);
            }
        }

        return child;
    }

    @Override
    public void subscribeToDebugManager(ExecutionManager manager) {
        //do not listen to debug event
    }
}
