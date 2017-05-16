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
import org.perfcake.ide.core.components.ComponentLoader;
import org.perfcake.ide.core.components.PerfCakeComponent;
import org.perfcake.ide.core.exec.ExecutionManager;
import org.perfcake.ide.core.exec.MBeanSubscription;
import org.perfcake.ide.core.model.AbstractModel;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.components.DestinationModel;
import org.perfcake.ide.core.model.factory.ModelFactory;
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
     * @param model        model to be managed
     * @param modelFactory model factory.
     * @param viewFactory  viewFactory which may be used to create views.
     */
    public DestinationController(Model model, ModelFactory modelFactory, ViewFactory viewFactory) {
        super(model, modelFactory, viewFactory);
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

    @Override
    public void subscribeToDebugManager(ExecutionManager manager) {

        ComponentLoader componentLoader = getRoot().getServiceManager().getComponentLoader();
        String destinationName = getModel().getSingleProperty(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY, Value.class).getValue();
        String reporterName = getModel().getModel().getSingleProperty(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY, Value.class).getValue();
        Class<?> reporter = componentLoader.loadComponent(reporterName, PerfCakeComponent.REPORTER);
        Class<?> destination = componentLoader.loadComponent(destinationName, PerfCakeComponent.DESTINATION);

        String mbean = manager.createCounterMBeanQuery("Reporting", reporter.getCanonicalName(), destination.getCanonicalName());

        manager.addListener(this, new MBeanSubscription(mbean));
    }
}
