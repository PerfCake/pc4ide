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

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.PropertyInfo;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.components.ScenarioModel.PropertyNames;
import org.perfcake.ide.core.model.factory.ModelFactory;
import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.controller.RootController;
import org.perfcake.ide.editor.controller.visitor.MouseClickVisitor;
import org.perfcake.ide.editor.forms.FormManager;
import org.perfcake.ide.editor.view.factory.ViewFactory;
import org.perfcake.ide.editor.view.impl.ScenarioView;


/**
 * Controller of the whole scenario. It is effectively controller of whole editor.
 */
public class ScenarioController extends AbstractController implements RootController {

    private JComponent jComponent;
    private FormManager formManager;

    /**
     * Creates new editor controller.
     *
     * @param jComponent   Swing inspector used as a container for editor visuals
     * @param model        model of scenario managed by controller
     * @param modelFactory model factory.
     * @param viewFactory  Factory for creating views
     * @param formManager  manager of forms to modify inspector properties
     */
    public ScenarioController(JComponent jComponent, ScenarioModel model, ModelFactory modelFactory,
                              ViewFactory viewFactory, FormManager formManager) {
        super(model, modelFactory, viewFactory);
        this.jComponent = jComponent;
        this.formManager = formManager;
        ScenarioView scenarioView = (ScenarioView) view;
        scenarioView.setJComponent(jComponent);
        this.view = scenarioView;

        createChildrenControllers();
    }

    @Override
    public boolean updateViewData() {
        // do nothing, editor view has no data, it has only children views
        return true;
    }

    @Override
    protected void initActionHandlers() {
        // no handlers
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        Point2D point = new Point2D.Double(e.getX(), e.getY());
        MouseClickVisitor selectVisitor = new MouseClickVisitor(point, formManager);
        selectVisitor.visit(this);
    }

    @Override
    public JComponent getJComponent() {
        return this.jComponent;
    }

    @Override
    public Controller getParent() {
        return null;
    }

    @Override
    public Controller createChildController(Model model) {
        Controller child = super.createChildController(model);

        if (child == null) {
            PropertyInfo info = model.getPropertyInfo();

            if (PropertyNames.REPORTERS.toString().equals(info.getName())) {
                child = new ReporterController(model, modelFactory, viewFactory);
            } else if (PropertyNames.SEQUENCES.toString().equals(info.getName())) {
                child = new SequenceController(model, modelFactory, viewFactory);
            } else if (PropertyNames.SENDER.toString().equals(info.getName())) {
                child = new SenderController(model, modelFactory, viewFactory);
            } else if (PropertyNames.GENERATOR.toString().equals(info.getName())) {
                child = new GeneratorController(model, modelFactory, viewFactory);
            } else if (PropertyNames.MESSAGES.toString().equals(info.getName())) {
                child = new MessageController(model, modelFactory, viewFactory);
            } else if (PropertyNames.VALIDATORS.toString().equals(info.getName())) {
                child = new ValidatorController(model, modelFactory, viewFactory);
            } else if (PropertyNames.RECEIVER.toString().equals(info.getName())) {
                child = new ReceiverController(model, modelFactory, viewFactory);
            }
        }

        return child;

    }
}
