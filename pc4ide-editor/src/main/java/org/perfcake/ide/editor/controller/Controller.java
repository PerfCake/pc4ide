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

package org.perfcake.ide.editor.controller;

import java.awt.event.MouseListener;
import java.util.Iterator;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.editor.actions.ActionType;
import org.perfcake.ide.editor.controller.visitor.ControllerVisitor;
import org.perfcake.ide.editor.view.UnsupportedChildViewException;
import org.perfcake.ide.editor.view.View;
import org.perfcake.ide.editor.view.factory.ViewFactory;

/**
 * Represents a controller of a PerfCake component.
 *
 * @author jknetl
 */
public interface Controller extends MouseListener {

    /**
     * @return Return model object which is managed by this controller.
     */
    Model getModel();

    /**
     * @return controller of the parent inspector. The root controller returns null.
     */
    Controller getParent();

    /**
     * @return root of the whole controller hierarchy.
     */
    RootController getRoot();

    /**
     * Sets parent controller of this controller. This method should be called only when
     * the controller is added/removed as children view to other view.
     *
     * @param parent parent controller
     */
    void setParent(Controller parent);

    /**
     * Adds controller as a child controller.
     *
     * @param child adds child controller
     * @throws UnsupportedChildViewException when the controller is not supported by this {@link View}
     */
    void addChild(Controller child) throws UnsupportedChildViewException;

    /**
     * Remove child.
     *
     * @param child child controller to be removed.
     * @return true if child was removed. False if the child was not found.
     */
    boolean removeChild(Controller child);

    /**
     * @return iterator over children.
     */
    Iterator<Controller> getChildrenIterator();

    /**
     * @return associated view.
     */
    View getView();

    /**
     * @return an instance of view factory.
     */
    ViewFactory getViewFactory();

    /**
     * Updates view data according to values in model.
     *
     * @return True if data was changed, false if data had been up to date before and no change was needed.
     */
    boolean updateViewData();

    /**
     * Accepts a visitor to visit this node.
     *
     * @param visitor visitor instance
     */
    void accept(ControllerVisitor visitor);


    /**
     * Performs an action.
     *
     * @param action action type to be performed.
     */
    void performAction(ActionType action);
}