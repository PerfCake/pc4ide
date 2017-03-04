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

package org.perfcake.ide.editor.view.factory;

import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.editor.view.View;

/**
 * ViewFactory is responsible for creating view instances.
 *
 * @author Jakub Knetl
 */
public interface ViewFactory {

    /**
     * Creates view for a model.
     *
     * @param model Model for which view will be created.
     * @return view for the model
     */
    View createView(Model model);

}
