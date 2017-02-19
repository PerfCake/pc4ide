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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;
import org.perfcake.ide.core.components.ComponentCatalogue;
import org.perfcake.ide.core.components.PerfCakeComponents;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.PropertyInfo;
import org.perfcake.ide.core.model.PropertyType;
import org.perfcake.ide.editor.forms.FormBuilder;
import org.perfcake.ide.editor.forms.FormElement;
import org.perfcake.ide.editor.forms.FormGenerator;
import org.perfcake.ide.editor.forms.FormPageDirector;
import org.perfcake.ide.editor.forms.impl.elements.ChoiceElement;
import org.perfcake.ide.editor.forms.impl.elements.TextElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FormGeneratorImpl reflectively inspects the model of the form and creates
 * graphical control elements based on its inspection.
 *
 * @author jknetl
 */
public class FormGeneratorImpl implements FormGenerator {

    private static final int MAIN_COLUMN = 1;
    private static final int DEFAULT_MAX_COMPONENTS = 3;
    private static final int COLUMNS = 2;

    static final Logger logger = LoggerFactory.getLogger(FormGeneratorImpl.class);

    private Model model;
    private FormPageDirector director;
    private ComponentCatalogue componentCatalogue;
    private JPanel form;

    private FormBuilder formBuilder;

    /**
     * Creates new reflective form generator.
     *
     * @param model              model for which form will be generated
     * @param director           model of the form page
     * @param componentCatalogue PerfCake components catalogue
     * @param form               Swing container of a node.
     */
    public FormGeneratorImpl(Model model, FormPageDirector director, ComponentCatalogue componentCatalogue, JPanel form) {
        super();
        this.model = model;
        this.director = director;
        this.componentCatalogue = componentCatalogue;
        this.form = form;
        formBuilder = new FormBuilderImpl(form, DEFAULT_MAX_COMPONENTS, COLUMNS, MAIN_COLUMN);
    }

    @Override
    public void createForm() {

        final List<FormElement> elements = createFormElements();

        for (final FormElement element : elements) {
            formBuilder.addElement(element);
        }

    }

    private List<FormElement> createFormElements() {

        final List<FormElement> elements = new ArrayList<>();

        final Class<?> modelClazz = model.getClass();

        Set<PropertyInfo> supportedProperties = model.getSupportedProperties();

        for (PropertyInfo propertyInfo : supportedProperties) {


            if (propertyInfo.getType() == PropertyType.VALUE) {
                for (Property p : model.getProperties(propertyInfo)) {
                    FormElement element;
                    if ("clazz".equals(propertyInfo.getName())) {
                        element = new ChoiceElement(model, p, getImplementationNames(model.getApi()));
                    } else {
                        element = new TextElement(model, p);
                    }
                    elements.add(element);
                }
            }

            //TODO: display other types
        }

        return elements;
    }

    private List<String> getImplementationNames(Class<?> modelClazz) {

        PerfCakeComponents component = null;
        for (PerfCakeComponents c : PerfCakeComponents.values()) {
            if (c.getApi().equals(modelClazz)) {
                component = c;
            }

        }
        return componentCatalogue.list(component);

    }
}
