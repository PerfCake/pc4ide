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
import org.perfcake.ide.core.exec.ExecutionManager;
import org.perfcake.ide.editor.controller.Controller;

/**
 * Detaches controllers from Debug manager.
 *
 * @author Jakub Knetl
 */
public class DetachDebugManagerVisitor implements ControllerVisitor {

    private ExecutionManager executionManager;

    public DetachDebugManagerVisitor(ExecutionManager executionManager) {
        this.executionManager = executionManager;
    }

    @Override
    public void visit(Controller controller) {

        executionManager.removeListener(controller);

        Iterator<Controller> it = controller.getChildrenIterator();

        while (it.hasNext()) {
            Controller child = it.next();
            child.accept(this);
        }
    }
}
