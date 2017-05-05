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
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

/**
 * Value agents is able to create {@link ValueAgent} for a {@link javax.swing.JComponent}
 *
 * @author Jakub Knetl
 */
public class ValueAgents {

    private ValueAgents() {
    }

    /**
     * Creates property agent for given swing component. Only some component types are supported
     *
     * @param component component
     * @return property agent
     * @throws UnsupportedOperationException if component type is not supported
     */
    public static ValueAgent createAgent(JComponent component) throws UnsupportedOperationException {
        if (component == null) {
            throw new IllegalArgumentException("component cannot be null.");
        }
        if (component instanceof JTextComponent) {
            return new TextAgent((JTextComponent) component);
        }

        if (component instanceof JComboBox) {
            return new ComboBoxAgent((JComboBox<String>) component);
        }

        throw new UnsupportedOperationException(String.format("Cannot create ValueAgent for component type %s.",
                component.getClass().getSimpleName()));
    }
}
