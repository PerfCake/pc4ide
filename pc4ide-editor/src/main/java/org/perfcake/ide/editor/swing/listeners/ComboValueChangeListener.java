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

package org.perfcake.ide.editor.swing.listeners;

import javax.swing.JComboBox;
import org.perfcake.ide.core.command.invoker.CommandInvoker;
import org.perfcake.ide.core.model.Property;

/**
 * Listener for combobox property change.
 *
 * @author Jakub Knetl
 */
public class ComboValueChangeListener extends ValueChangeListener {

    private JComboBox<String> comboBox;

    public ComboValueChangeListener(Property property, KeyValueField keyValueField, CommandInvoker invoker, JComboBox<String> comboBox) {
        super(property, keyValueField, invoker, comboBox);
        this.comboBox = comboBox;
    }

    @Override
    public void subscribeAll() {
        comboBox.addActionListener(this);
    }

    @Override
    public void unsubscribeAll() {
        comboBox.removeActionListener(this);
    }
}
