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

package org.perfcake.ide.editor.form;

import javax.swing.JPanel;
import org.perfcake.ide.core.components.ComponentCatalogue;
import org.perfcake.ide.core.model.Property;

/**
 * Form builder is responsible for creating form and filling it with proper values.
 *
 * @author Jakub Knetl
 */
public interface FormBuilder {

    /**
     * Creates form and places it into panel.
     *
     * @param panel    panel where form will be created
     * @param property property which will be displayed in the form.
     * @param componentCatalogue catalogue of the components
     */
    void buildForm(JPanel panel, Property property, ComponentCatalogue componentCatalogue);
}
