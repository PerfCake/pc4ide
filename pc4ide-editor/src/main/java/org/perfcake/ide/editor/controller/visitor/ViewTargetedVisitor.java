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

import java.awt.geom.Point2D;
import java.util.Iterator;
import org.perfcake.ide.editor.controller.Controller;

/**
 * Targeted visitor is a visitor which does not visit all Controllers in the hierarchy but
 * traverses through a tree to the particular controller where it performs an operation. It aims to
 * the controller which has a view on a given location.
 * <p>There may be multiple views at the given coordinates. In that case the visitor aims to the most
 * specific controller (the one which has the most depth in controller hierarchy).</p>
 * <p>If no controller has view on the given coordinates then operation is not executed.</p>
 */
public abstract class ViewTargetedVisitor implements ControllerVisitor {

    protected Point2D location;

    public ViewTargetedVisitor(Point2D location) {
        this.location = location;
    }

    /**
     * Doesn't traverse all hierarchy but aims to specific controller.
     *
     * @param controller Controller where to start a traversal
     */
    @Override
    public void visit(Controller controller) {
        if (controller.getView() != null && controller.getView().getViewBounds().contains(location)) {
            Controller moreSpecificTarget = findMoreSpecificTarget(controller);

            if (moreSpecificTarget == null) {
                performOperation(controller);
            } else {
                moreSpecificTarget.accept(this);
            }
        }
    }

    private Controller findMoreSpecificTarget(Controller controller) {
        Controller moreSpecificController = null;

        Iterator<Controller> it = controller.getChildrenIterator();
        while (it.hasNext()) {
            Controller childController = it.next();
            if (childController.getView() != null && childController.getView().getViewBounds() != null
                    && childController.getView().getViewBounds().contains(location)) {
                moreSpecificController = childController;
                break;
            }
        }

        return moreSpecificController;
    }

    /**
     * Performs a given operation on the most specifc controller which has a view on given coordinates.
     * Most specific controller means the one which has largest depth in the controllers hierarchy. If there are
     * more contorllers on the same level which both have their view on the location then most specific controller
     * is undefined and any of them may be chosen as a targed of the operation.
     *
     * @param controller Controller where operation is performed
     */
    protected abstract void performOperation(Controller controller);
}
