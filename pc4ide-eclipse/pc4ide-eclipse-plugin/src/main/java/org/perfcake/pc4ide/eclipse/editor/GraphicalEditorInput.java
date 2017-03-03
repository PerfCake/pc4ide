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

package org.perfcake.pc4ide.eclipse.editor;

import java.net.MalformedURLException;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.part.FileEditorInput;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.loader.ModelLoader;
import org.perfcake.pc4ide.eclipse.Activator;
import org.perfcake.pc4ide.eclipse.EclipseLogger;


/**
 * Input for PerfCake graphical editor.
 */
public class GraphicalEditorInput extends FileEditorInput {

    private static final String SCENARIO_DESIGN_EDITOR = "Scenario Design editor";
    static final EclipseLogger log = Activator.getInstance().getLogger();
    private ScenarioModel model;

    /**
     * Creates new graphical editor input from file.
     * @param file file with the scenario
     */
    public GraphicalEditorInput(IFile file) {
        super(file);
        createModel();
    }

    @Override
    public String getName() {
        return SCENARIO_DESIGN_EDITOR;
    }

    public ScenarioModel getModel() {
        return model;
    }

    /**
     * Creates scenario model from file.
     */
    public void createModel() {
        final ModelLoader loader = new ModelLoader();
        try {
            model = loader.loadModel(getURI().toURL());
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        } catch (ModelConversionException e) {
            e.printStackTrace();
        } catch (org.perfcake.PerfCakeException e) {
            e.printStackTrace();
        }
    }
}
