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
import org.perfcake.ide.core.model.components.DestinationModel;
import org.perfcake.ide.core.model.properties.KeyValue;
import org.perfcake.ide.core.model.properties.Value;
import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.view.Pair;
import org.perfcake.ide.editor.view.factory.ViewFactory;
import org.perfcake.ide.editor.view.impl.DestinationView;

/**
 * Represents controller of destination.
 *
 * @author Jakub Knetl
 */
public class DestinationController extends AbstractController {

    /**
     * Creates abstract controller which will manage a model.
     *
     * @param model       model to be managed
     * @param viewFactory viewFactory which may be used to create views.
     */
    public DestinationController(Model model, ViewFactory viewFactory) {
        super(model, viewFactory);
        updateViewData();
    }

    @Override
    public boolean updateViewData() {

        boolean modified = false;
        DestinationView view = (DestinationView) this.view;
        Value implementation = model.getSingleProperty(DestinationModel.PropertyNames.IMPLEMENTATION.toString(), Value.class);

        if (implementation != null && !implementation.getValue().equals(view.getHeader())) {
            view.setHeader(implementation.getValue());
            modified = true;
        }

        List<Property> periods = model.getProperties(DestinationModel.PropertyNames.PERIOD.toString());

        Set<Pair> modelPeriodPairs = new HashSet<>();
        Set<Pair> viewPeriods = view.getPeriods();

        for (Property p : periods) {
            KeyValue k = p.cast(KeyValue.class);
            modelPeriodPairs.add(new Pair(k.getKey(), k.getValue()));
        }

        if (!modelPeriodPairs.equals(viewPeriods)) {
            view.setPeriods(modelPeriodPairs);
            modified = true;
        }

        return modified;
    }
}
