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

package org.perfcake.ide.editor.controller;

import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.factory.ModelFactory;
import org.perfcake.ide.editor.view.factory.ViewFactory;

/**
 * Composite controller contains multiple grouped controllers. It delegates call to the contained controllers.
 *
 * @author Jakub Knetl
 */
public abstract class CompositeController extends AbstractController {

    /**
     * Creates abstract controller which will manage a model.
     *
     * @param model        model to be managed
     * @param modelFactory model factory.
     * @param viewFactory  viewFactory which may be used to create views.
     */
    public CompositeController(Model model, ModelFactory modelFactory, ViewFactory viewFactory) {
        super(model, modelFactory, viewFactory);
    }

    @Override
    public boolean updateViewData() {
        return false;
    }
}
