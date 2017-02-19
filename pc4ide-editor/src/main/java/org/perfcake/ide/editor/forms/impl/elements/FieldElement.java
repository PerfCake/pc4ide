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
/**
 *
 */

package org.perfcake.ide.editor.forms.impl.elements;

import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.editor.forms.FormElement;

/**
 * Abstract class for the element which consists of three components.:
 * <ol>
 * <li>label with name</li>
 * <li>specific inspector defined by subclass</li>
 * <li>hint or help for the user </li>
 * </ol>
 * <p>Subclasses are required to override {@link #createMainComponent()} and call this method  at the end of initialization</p>
 *
 * @author jknetl
 */
public abstract class FieldElement implements FormElement {

    private static final int DEFAULT_DOCS_MIN_WIDTH = 200;

    protected Model model;
    protected Property property;

    protected JLabel label;
    protected JComponent component;
    protected JLabel docsLabel;

    /**
     * Creates a new form property element.
     *
     * @param model model model of managed model
     * @param property    property managed by this element
     */
    public FieldElement(Model model, Property property) {

        this.model = model;
        this.property = property;

        label = new JLabel(property.getPropertyInfo().getDisplayName());

        //TODO(jknetl) change for icon
        String label;
        String docs = property.getPropertyInfo().getDocs();
        if (docs != null && !docs.isEmpty()) {
            label = docs;
        } else {
            label = "Documentation is not available.";
        }
        docsLabel = new JLabel("<html>" + label + "</html>");
        docsLabel.setToolTipText(docs);
    }

    /**
     * Creates main inspector of the element ans stores it into {@link #component} property. This method is
     * not called automatically so the subclass must call this method on its own.
     */
    abstract void createMainComponent();

    @Override
    public List<JComponent> getGraphicalComponents() {
        return Arrays.asList(label, component, docsLabel);
    }

    @Override
    public int getMainComponent() {
        return 1;
    }
}
