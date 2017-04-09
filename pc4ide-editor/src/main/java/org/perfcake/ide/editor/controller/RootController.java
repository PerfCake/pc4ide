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

package org.perfcake.ide.editor.controller;

import java.nio.file.Path;
import javax.swing.JComponent;
import org.perfcake.ide.core.exception.ModelSerializationException;
import org.perfcake.ide.core.manager.ScenarioManager;
import org.perfcake.ide.editor.form.FormManager;

/**
 * Represents top-level controller.
 *
 * @author jknetl
 */
public interface RootController extends Controller {

    /**
     * @return Jcomponent on which the editor is drawn.
     */
    JComponent getJComponent();

    /**
     * @return Form manager for managing form panel.
     */
    FormManager getFormManger();

    /**
     * @return Manager of the scenario file.
     */
    ScenarioManager getScenarioManager();
}
