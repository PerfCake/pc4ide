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

package org.perfcake.ide.editor.forms.impl.elements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComboBox;

import org.perfcake.ide.core.command.Command;
import org.perfcake.ide.core.command.impl.DirectedSetCommand;
import org.perfcake.ide.core.model.director.ModelDirector;
import org.perfcake.ide.core.model.director.ModelField;

/**
 * Represents a choice in a form.
 */
public class ChoiceElement extends FieldElement {

    private List<String> values;

    /**
     * Creates new choice element.
     *
     * @param director Director of managed model
     * @param field field of the model which is represented as a choice
     * @param values possible values for the field
     */
    public ChoiceElement(ModelDirector director, ModelField field, List<String> values) {
        super(director, field);
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("values must not be neither null or empty.");
        }
        this.values = values;

        createMainComponent();
    }

    @Override
    void createMainComponent() {
        JComboBox<String> comboBox = new JComboBox<>(values.toArray(new String[values.size()]));
        comboBox.setSelectedItem(director.getFieldValue(field));
        this.component = comboBox;

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Command command = new DirectedSetCommand(director, field, String.valueOf(comboBox.getSelectedItem()));
                command.execute();
            }
        });

    }

}
