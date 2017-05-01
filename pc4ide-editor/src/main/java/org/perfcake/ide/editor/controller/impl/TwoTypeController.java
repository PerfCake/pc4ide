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

import java.util.List;
import org.perfcake.ide.core.model.factory.ModelFactory;
import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.view.View;
import org.perfcake.ide.editor.view.factory.ViewFactory;
import org.perfcake.ide.editor.view.impl.LayeredView;

/**
 * Two type controller is able to manage multiple controllers. However, the controllers must be subtypes of two specific controllers.
 *
 * @author Jakub Knetl
 */
public class TwoTypeController extends CompositeController {

    private Class<? extends View> type1;
    private Class<? extends View> type2;

    public TwoTypeController(ModelFactory modelFactory, ViewFactory viewFactory, Class<? extends View> type1,
                             Class<? extends View> type2, Controller... controllers) {
        super(modelFactory, viewFactory, controllers);
        this.type1 = type1;
        this.type2 = type2;
    }

    public TwoTypeController(List<Controller> controllers, ModelFactory modelFactory, ViewFactory viewFactory,
                             Class<? extends View> type1, Class<? extends View> type2) {
        super(controllers, modelFactory, viewFactory);
        this.type1 = type1;
        this.type2 = type2;
    }

    @Override
    protected View createView() {
        return new LayeredView(type1, type2);
    }
}
