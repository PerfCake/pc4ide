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

package org.perfcake.ide.editor;

import java.awt.EventQueue;
import java.io.File;
import java.net.MalformedURLException;
import javax.swing.JFrame;
import org.perfcake.PerfCakeException;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.loader.ModelLoader;
import org.perfcake.ide.editor.swing.EditorJPanel;

/**
 * Main class. This class is intendet for testing purposes only. The program should
 * run inside of IDE instead.
 *
 * @author jknetl
 */
public class Main {

    /**
     * Main method for launching editor
     *
     * @param args arguments
     * @throws PerfCakeException     when scenario cannot be loaded.
     * @throws MalformedURLException when URL to scenario file is illegal.
     * @throws ModelConversionException when model cannot be converted
     */
    public static void main(String[] args) throws PerfCakeException, MalformedURLException, ModelConversionException {
        // TODO Auto-generated method stub

        final File scenarioFile = new File("src/main/resources/scenario/http.xml");
        ModelLoader loader = new ModelLoader();
        final ScenarioModel model = loader.loadModel(scenarioFile.toURI().toURL());

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {

                /*
                final PerfCakeScenarioParser parser = new PerfCakeScenarioParser();
                ScenarioModel model = null;
                try {
                    final Scenario s = parser.parse(new File("src/main/resources/scenario/http.xml").toURI().toURL());
                    model = ModelConverter.getPc4ideModel(s);
                } catch (final PerfCakeException e) {
                    e.printStackTrace();
                } catch (final MalformedURLException e) {
                    e.printStackTrace();
                }
                */

                final JFrame frame = new JFrame();
                frame.setTitle("Perfcake editor");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                // final GraphicalEditorJPanel editor = new GraphicalEditorJPanel(model);
                final EditorJPanel editor = new EditorJPanel(model);
                frame.add(editor);
                frame.setVisible(true);
            }
        });
    }

}
