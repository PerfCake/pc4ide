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

import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.view.View;

/**
 * Traverses through all controllers and unselects its view.
 */
public class UnselectVisitor implements ControllerVisitor {

    @Override
    public void visit(Controller controller) {
        View view = controller.getView();

        if (view != null) {
            view.setSelected(false);
        }

        Iterator<Controller> it = controller.getChildrenIterator();

        while (it.hasNext()) {
            it.next().accept(this);
        }
    }
}
