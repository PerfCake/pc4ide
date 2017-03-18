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

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.editor.actions.ActionType;
import org.perfcake.ide.editor.actions.handlers.ActionHandler;
import org.perfcake.ide.editor.actions.handlers.SelectionHandler;
import org.perfcake.ide.editor.controller.visitor.ControllerVisitor;
import org.perfcake.ide.editor.view.UnsupportedChildViewException;
import org.perfcake.ide.editor.view.View;
import org.perfcake.ide.editor.view.factory.ViewFactory;

/**
 * AbstractController provides default implementation for some methods from {@link Controller} interface.
 * <p>AbstractController implements:</p>
 * <ol>
 * <li> no operation handlers for {@link MouseEvent} </li>
 * <li> child management operations </li>
 * </ol>
 *
 * @author jknetl
 */
public abstract class AbstractController implements Controller {

    protected ViewFactory viewFactory;
    protected View view;
    protected Model model;
    protected List<Controller> children = new ArrayList<>();
    private Map<ActionType, ActionHandler> actionHandlers = new HashMap<>();
    protected Controller parent = null;

    /**
     * Creates abstract controller which will manage a model.
     *
     * @param model       model to be managed
     * @param viewFactory viewFactory which may be used to create views.
     */
    public AbstractController(Model model, ViewFactory viewFactory) {
        super();
        this.model = model;
        this.viewFactory = viewFactory;
        this.view = viewFactory.createView(model);
        initActionHandlers();
    }

    /**
     * This method initializes action handlers.
     */
    protected void initActionHandlers() {
        addActionHandler(new SelectionHandler());
    }

    @Override
    public Controller getParent() {
        return parent;
    }

    @Override
    public RootController getRoot() {
        Controller root;
        root = this;
        while (root.getParent() != null) {
            root = this.getParent();
        }

        return (RootController) root;
    }

    @Override
    public void setParent(Controller parent) {
        this.parent = parent;
    }

    @Override
    public void addChild(Controller child) throws UnsupportedChildViewException {
        children.add(child);
        child.setParent(this);
        getView().addChild(child.getView());
        child.getView().setParent(this.getView());
        child.getView().invalidate();
    }

    @Override
    public Iterator<Controller> getChildrenIterator() {
        return children.iterator();
    }

    @Override
    public boolean removeChild(Controller child) {
        final boolean removed = children.remove(child);
        if (removed) {
            child.getView().setParent(null);
            getView().removeChild(getView());
            child.setParent(null);
            getView().invalidate();
        }
        return removed;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //empty on purpose
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //empty on purpose
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //empty on purpose
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //empty on purpose
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //empty on purpose
    }

    @Override
    public void accept(ControllerVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public void performAction(ActionType action) {
        ActionHandler handler = actionHandlers.get(action);
        if (handler != null) {
            handler.handleAction();
        }
    }

    /**
     * Adds action handler. If this controller already has action handler for same {@link ActionType}, then old handler is removed.
     *
     * @param handler handler to be added
     */
    public void addActionHandler(ActionHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("Action handler is null");
        }
        if (handler.getEventType() == null) {
            throw new IllegalArgumentException("Action handler has null ActionType");
        }

        actionHandlers.put(handler.getEventType(), handler);
        handler.setController(this);
    }

    /**
     * Removes actionHandler from this controller.
     * @param handler handler to be removed
     * @return True if handler was removed.
     */
    public boolean removeActionHandler(ActionHandler handler) {
        if (handler == null) {
            return false;
        }

        boolean removed = actionHandlers.remove(handler.getEventType(), handler);
        if (removed) {
            handler.setController(null);
        }

        return removed;
    }
}
