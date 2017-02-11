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

import javax.swing.JComponent;

import org.perfcake.ide.core.model.AbstractModel;
import org.perfcake.ide.core.model.GeneratorModel;
import org.perfcake.ide.core.model.MessagesModel;
import org.perfcake.ide.core.model.ReceiverModel;
import org.perfcake.ide.core.model.ReportingModel;
import org.perfcake.ide.core.model.ScenarioModel;
import org.perfcake.ide.core.model.SenderModel;
import org.perfcake.ide.core.model.SequencesModel;
import org.perfcake.ide.core.model.ValidationModel;
import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.controller.RootController;
import org.perfcake.ide.editor.controller.visitor.SelectVisitor;
import org.perfcake.ide.editor.controller.visitor.UnselectVisitor;
import org.perfcake.ide.editor.forms.FormManager;
import org.perfcake.ide.editor.view.ComponentView;
import org.perfcake.ide.editor.view.UnsupportedChildViewException;
import org.perfcake.ide.editor.view.icons.GeneratorIcon;
import org.perfcake.ide.editor.view.icons.MessageIcon;
import org.perfcake.ide.editor.view.icons.ReceiverIcon;
import org.perfcake.ide.editor.view.icons.ReporterIcon;
import org.perfcake.ide.editor.view.icons.SenderIcon;
import org.perfcake.ide.editor.view.icons.SequenceIcon;
import org.perfcake.ide.editor.view.icons.ValidatorIcon;
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
     * @param jComponent Swing component used as a container for editor visuals
     * @param model model of scenario managed by controller
     * @param formManager manager of forms to modify component properties
     */
    public EditorController(JComponent jComponent, ScenarioModel model, FormManager formManager) {
        super(model);
        this.jComponent = jComponent;
        this.formManager = formManager;
        view = new EditorView(jComponent);

        for (final AbstractModel child : model.getModelChildren()) {
            if (child instanceof GeneratorModel) {
                final Controller generator = new SectionController("Generator", new GeneratorIcon(), child);
                addChild(generator);
            } else if (child instanceof SenderModel) {
                final Controller sender = new SectionController("Sender", new SenderIcon(), child);
                addChild(sender);
            } else if (child instanceof ReportingModel) {
                final Controller reporting = new SectionController("Reporting", new ReporterIcon(), child);
                addChild(reporting);
            } else if (child instanceof SequencesModel) {
                final Controller sequences = new SectionController("Sequences", new SequenceIcon(), child);
                addChild(sequences);
            } else if (child instanceof ReceiverModel) {
                final Controller receiver = new SectionController("Receiver", new ReceiverIcon(), child);
                addChild(receiver);
            } else if (child instanceof MessagesModel) {
                final Controller messages = new SectionController("MessagesModel", new MessageIcon(), child);
                addChild(messages);
            } else if (child instanceof ValidationModel) {
                final Controller validation = new SectionController("ValidationModel", new ValidatorIcon(), child);
                addChild(validation);
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
