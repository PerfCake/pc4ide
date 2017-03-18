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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.components.MessageModel;
import org.perfcake.ide.core.model.factory.ModelFactory;
import org.perfcake.ide.core.model.properties.Value;
import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.view.factory.ViewFactory;
import org.perfcake.ide.editor.view.impl.MessageView;

/**
 * Message controller.
 *
 * @author Jakub Knetl
 */
public class MessageController extends AbstractController {
    /**
     * Creates abstract controller which will manage a model.
     *
     * @param model        model to be managed
     * @param modelFactory model factory.
     * @param viewFactory  viewFactory which may be used to create views.
     */
    public MessageController(Model model, ModelFactory modelFactory, ViewFactory viewFactory) {
        super(model, modelFactory, viewFactory);
        updateViewData();
    }

    @Override
    public boolean updateViewData() {
        boolean modified = false;
        MessageView view = (MessageView) this.view;
        view.setHeader("Message");
        Value uri = model.getSingleProperty(MessageModel.PropertyNames.URI.toString(), Value.class);
        Value content = model.getSingleProperty(MessageModel.PropertyNames.URI.toString(), Value.class);
        Value multiplicity = model.getSingleProperty(MessageModel.PropertyNames.URI.toString(), Value.class);

        if (uri != null && !uri.getValue().equals(view.getUri())) {
            view.setUri(uri.getValue());
            modified = true;
        }
        if (multiplicity != null && !multiplicity.getValue().equals(view.getMultiplicity())) {
            view.setMultiplicity(multiplicity.getValue());
            modified = true;
        }
        if (content != null && !content.getValue().equals(view.getContent())) {
            view.setContent(content.getValue());
            modified = true;
        }

        List<Property> validatorRefs = model.getProperties(MessageModel.PropertyNames.VALIDATOR_REFS.toString());


        Set<String> modelValidatorRefs = new HashSet<>();
        Set<String> viewValidatorRefs = view.getValidatorRefs();

        for (Property p : validatorRefs) {
            Value v = p.cast(Value.class);
            modelValidatorRefs.add(v.getValue());
        }

        if (!modelValidatorRefs.equals(viewValidatorRefs)) {
            view.setValidatorRefs(modelValidatorRefs);
            modified = true;
        }

        return modified;
    }
}
