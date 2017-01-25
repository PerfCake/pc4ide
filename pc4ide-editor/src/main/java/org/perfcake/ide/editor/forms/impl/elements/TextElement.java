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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import org.perfcake.ide.core.Field;
import org.perfcake.ide.core.command.Command;
import org.perfcake.ide.core.command.impl.DirectedSetCommand;
import org.perfcake.ide.core.model.director.ModelDirector;

/**
 * Represents form element which expects text data.
 *
 * @author jknetl
 */
public class TextElement extends FieldElement {

    /**
     * Create new text element.
     * @param director Model director of managed object
     * @param field field of the managed object
     */
    public TextElement(ModelDirector director, Field field) {
        super(director, field);
        createMainComponent();
    }

    @Override
    void createMainComponent() {
        this.component = new JTextField(String.valueOf(director.getFieldValue(field)));
        component.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                JTextField tf = (JTextField) component;
                Command command = new DirectedSetCommand(director, field, ((JTextField) component).getText());
                command.execute();
            }
        });
    }

}
