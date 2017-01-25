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

import org.perfcake.ide.core.model.director.ReflectiveModelDirector;
import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.forms.FormManager;
import org.perfcake.ide.editor.forms.FormPage;
import org.perfcake.ide.editor.forms.impl.SimpleFormPage;


/**
 * Selects a view of the most specific controller.
 *
 * @see ViewTargetedVisitor
 */
public class SelectVisitor extends ViewTargetedVisitor {

    private FormManager formManager;

    /**
     * Creates new select visitor.
     * @param location coordinates representing location to be visited.
     * @param formManager Form manager
     */
    public SelectVisitor(Point2D location, FormManager formManager) {
        super(location);
        this.formManager = formManager;
    }

    @Override
    protected void performOperation(Controller controller) {
        controller.getView().setSelected(true);
        formManager.removeAllPages();
        //TODO: (you should have some kind of factory method for the creating directors!!!)
        FormPage page = new SimpleFormPage(formManager, new ReflectiveModelDirector(controller.getModel(),
                formManager.getComponentManager()));
        formManager.addFormPage(page);
    }
}
