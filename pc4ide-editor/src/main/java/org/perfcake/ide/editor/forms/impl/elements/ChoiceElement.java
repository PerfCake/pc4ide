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
import org.perfcake.ide.core.command.SimplePropertyCommand;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.properties.Value;

/**
 * Represents a choice in a form.
 */
public class ChoiceElement extends FieldElement {

    private List<String> values;

    /**
     * Creates new choice element.
     *
     * @param model    Director of managed model
     * @param property property of the model which is represented as a choice
     * @param values   possible values for the property
     */
    public ChoiceElement(Model model, Property property, List<String> values) {
        super(model, property);
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("values must not be neither null or empty.");
        }
        this.values = values;

        createMainComponent();
    }

    @Override
    void createMainComponent() {
        JComboBox<String> comboBox = new JComboBox<>(values.toArray(new String[values.size()]));
        comboBox.setSelectedItem(property.cast(Value.class).getValue());
        this.component = comboBox;

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Command command = new SimplePropertyCommand(property.cast(Value.class), String.valueOf(comboBox.getSelectedItem()));
                command.execute();
            }
        });

    }

}
