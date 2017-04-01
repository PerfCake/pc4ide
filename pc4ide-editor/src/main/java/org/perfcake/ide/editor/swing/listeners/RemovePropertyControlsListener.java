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

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.editor.form.FormController;

/**
 * RemovePropertyControlsListener is a {@link RemovePropertyListener} which additionally removes also GUI controls of the property.
 *
 * @author Jakub Knetl
 */
public class RemovePropertyControlsListener extends RemovePropertyListener {

    private List<JComponent> componentsForRemoval;
    private JPanel panel;

    /**
     * Creates new RemovePropertyListener.
     *
     * @param controller           controller of the form
     * @param property             property
     * @param panel                panel which contains controls of the property
     * @param componentsForRemoval components which should be removed from panel
     */
    public RemovePropertyControlsListener(FormController controller, Property property, JPanel panel, JComponent... componentsForRemoval) {
        super(controller, property);
        this.panel = panel;
        this.componentsForRemoval = Arrays.asList(componentsForRemoval);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);

        for (JComponent c : componentsForRemoval) {
            panel.remove(c);
        }
        panel.revalidate();
        panel.repaint();
    }
}
