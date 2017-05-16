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

package org.perfcake.ide.editor.swing.editor;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import org.perfcake.PerfCakeException;
import org.perfcake.ide.core.command.invoker.CommandInvoker;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.exception.ModelSerializationException;
import org.perfcake.ide.core.exception.Pc4ideException;
import org.perfcake.ide.core.manager.ScenarioManager;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.editor.ServiceManager;
import org.perfcake.ide.editor.controller.ExecutionFactory;
import org.perfcake.ide.editor.controller.RootController;
import org.perfcake.ide.editor.form.FormManager;
import org.perfcake.ide.editor.form.impl.FormManagerImpl;

/**
 * Represents an pc4ide editor as a whole. It consists of graphical editor panel and the panel with form.
 *
 * @author jknelt
 */
public class Pc4ideEditor {

    private JSplitPane contentPanel;
    private ScenarioManager scenarioManager;
    private CommandInvoker commandInvoker;
    private GraphicalPanel graphicalEditorPanel;
    private final FormManager formManager;

    /**
     * Creates new editor panel.
     *
     * @param scenarioManager  Path to the scenarioManager
     * @param executionFactory execution manager
     * @param serviceManager   service manager
     * @param commandInvoker   Command invoker
     * @throws PerfCakeException When it is impossible to load scenario from scenario path
     */
    public Pc4ideEditor(ScenarioManager scenarioManager, ExecutionFactory executionFactory, ServiceManager serviceManager,
                        CommandInvoker commandInvoker) throws PerfCakeException {
        // final BorderLayout layout = new BorderLayout();
        // setLayout(layout);
        this.contentPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this.scenarioManager = scenarioManager;
        this.commandInvoker = commandInvoker;


        final ScenarioModel model;
        try {
            model = (ScenarioModel) scenarioManager.loadScenarioModel();
        } catch (ModelConversionException | ModelSerializationException e) {
            throw new PerfCakeException("Cannot load model of a scenario.", e);
        }


        formManager = new FormManagerImpl(model, commandInvoker, serviceManager.getSwingFactory(),
                serviceManager.getComponentCatalogue(), serviceManager.getModelFactory());

        graphicalEditorPanel = new GraphicalPanel(scenarioManager, model, executionFactory, serviceManager, commandInvoker, formManager);
        formManager.setGraphicalController(graphicalEditorPanel.getController());


        contentPanel.setLeftComponent(graphicalEditorPanel);
        contentPanel.setRightComponent(formManager.getMasterPanel());

        contentPanel.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                contentPanel.setDividerLocation(0.80);
                super.componentResized(e);
            }
        });
    }

    /**
     * Serializes scenario to a file.
     *
     * @throws Pc4ideException when scenario cannot be saved.
     */
    public void save() throws Pc4ideException {
        RootController controller = getGraphicalEditorPanel().getController();
        try {
            controller.getScenarioManager().writeScenario((ScenarioModel) controller.getModel());
        } catch (ModelSerializationException | ModelConversionException e) {
            throw new Pc4ideException("Cannot save scenario: " + controller.getScenarioManager().getScenarioLocation(), e);
        }
    }

    /**
     * (Re)loads scenario from a file.
     *
     * @throws Pc4ideException when scenario cannot be loaded.
     */
    public void load() throws Pc4ideException {

        RootController controller = getGraphicalEditorPanel().getController();
        try {
            ScenarioModel model = controller.getScenarioManager().loadScenarioModel();
            updateModel(model);
        } catch (ModelSerializationException | ModelConversionException e) {
            throw new Pc4ideException("Cannot load scenario: " + controller.getScenarioManager().getScenarioLocation(), e);
        }

    }

    /**
     * Updates model and all dependent controllers.
     *
     * @param model new model instance
     */
    public void updateModel(ScenarioModel model) {
        getGraphicalEditorPanel().setModel(model);
        formManager.setGraphicalController(graphicalEditorPanel.getController());
        formManager.setModel(model);
    }

    public GraphicalPanel getGraphicalEditorPanel() {
        return graphicalEditorPanel;
    }

    public FormManager getFormManager() {
        return formManager;
    }

    public CommandInvoker getCommandInvoker() {
        return commandInvoker;
    }

    public ScenarioManager getScenarioManager() {
        return scenarioManager;
    }

    public JComponent getContentPanel() {
        return this.contentPanel;
    }
}
