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

package org.perfcake.ide.editor.forms.impl;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.perfcake.ide.core.components.ComponentCatalogue;
import org.perfcake.ide.editor.forms.FormManager;
import org.perfcake.ide.editor.forms.FormPage;

/**
 * Basic implementation of the FormManager.
 *
 * @author jknetl
 */
public class FormManagerImpl implements FormManager {

    private List<FormPage> pages;
    private FormPage currentPage;
    private JPanel container;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JPanel controlPanel;
    private ComponentCatalogue componentManager;

    /**
     * Creates new form manager.
     *
     * @param componentManager inspector manager
     */
    public FormManagerImpl(ComponentCatalogue componentManager) {
        super();
        pages = new ArrayList<FormPage>();

        createHeaderPanel();

        createControlPanel();

        createContainerPanel();

        container.add(headerPanel, BorderLayout.PAGE_START);
        container.add(controlPanel, BorderLayout.PAGE_END);


        this.componentManager = componentManager;
    }

    @Override
    public boolean isValid() {
        if (pages.isEmpty()) {
            return false;
        }

        boolean isValid = true;

        for (final FormPage page : pages) {
            if (!page.isValid()) {
                isValid = false;
                break;
            }
        }
        return isValid;
    }

    @Override
    public ComponentCatalogue getComponentManager() {
        return componentManager;
    }

    @Override
    public JPanel getContainerPanel() {
        return container;
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public void addFormPage(FormPage page) {
        if (page == null) {
            throw new IllegalArgumentException("Page cannot be null");
        }

        // if page is first page then it should be immediately displayed
        if (pages.isEmpty()) {
            container.add(page.getContentPanel(), BorderLayout.CENTER);
            container.updateUI();
            currentPage = page;
        }

        pages.add(page);
    }

    @Override
    public boolean removePage(FormPage page) {
        //TODO(jknetl) handle the situation when the page is actually displayed!
        if (currentPage == page) {
            container.remove(page.getContentPanel());
        }
        return pages.remove(page);
    }

    @Override
    public void removeAllPages() {
        Iterator<FormPage> it = pages.iterator();
        while (it.hasNext()) {
            FormPage p = it.next();
            if (currentPage == p) {
                container.remove(p.getContentPanel());
            }
            it.remove();
        }
    }

    @Override
    public List<FormPage> getFormPages() {
        return null;
    }

    @Override
    public void applyChanges() {
        // TODO Auto-generated method stub
    }

    /**
     * Creates top-level JPanel for whole Form. This method is called from constructor.
     */
    protected void createContainerPanel() {
        container = new JPanel();
        container.setLayout(new BorderLayout());
    }

    /**
     * Creates JPanel containing control buttons for the form. This method is called from constructor.
     */
    protected void createControlPanel() {
        controlPanel = new JPanel();
        final JButton applyButton = new JButton("Apply");
        controlPanel.add(applyButton);
    }

    /**
     * Creates Jpanel with header of the form. This method is called from the constructor.
     */
    protected void createHeaderPanel() {
        headerPanel = new JPanel();
        final JLabel headerLabel = new JLabel("this is the header");
        headerPanel.add(headerLabel);
    }

}
