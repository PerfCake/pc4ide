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

package org.perfcake.ide.editor.controller.impl;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.List;
import javax.swing.JComponent;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.PropertyInfo;
import org.perfcake.ide.core.model.PropertyType;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.components.ScenarioModel.PropertyNames;
import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.controller.RootController;
import org.perfcake.ide.editor.controller.visitor.SelectVisitor;
import org.perfcake.ide.editor.controller.visitor.UnselectVisitor;
import org.perfcake.ide.editor.forms.FormManager;
import org.perfcake.ide.editor.swing.icons.GeneratorIcon;
import org.perfcake.ide.editor.swing.icons.MessageIcon;
import org.perfcake.ide.editor.swing.icons.ReceiverIcon;
import org.perfcake.ide.editor.swing.icons.ReporterIcon;
import org.perfcake.ide.editor.swing.icons.SenderIcon;
import org.perfcake.ide.editor.swing.icons.SequenceIcon;
import org.perfcake.ide.editor.swing.icons.ValidatorIcon;
import org.perfcake.ide.editor.view.ComponentView;
import org.perfcake.ide.editor.view.UnsupportedChildViewException;
import org.perfcake.ide.editor.view.impl.EditorView;


/**
 * Controler of the editor.
 */
public class EditorController extends AbstractController implements RootController {

    private JComponent jComponent;
    private EditorView view;
    private FormManager formManager;


    /**
     * Creates new editor controller.
     *
     * @param jComponent  Swing inspector used as a container for editor visuals
     * @param model       model of scenario managed by controller
     * @param formManager manager of forms to modify inspector properties
     */
    public EditorController(JComponent jComponent, ScenarioModel model, FormManager formManager) {
        super(model);
        this.jComponent = jComponent;
        this.formManager = formManager;
        view = new EditorView(jComponent);

        for (final PropertyInfo propertyInfo : model.getSupportedProperties()) {
            List<Property> properties =  model.getProperties(propertyInfo);
            if (propertyInfo.getType() == PropertyType.MODEL && properties != null && !properties.isEmpty()) {
                if (PropertyNames.GENERATOR.toString().equals(propertyInfo.getName())) {
                    Model generatorModel = properties.get(0).cast(Model.class);
                    final Controller generator = new SectionController("Generator", new GeneratorIcon(), generatorModel);
                    addChild(generator);
                } else if (PropertyNames.SENDER.toString().equals(propertyInfo.getName())) {
                    Model senderModel = properties.get(0).cast(Model.class);
                    final Controller sender = new SectionController("Sender", new SenderIcon(), senderModel);
                    addChild(sender);
                } else if (PropertyNames.REPORTERS.toString().equals(propertyInfo.getName())) {
                    for (Property reporterModel : properties) {
                        final Controller reporter = new SectionController("Reporter", new ReporterIcon(), reporterModel.cast(Model.class));
                        addChild(reporter);
                    }
                } else if (PropertyNames.SEQUENCES.toString().equals(propertyInfo.getName())) {
                    for (Property sequenceModel : properties) {
                        final Controller sequence = new SectionController("Sequences", new SequenceIcon(), sequenceModel.cast(Model.class));
                        addChild(sequence);
                    }
                } else if (PropertyNames.RECEIVER.toString().equals(propertyInfo)) {
                    final Controller receiver = new SectionController("Receiver", new ReceiverIcon(), properties.get(0).cast(Model.class));
                    addChild(receiver);
                } else if (PropertyNames.MESSAGES.toString().equals(propertyInfo)) {
                    for (Property messageModel : properties) {
                        final Controller message
                                = new SectionController("MessagesModel", new MessageIcon(), messageModel.cast(Model.class));
                        addChild(message);
                    }
                } else if (PropertyNames.VALIDATORS.toString().equals(propertyInfo)) {
                    for (Property validatorModel : properties) {
                        final Controller validator
                                = new SectionController("ValidationModel", new ValidatorIcon(), validatorModel.cast(Model.class));
                        addChild(validator);
                    }
                }
            }
        }
    }

    @Override
    public void addChild(Controller child) throws UnsupportedChildViewException {
        if (child instanceof SectionController) {
            super.addChild(child);
        } else {
            throw new UnsupportedChildViewException("Editor controller can accept only SectorController object as child");
        }
    }

    @Override
    public ComponentView getView() {
        return view;
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        UnselectVisitor unselectVisitor = new UnselectVisitor();
        unselectVisitor.visit(this);

        Point2D point = new Point2D.Double(e.getX(), e.getY());
        SelectVisitor selectVisitor = new SelectVisitor(point, formManager);
        selectVisitor.visit(this);

    }

    @Override
    public JComponent getJComponent() {
        return this.jComponent;
    }
}
