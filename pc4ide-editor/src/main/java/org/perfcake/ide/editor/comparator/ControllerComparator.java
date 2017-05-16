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

package org.perfcake.ide.editor.comparator;

import java.util.Comparator;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.editor.controller.Controller;

/**
 * Controller comparator is able to compare views based on its model classes.
 *
 * @author Jakub Knetl
 */
public class ControllerComparator implements Comparator<Controller> {

    private Comparator<Model> modelComparator;

    public ControllerComparator() {
        modelComparator = new ModelTypeComparator();
    }

    public ControllerComparator(Comparator<Model> modelComparator) {
        this.modelComparator = modelComparator;
    }

    @Override
    public int compare(Controller o1, Controller o2) {
        return modelComparator.compare(o1.getModel(), o2.getModel());
    }
}
