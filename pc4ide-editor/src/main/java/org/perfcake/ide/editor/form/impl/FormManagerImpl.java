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

package org.perfcake.ide.editor.form.impl;

import java.util.Stack;
import javax.swing.JPanel;
import org.perfcake.ide.core.components.ComponentCatalogue;
import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.form.FormBuilder;
import org.perfcake.ide.editor.form.FormController;
import org.perfcake.ide.editor.form.FormManager;
import org.perfcake.ide.editor.form.builder.FormBuilderImpl;
import org.perfcake.ide.editor.swing.DefaultSwingFactory;
import org.perfcake.ide.editor.swing.SwingFactory;

/**
 * Default implementation of Form Manager.
 *
 * @author Jakub Knetl
 */
public class FormManagerImpl implements FormManager {

    private JPanel contentPanel;
    private ComponentCatalogue componentCatalogue;
    private Stack<FormController> controllers;
    private SwingFactory swingFactory;
    private FormBuilder formBuilder;

    /**
     * Creates new Form manager.
     * @param componentCatalogue catalogue of PerfCake components
     */
    public FormManagerImpl(ComponentCatalogue componentCatalogue) {
        this.componentCatalogue = componentCatalogue;
        swingFactory = new DefaultSwingFactory();
        contentPanel = swingFactory.createPanel();
        formBuilder = new FormBuilderImpl();
        controllers = new Stack<>();
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public void cleanContentPanel() {
        contentPanel.removeAll();
    }

    @Override
    public void addPage(FormController controller) {
        cleanContentPanel();
        controllers.push(controller);
        controller.setFormManager(this);
        controller.setFormBuilder(formBuilder);
        controller.drawForm();
        getContentPanel().updateUI();
    }

    @Override
    public boolean removePage() {
        boolean removed = false;
        if (!controllers.isEmpty()) {
            cleanContentPanel();
            FormController removedController = controllers.pop();
            removedController.setFormManager(null);
            removed = true;
            FormController c = getCurrentPageController();
            if (c != null) {
                c.drawForm();
            }
            //TODO(jknetl): dispose bindings between form elements
        }

        return  removed;
    }

    @Override
    public void removeAllPages() {
        cleanContentPanel();
        while (!controllers.isEmpty()) {
            controllers.pop();
        }
        //TODO(jknetl): dispose bindings between form elements
    }

    @Override
    public int getNumOfPages() {
        return controllers.size();
    }

    @Override
    public FormController getCurrentPageController() {
        if (controllers.isEmpty()) {
            return null;
        } else {
            return controllers.peek();
        }
    }

    @Override
    public ComponentCatalogue getComponentCatalogue() {
        return componentCatalogue;
    }

}
