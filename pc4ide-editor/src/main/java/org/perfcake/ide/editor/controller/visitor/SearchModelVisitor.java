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

package org.perfcake.ide.editor.controller.visitor;

import java.util.Iterator;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.editor.controller.Controller;

/**
 * <p>
 * Search model visitor searches from a controller in the hierarchy and searches down through the hierarchy for controller
 * which contains particular model. If the controller is found, then an action is invoked on the controller.
 * If no controller which controls the model is found, then this controllers does nothing.
 * </p>
 * <p>
 * This implementations stops to search in case that it either:
 * </p>
 * <ul>
 * <li>It finds first occurrence of the controller which controls the model</li>
 * <li>It traverses all controllers but none of them contains the model</li>
 * </ul>
 *
 * @author Jakub Knetl
 */
public abstract class SearchModelVisitor implements ControllerVisitor {

    /**
     * Model which is searched through the controller hierarchy.
     */
    protected Model model;
    private boolean actionPerformed = false;

    /**
     * Creates new search model visitor.
     *
     * @param model model for whose controller is being searched
     */
    public SearchModelVisitor(Model model) {
        if (model == null) {
            throw new IllegalArgumentException("model cannot be null.");

        }
        this.model = model;
    }

    @Override
    public void visit(Controller controller) {
        if (controller == null) {
            throw new IllegalArgumentException("controller cannot be null");
        }

        if (model.equals(controller.getModel())) {
            performAction(controller);
            actionPerformed = true;
        } else {
            searchInChildren(controller);
        }
    }

    private void searchInChildren(Controller controller) {
        Iterator<Controller> it = controller.getChildrenIterator();
        while (it.hasNext()) {
            Controller child = it.next();
            child.accept(this);
            if (actionPerformed) {
                break;
            }
        }
    }


    /**
     * This method is called if controller which contains the #model is found
     *
     * @param controller controller which contains the model which was searched for byt this visitor.
     */
    protected abstract void performAction(Controller controller);

}
