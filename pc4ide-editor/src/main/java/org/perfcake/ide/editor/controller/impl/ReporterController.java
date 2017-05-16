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
import org.perfcake.ide.core.components.PerfCakeComponent;
import org.perfcake.ide.core.exec.ExecutionManager;
import org.perfcake.ide.core.exec.MBeanSubscription;
import org.perfcake.ide.core.model.AbstractModel;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.PropertyInfo;
import org.perfcake.ide.core.model.components.ReporterModel;
import org.perfcake.ide.core.model.factory.ModelFactory;
import org.perfcake.ide.core.model.properties.Value;
import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.view.factory.ViewFactory;
import org.perfcake.ide.editor.view.impl.ReporterView;

/**
 * Controller of reporter.
 *
 * @author Jakub Knetl
 */
public class ReporterController extends AbstractController {

    /**
     * Creates new view.
     *
     * @param senderModel  model of sender
     * @param modelFactory model factory.
     * @param viewFactory  view factory
     */
    public ReporterController(Model senderModel, ModelFactory modelFactory, ViewFactory viewFactory) {
        super(senderModel, modelFactory, viewFactory);
        view = viewFactory.createView(senderModel);
        updateViewData();

        List<Property> destinations = model.getProperties(ReporterModel.PropertyNames.DESTINATION.toString());

        createChildrenControllers();
    }

    @Override
    public boolean updateViewData() {
        ReporterView view = (ReporterView) this.view;
        boolean modified = false; // was view modified during execution of this method?

        Value implementation = model.getSingleProperty(ReporterModel.PropertyNames.IMPLEMENTATION.toString(), Value.class);

        if (implementation != null && !implementation.getValue().equals(view.getHeader())) {
            view.setHeader(implementation.getValue());
            modified = true;
        }


        return modified;
    }

    @Override
    public Controller createChildController(Model model) {
        Controller child = super.createChildController(model);

        if (child == null) {
            PropertyInfo info = model.getPropertyInfo();
            if (ReporterModel.PropertyNames.DESTINATION.toString().equals(info.getName())) {
                child = new DestinationController(model, modelFactory, viewFactory);
            }
        }

        return child;
    }

    @Override
    public void subscribeToDebugManager(ExecutionManager manager) {
        String className = getModel().getSingleProperty(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY, Value.class).getValue();
        Class<?> impl = getRoot().getServiceManager().getComponentLoader().loadComponent(className, PerfCakeComponent.REPORTER);

        String mbean = manager.createCounterMBeanQuery("Reporting", impl.getCanonicalName());

        manager.addListener(this, new MBeanSubscription(mbean));

    }
}
