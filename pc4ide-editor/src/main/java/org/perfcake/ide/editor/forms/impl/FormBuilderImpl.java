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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.perfcake.ide.editor.forms.FormBuilder;
import org.perfcake.ide.editor.forms.FormElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements a {@link FormBuilder} interface.
 */
public class FormBuilderImpl implements FormBuilder {

    private static final double WEIGHT_OF_MAIN_COLUMN = 0.6;
    private static final double WEIGHT_OF_OTHER_COLUMNS = 0.2;

    static final Logger logger = LoggerFactory.getLogger(FormBuilderImpl.class);

    private JPanel panel;
    private GridBagLayout layout;

    // represents how many components are in the layout
    private int maxComponents;

    // column with a main inspector will be expanded
    private int mainComponentIndex;

    // weight of the column except the main
    private double weightOfColumn;

    // number of columns
    private int columns;

    private int nextLine = 0;

    private List<FormElement> elements;


    /**
     * Creates new form builder instance.
     *
     * @param panel panel where form will be created
     * @param maxComponents maximum number of components
     * @param columns number of columns
     * @param mainComponentIndex index of main column (must be less than number of columns)
     */
    public FormBuilderImpl(JPanel panel, int maxComponents, int columns, int mainComponentIndex) {
        if (panel == null) {
            throw new IllegalArgumentException("Panel must not be null");
        }
        if (maxComponents <= 0) {
            throw new IllegalArgumentException("Maximum number of elements must be positive");
        }
        if (mainComponentIndex < 0 || mainComponentIndex >= maxComponents) {
            throw new IllegalArgumentException("Main column must be in range [0,maxComponents).");
        }

        this.columns = columns;
        this.panel = panel;
        this.maxComponents = maxComponents;
        this.mainComponentIndex = mainComponentIndex;

        this.elements = new ArrayList<>();
        layout = new GridBagLayout();
        if (maxComponents > 1) {
            this.weightOfColumn = WEIGHT_OF_OTHER_COLUMNS / (maxComponents - 1);
        } else {
            //there is only one column so that it must be the main one
            weightOfColumn = WEIGHT_OF_MAIN_COLUMN;
        }

        panel.setLayout(layout);
    }

    @Override
    public void addElement(FormElement element) {
        elements.add(element);

        final int componentsInElement = element.getGraphicalComponents().size();

        // integer division rounded up
        // int mod = (maxComponents % columns);
        // int emptyCellsInLastRow = (mod == 0) ? 0 : columns - mod;
        int neededRows = (maxComponents + (columns - 1)) / columns;


        int i = 0;
        for (final JComponent component : element.getGraphicalComponents()) {
            if (i == maxComponents) {
                logger.warn("Form elements contains {} components while the Form can handle only {}."
                        + " Some components will not be displayed", componentsInElement, maxComponents);
                break;
            }

            int x = 0;

            // compute x position
            if (i != 0 && columns > 1) {
                x = (x % (columns - 1)) + 1;
            }

            int y = 0;
            //compute y position
            if ((i / columns) != 0 && columns > 1) {
                y = (i - 1) / (columns - 1);
            }


            final GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = x;
            constraints.gridy = nextLine + y;
            constraints.anchor = GridBagConstraints.LINE_START;
            constraints.weightx = weightOfColumn;
            constraints.fill = GridBagConstraints.HORIZONTAL;

            if (i == 0) {
                // constraints.anchor = GridBagConstraints.LINE_cEND;
                constraints.fill = GridBagConstraints.NONE;
                constraints.insets = new Insets(0, 5, 0, 0);
            }

            if (i == mainComponentIndex) {
                constraints.weightx = WEIGHT_OF_MAIN_COLUMN;
            }

            // if there is not enough components then span main one over free components
            if (i == element.getMainComponent()) {
                if (componentsInElement < maxComponents) {
                    constraints.gridwidth = maxComponents - componentsInElement;
                    i += (constraints.gridwidth - 1);
                }
            }

            panel.add(component, constraints);
            i++;
        }
        nextLine += neededRows;
    }

    @Override
    public boolean removeElement(FormElement element) {
        final boolean removed = elements.remove(element);
        if (removed) {
            for (final JComponent component : element.getGraphicalComponents()) {
                panel.remove(component);
            }
        }

        return removed;
    }

}
