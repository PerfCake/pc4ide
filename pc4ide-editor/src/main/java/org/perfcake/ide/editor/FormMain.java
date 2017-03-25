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

package org.perfcake.ide.editor;

import java.awt.EventQueue;
import java.io.File;
import java.net.MalformedURLException;
import javax.swing.JFrame;
import org.perfcake.PerfCakeException;
import org.perfcake.ide.core.components.ComponentCatalogue;
import org.perfcake.ide.core.components.ReflectionComponentCatalogue;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.model.AbstractModel;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.components.GeneratorModel;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.loader.ModelLoader;
import org.perfcake.ide.core.model.properties.Value;
import org.perfcake.ide.editor.colors.DefaultColorScheme;
import org.perfcake.ide.editor.form.FormBuilder;
import org.perfcake.ide.editor.form.builder.FormBuilderImpl;
import org.perfcake.ide.editor.swing.editor.FormPanel;
import org.perfcake.ide.editor.swing.editor.Pc4ideEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains main class which displays only the form. It is meant for testing purposes only.
 * @author Jakub Knetl
 */
public class FormMain {
    static final Logger logger = LoggerFactory.getLogger(FormMain.class);

    /**
     * Main method for launching editor
     *
     * @param args arguments
     * @throws PerfCakeException        when scenario cannot be loaded.
     * @throws MalformedURLException    when URL to scenario file is illegal.
     * @throws ModelConversionException when model cannot be converted
     */
    public static void main(String[] args) throws PerfCakeException, MalformedURLException, ModelConversionException {

        if (args.length == 0) {
            System.out.println("No scenario file specified as argument. Exiting");
            System.exit(1);
        }

        logger.info("Application is starting. Loading scenario: {}", args[0]);

        final File scenarioFile = new File(args[0]);
        ModelLoader loader = new ModelLoader();
        final ScenarioModel model = loader.loadModel(scenarioFile.toURI().toURL());

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {

                final JFrame frame = new JFrame();
                frame.setTitle("Perfcake editor");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                // final GraphicalPanel editor = new GraphicalPanel(model);
                final FormPanel formPane = new FormPanel();
                FormBuilder builder = new FormBuilderImpl();

                Model generatorModel = model.getSingleProperty(ScenarioModel.PropertyNames.GENERATOR.toString(), Model.class);
                ComponentCatalogue catalogue = new ReflectionComponentCatalogue();
                //builder.buildForm(formPane.getContentPanel(), generatorModel, catalogue);
                builder.buildForm(formPane.getContentPanel(), generatorModel.getSingleProperty(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY,
                        Value.class), catalogue);
                frame.add(formPane);
                frame.setVisible(true);
            }
        });
    }
}
