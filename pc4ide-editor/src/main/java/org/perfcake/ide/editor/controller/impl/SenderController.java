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

import java.util.Arrays;
import java.util.List;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.components.SenderModel;
import org.perfcake.ide.core.model.factory.ModelFactory;
import org.perfcake.ide.core.model.properties.Value;
import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.view.factory.ViewFactory;
import org.perfcake.ide.editor.view.impl.SenderView;

/**
 * Controller of a sender.
 *
 * @author Jakub Knetl
 */
public class SenderController extends AbstractController {

    /**
     * Creates new view.
     *
     * @param senderModel  model of sender
     * @param modelFactory model factory.
     * @param viewFactory  view factory
     */
    public SenderController(Model senderModel, ModelFactory modelFactory, ViewFactory viewFactory) {
        super(senderModel, modelFactory, viewFactory);
        view = viewFactory.createView(senderModel);
        updateViewData();
    }

    @Override
    public boolean updateViewData() {
        SenderView view = (SenderView) this.view;
        boolean modified = false; // was view modified during execution of this method?

        Value implementation = model.getSingleProperty(SenderModel.PropertyNames.IMPLEMENTATION.toString(), Value.class);
        Value target = model.getSingleProperty(SenderModel.PropertyNames.TARGET.toString(), Value.class);

        if (implementation != null && !implementation.getValue().equals(view.getHeader())) {
            view.setHeader(implementation.getValue());
            modified = true;
        }


        if (target != null && !target.getValue().equals(view.getTarget())) {
            view.setTarget(target.getValue());
            modified = true;
        }

        return modified;
    }

    @Override
    public List<String> getObjectNameHints() {
        return Arrays.asList("SentMessages");
    }
}
