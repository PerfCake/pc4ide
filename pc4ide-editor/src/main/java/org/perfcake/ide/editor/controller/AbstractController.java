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
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.perfcake.ide.core.command.invoker.CommandInvoker;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.PropertyInfo;
import org.perfcake.ide.core.model.PropertyType;
import org.perfcake.ide.core.model.factory.ModelFactory;
import org.perfcake.ide.core.model.listeners.ModelListener;
import org.perfcake.ide.editor.actions.ActionType;
import org.perfcake.ide.editor.actions.handlers.ActionHandler;
import org.perfcake.ide.editor.actions.handlers.AddSiblingHandler;
import org.perfcake.ide.editor.actions.handlers.RemoveHandler;
import org.perfcake.ide.editor.actions.handlers.SelectionHandler;
import org.perfcake.ide.editor.controller.visitor.ControllerVisitor;
import org.perfcake.ide.editor.view.UnsupportedChildViewException;
import org.perfcake.ide.editor.view.View;
import org.perfcake.ide.editor.view.factory.ViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public abstract class AbstractController implements Controller, ModelListener {

    static final Logger logger = LoggerFactory.getLogger(AbstractController.class);

    protected ViewFactory viewFactory;
    protected View view;
    protected Model model;
    protected List<Controller> children = new ArrayList<>();
    private Map<ActionType, ActionHandler> actionHandlers = new HashMap<>();
    protected Controller parent = null;
    protected ModelFactory modelFactory;

    /**
     * Creates abstract controller which will manage a model.
     * @param model        model to be managed
     * @param modelFactory model factory.
     * @param viewFactory  viewFactory which may be used to create views.
     */
    public AbstractController(Model model, ModelFactory modelFactory, ViewFactory viewFactory) {
        super();
        if (model == null) {
            throw new IllegalArgumentException("Model is null.");
        }
        if (modelFactory == null) {
            throw new IllegalArgumentException("Model factory is null.");
        }
        if (viewFactory == null) {
            throw new IllegalArgumentException("View factory is null.");
        }

        this.model = model;
        this.modelFactory = modelFactory;
        this.viewFactory = viewFactory;
        this.view = viewFactory.createView(model);

        initActionHandlers();
        model.addModelListener(this);
    }

    /**
     * This method initializes action handlers.
     */
    protected void initActionHandlers() {
        addActionHandler(new SelectionHandler());
        addActionHandler(new RemoveHandler());
        addActionHandler(new AddSiblingHandler());
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
            root = root.getParent();
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
            getView().removeChild(child.getView());
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
     *
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

    @Override
    public ModelFactory getModelFactory() {
        return modelFactory;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        logger.debug("Event received in {}: {}", this.getClass().getSimpleName(), evt);
        if (evt.getOldValue() instanceof Model && evt.getNewValue() == null) {
            Model oldValue = (Model) evt.getOldValue();
            Controller oldController = findChildByModel(oldValue);
            if (oldController != null) {
                logger.debug("Controller {} removes child: {}", this.getClass().getSimpleName(), oldController.getClass().getSimpleName());
                removeChild(oldController);
            }
        }

        if (evt.getNewValue() instanceof Model && evt.getOldValue() == null) {
            Controller childController = createChildController(((Model) evt.getNewValue()).cast(Model.class));
            if (childController != null) {
                addChild(childController);
            }
        }
        boolean modified = updateViewData();
        if (modified) {
            logger.debug("Event caused view data update invalidating the view!");
            getView().invalidate();
        }
    }

    /**
     * Finds child controller by its model class.
     *
     * @param model model whose controller is searched for
     * @return child controller or null. If no direct child has such model.
     */
    protected Controller findChildByModel(Model model) {

        Controller child = null;

        Iterator<Controller> it = getChildrenIterator();
        while (it.hasNext()) {
            Controller c = it.next();
            if (c.getModel() == model) {
                child = c;
                break;
            }
        }

        return child;
    }

    /**
     * This method create children controller. It goes through all model properties which are also model. Child controller is added only
     * if the {@link #createChildController(Model)} method returns controller.
     */
    protected void createChildrenControllers() {
        for (final PropertyInfo propertyInfo : model.getSupportedProperties()) {
            List<Property> properties = model.getProperties(propertyInfo);
            if (propertyInfo.getType() == PropertyType.MODEL && properties != null && !properties.isEmpty()) {
                for (Property p : properties) {
                    Controller child = createChildController(p.cast(Model.class));
                    if (child != null) {
                        addChild(child);
                    }
                }
            }
        }
    }

    @Override
    public Controller createChildController(Model model) {
        if (model == null) {
            throw new IllegalArgumentException("model cannot be null");
        }

        if (model.getPropertyInfo() == null) {
            throw new IllegalArgumentException("model must containt property info");
        }

        return null;
    }

    @Override
    public CommandInvoker getCommandInvoker() {
        return getRoot().getCommandInvoker();
    }
}
