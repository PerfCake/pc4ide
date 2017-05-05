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
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.factory.ModelFactory;
import org.perfcake.ide.editor.actions.handlers.AddNewComponentHandler;
import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.swing.icons.control.PlusIcon;
import org.perfcake.ide.editor.view.factory.ViewFactory;
import org.perfcake.ide.editor.view.impl.ComponentAddView;

/**
 * Controller of a view which servers as factory for components.
 *
 * @author Jakub Knetl
 */
public class FactoryController extends AbstractController {

    private ScenarioModel scenarioModel;

    /**
     * Creates abstract controller which will manage a model.
     *
     * @param model        model to be managed
     * @param modelFactory model factory.
     * @param viewFactory  viewFactory which may be used to create views.
     */
    public FactoryController(Model model, ModelFactory modelFactory, ViewFactory viewFactory) {
        super(model, modelFactory, viewFactory);
        view = new ComponentAddView(new PlusIcon(32,32));
    }

    @Override
    public boolean updateViewData() {
        return false;
    }

    @Override
    public void subscribeToDebugManager(ExecutionManager manager) {
        // do nothing
    }

    @Override
    protected void initActionHandlers() {
        addActionHandler(new AddNewComponentHandler());
    }
}
