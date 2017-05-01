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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComponent;
import org.perfcake.ide.core.command.invoker.CommandInvoker;
import org.perfcake.ide.core.exec.ExecutionEvent;
import org.perfcake.ide.core.exec.ExecutionManager;
import org.perfcake.ide.core.exec.MBeanSubscription;
import org.perfcake.ide.core.manager.ScenarioManager;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.PropertyInfo;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.components.ScenarioModel.PropertyNames;
import org.perfcake.ide.core.model.factory.ModelFactory;
import org.perfcake.ide.editor.ServiceManager;
import org.perfcake.ide.editor.actions.handlers.DebugHandler;
import org.perfcake.ide.editor.actions.handlers.RunHandler;
import org.perfcake.ide.editor.actions.handlers.SelectionHandler;
import org.perfcake.ide.editor.actions.handlers.StopHandler;
import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.controller.ExecutionFactory;
import org.perfcake.ide.editor.controller.RootController;
import org.perfcake.ide.editor.controller.visitor.MouseClickVisitor;
import org.perfcake.ide.editor.form.FormManager;
import org.perfcake.ide.editor.view.UnsupportedChildViewException;
import org.perfcake.ide.editor.view.factory.ViewFactory;
import org.perfcake.ide.editor.view.impl.DestinationView;
import org.perfcake.ide.editor.view.impl.LayeredView;
import org.perfcake.ide.editor.view.impl.MessageView;
import org.perfcake.ide.editor.view.impl.ReporterView;
import org.perfcake.ide.editor.view.impl.ScenarioView;
import org.perfcake.ide.editor.view.impl.SequenceView;

/**
 * Controller of the whole scenario. It is effectively controller of whole editor.
 */
public class ScenarioController extends AbstractController implements RootController {

    private JComponent jComponent;
    private FormManager formManager;

    private CommandInvoker commandInvoker;
    private ScenarioManager scenarioManager;
    private ExecutionFactory executionFactory;
    private ServiceManager serviceManager;

    private LayeredView messagesAndSequencesView;
    private ExecutionManager executionManager;

    /**
     * Creates new editor controller.
     *
     * @param jComponent       Swing inspector used as a container for editor visuals
     * @param model            Model of the scenario
     * @param scenarioManager  Manager of the scenario
     * @param executionFactory execution manager
     * @param modelFactory     model factory.
     * @param serviceManager   service manger
     * @param viewFactory      Factory for creating views
     * @param commandInvoker   command invoker for executing commands
     * @param formManager      manager of forms to modify inspector properties
     */
    public ScenarioController(JComponent jComponent, ScenarioModel model, ScenarioManager scenarioManager,
                              ExecutionFactory executionFactory, ServiceManager serviceManager, ModelFactory modelFactory,
                              ViewFactory viewFactory, CommandInvoker commandInvoker, FormManager formManager) {
        super(model, modelFactory, viewFactory);
        this.scenarioManager = scenarioManager;
        this.jComponent = jComponent;
        this.formManager = formManager;
        this.executionFactory = executionFactory;
        this.serviceManager = serviceManager;
        ScenarioView scenarioView = (ScenarioView) view;
        scenarioView.setJComponent(jComponent);
        this.commandInvoker = commandInvoker;
        this.view = scenarioView;

        /* add composite view for messages and seqeunces. Warning: this violates hierarchy,
         * because this view will have no controller attached! */
        messagesAndSequencesView = new LayeredView(MessageView.class, SequenceView.class);
        this.getView().addChild(messagesAndSequencesView);

        createChildrenControllers();
    }

    @Override
    public boolean updateViewData() {
        // do nothing, editor view has no data, it has only children views
        return true;
    }

    @Override
    protected void initActionHandlers() {
        addActionHandler(new SelectionHandler());
        addActionHandler(new RunHandler());
        addActionHandler(new DebugHandler());
        addActionHandler(new StopHandler());

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
    public FormManager getFormManger() {
        return formManager;
    }

    @Override
    public ScenarioManager getScenarioManager() {
        return scenarioManager;
    }

    public ExecutionFactory getExecutionFactory() {
        return executionFactory;
    }

    @Override
    public ServiceManager getServiceManager() {
        return serviceManager;
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
                Controller reporter = child;
                List<Controller> composedControllers = new ArrayList<>();
                composedControllers.add(reporter);
                Iterator<Controller> it = reporter.getChildrenIterator();
                while (it.hasNext()) {
                    composedControllers.add(it.next());
                }
                child = new TwoTypeController(composedControllers, modelFactory, viewFactory, ReporterView.class, DestinationView.class);
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

    @Override
    public void addChild(Controller child) throws UnsupportedChildViewException {
        // special case for messages and sequences since they need to be nested into their special parent view
        if (child instanceof MessageController || child instanceof SequenceController) {
            children.add(child);
            child.setParent(this);
            messagesAndSequencesView.addChild(child.getView());
            child.getView().invalidate();
        } else {
            super.addChild(child);
        }
    }

    @Override
    public boolean removeChild(Controller child) {
        // special case for messages and sequences since they need to be removed from their special parent view
        if (child instanceof MessageController || child instanceof SequenceController) {

            final boolean removed = children.remove(child);
            if (removed) {
                messagesAndSequencesView.removeChild(child.getView());
                child.setParent(null);
                messagesAndSequencesView.invalidate();
            }
            return removed;
        } else {
            return super.removeChild(child);
        }
    }

    @Override
    public CommandInvoker getCommandInvoker() {
        return commandInvoker;
    }

    @Override
    public void handleEvent(ExecutionEvent event) {
        super.handleEvent(event);

        ScenarioView scenarioView = (ScenarioView) getView();
        if (event.getType() == ExecutionEvent.Type.STARTED) {
            scenarioView.setRunning(true);
        }
        if (event.getType() == ExecutionEvent.Type.STOPED) {
            scenarioView.setRunning(false);
            this.executionManager = null;  // clear debug manager
        }
    }

    @Override
    public void subscribeToDebugManager(ExecutionManager manager) {
        // subscribe only for high level events
        manager.addListener(this, MBeanSubscription.createEmptySubscription());

        // cached debug manager.
        this.executionManager = manager;
    }

    @Override
    public ExecutionManager getExecutionManager() {
        return executionManager;
    }
}
