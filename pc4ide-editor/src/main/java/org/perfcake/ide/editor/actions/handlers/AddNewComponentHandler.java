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

package org.perfcake.ide.editor.actions.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.perfcake.ide.core.command.AddPropertyCommand;
import org.perfcake.ide.core.command.Command;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.PropertyInfo;
import org.perfcake.ide.core.model.PropertyType;
import org.perfcake.ide.editor.actions.ActionType;
import org.perfcake.ide.editor.controller.visitor.SelectModelVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler which adds new component action.
 *
 * @author Jakub Knetl
 */
public class AddNewComponentHandler extends AbstractHandler {

    static final Logger logger = LoggerFactory.getLogger(AddNewComponentHandler.class);

    public AddNewComponentHandler() {
        super(ActionType.SELECT);
    }

    @Override
    public void handleAction(Point2D location) {

        logger.info("Add new component action handled");

        JPopupMenu popup = new JPopupMenu();

        addMenuItems(popup);
        JComponent rootPanel = controller.getRoot().getJComponent();
        popup.show(rootPanel, (int) location.getX(), (int) location.getY());
    }

    protected void addMenuItems(JPopupMenu popup) {
        Model model = controller.getModel();


        List<PropertyInfo> possibleComponents = findComponents(model);
        for (PropertyInfo propertyInfo : possibleComponents) {
            JMenuItem menuItem = new JMenuItem("Add a " + propertyInfo.getPerfCakeComponent().toString().toLowerCase());
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Model newModel = controller.getModelFactory().createModel(propertyInfo.getPerfCakeComponent());
                    Command command = new AddPropertyCommand(model, newModel, propertyInfo);
                    controller.getCommandInvoker().executeCommand(command);

                    SwingUtilities.invokeLater(() -> controller.getRoot().accept(new SelectModelVisitor(newModel)));

                }
            });
            popup.add(menuItem);
        }
    }

    private List<PropertyInfo> findComponents(Model model) {

        List<PropertyInfo> components = new ArrayList<>();

        for (PropertyInfo info : model.getSupportedProperties()) {
            if ((info.getMaxOccurs() == -1 || info.getMaxOccurs() > model.getProperties(info).size())
                    && info.getType() == PropertyType.MODEL) {
                components.add(info);
                //Model nestedModel = controller.getModelFactory().createModel(info.getPerfCakeComponent());
                //components.addAll(findComponents(nestedModel));
            }
        }

        return components;
    }
}
