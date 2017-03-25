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

package org.perfcake.ide.editor.form.elements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import org.perfcake.ide.core.exception.UnsupportedPropertyException;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.PropertyInfo;
import org.perfcake.ide.core.model.properties.SimpleValue;
import org.perfcake.ide.core.model.properties.Value;
import org.perfcake.ide.editor.form.FormEvent;
import org.perfcake.ide.editor.form.event.FormEventImpl;

/**
 * Simple value element represents a simple Text field property.
 *
 * @author Jakub Knetl
 */
public class SimpleValueElement extends AbstractElement implements ActionListener {

    private JTextField field;

    /**
     * Creates simple value element.
     * @param field text field.
     */
    public SimpleValueElement(JTextField field) {
        super();
        this.field = field;
        field.addActionListener(this);
    }

    @Override
    public String getValue() {
        return field.getText();
    }

    @Override
    public void setValue(String value) {
        field.setText(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fireEvent(new FormEventImpl(FormEvent.Type.MODIFY, this, e));
    }
}
