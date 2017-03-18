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

import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.components.GeneratorModel;
import org.perfcake.ide.core.model.factory.ModelFactory;
import org.perfcake.ide.core.model.properties.KeyValue;
import org.perfcake.ide.core.model.properties.Value;
import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.view.factory.ViewFactory;
import org.perfcake.ide.editor.view.impl.GeneratorView;

/**
 * Controller of generator model.
 *
 * @author Jakub Knetl
 */
public class GeneratorController extends AbstractController {

    /**
     * Creates abstract controller which will manage a model.
     *
     * @param model        model to be managed
     * @param modelFactory model factory.
     * @param viewFactory  viewFactory which may be used to create views.
     */
    public GeneratorController(Model model, ModelFactory modelFactory, ViewFactory viewFactory) {
        super(model, modelFactory, viewFactory);
        view = viewFactory.createView(model);
        updateViewData();
    }

    @Override
    public boolean updateViewData() {
        GeneratorView view = (GeneratorView) this.view;
        boolean modified = false; // was view modified during execution of this method?

        Value implementation = model.getSingleProperty(GeneratorModel.PropertyNames.IMPLEMENTATION.toString(), Value.class);
        KeyValue modelRun = model.getSingleProperty(GeneratorModel.PropertyNames.RUN.toString(), KeyValue.class);
        Value threads = model.getSingleProperty(GeneratorModel.PropertyNames.THREADS.toString(), Value.class);

        if (implementation != null && !implementation.getValue().equals(view.getHeader())) {
            view.setHeader(implementation.getValue());
            modified = true;
        }

        if (modelRun != null) {
            if (modelRun.getKey() != null && !modelRun.getKey().equals(view.getRunType())) {
                view.setRunType(modelRun.getKey());
                modified = true;
            }
            if (modelRun.getValue() != null && !modelRun.getValue().equals(view.getRunValue())) {
                view.setRunValue(modelRun.getValue());
                modified = true;
            }
        }

        if (threads != null && !threads.getValue().equals(view.getThreads())) {
            view.setThreads(threads.getValue());
            modified = true;
        }


        return modified;
    }
}
