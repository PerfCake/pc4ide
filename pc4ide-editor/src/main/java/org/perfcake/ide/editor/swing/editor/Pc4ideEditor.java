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
import javax.swing.JSplitPane;
import org.perfcake.ide.core.command.invoker.CommandInvoker;
import org.perfcake.ide.core.command.invoker.CommandInvokerImpl;
import org.perfcake.ide.core.components.ComponentCatalogue;
import org.perfcake.ide.core.components.ReflectionComponentCatalogue;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.factory.ModelFactory;
import org.perfcake.ide.core.model.factory.ValidModelFactory;
import org.perfcake.ide.editor.form.FormManager;
import org.perfcake.ide.editor.form.impl.FormManagerImpl;

/**
 * Represents an pc4ide editor as a whole. It consists of graphical editor panel and the panel with form.
 *
 * @author jknelt
 */
public class Pc4ideEditor extends JSplitPane {

    private ScenarioModel scenario;
    private GraphicalPanel graphicalEditorPanel;

    public Pc4ideEditor(ScenarioModel scenario) {
        this(scenario, null);
    }

    /**
     * Creates new editor panel.
     *
     * @param scenario           Scenario edited by editor
     * @param componentCatalogue PerfCake inspector manager
     */
    public Pc4ideEditor(ScenarioModel scenario, ComponentCatalogue componentCatalogue) {
        super(JSplitPane.HORIZONTAL_SPLIT);
        // final BorderLayout layout = new BorderLayout();
        // setLayout(layout);
        this.scenario = scenario;

        // init invoker
        CommandInvoker commandInvoker = new CommandInvokerImpl();

        // init catalogue
        if (componentCatalogue == null) {
            componentCatalogue = createComponentCatalogue();
        }


        ModelFactory modelFactory = new ValidModelFactory(scenario.getDocsService());
        FormManager formManager = new FormManagerImpl(scenario, commandInvoker, componentCatalogue, modelFactory);

        graphicalEditorPanel = new GraphicalPanel(scenario, commandInvoker, formManager);
        formManager.setGraphicalController(graphicalEditorPanel.getScenarioController());

        // final FormPage generatorPage = new SimpleFormPage(formManager,
        // new ReflectiveModelDirector(scenario.getGenerator(), componentCatalogue));
        // formManager.addFormPage(generatorPage);

        setLeftComponent(graphicalEditorPanel);
        setRightComponent(formManager.getMasterPanel());

        // setDividerLocation(getWidth() - 200);
        // this.add(graphicalEditorPanel, BorderLayout.CENTER);
        // this.add(formPanel, BorderLayout.LINE_END);
        // formPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                //Dimension panelSize = getSize();
                //Dimension leftPreferredSize = new Dimension((int) (panelSize.getWidth() * 0.8), (int) panelSize.getHeight());
                //Dimension rightPreferredSize = new Dimension((int) (panelSize.getWidth() * 0.8), (int) panelSize.getHeight());
                //leftComponent.setPreferredSize(leftPreferredSize);
                //rightComponent.setPreferredSize(rightPreferredSize);
                setDividerLocation(0.7);
                super.componentResized(e);
            }
        });
    }

    private ComponentCatalogue createComponentCatalogue() {
        return new ReflectionComponentCatalogue();
    }
}
