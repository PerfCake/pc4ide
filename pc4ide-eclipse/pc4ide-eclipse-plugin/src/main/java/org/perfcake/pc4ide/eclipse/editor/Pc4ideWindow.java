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

package org.perfcake.pc4ide.eclipse.editor;

import java.awt.Frame;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.editor.swing.editor.Pc4ideEditor;
import org.perfcake.pc4ide.eclipse.Activator;
import org.perfcake.pc4ide.eclipse.EclipseLogger;


/**
 * This editor represents a graphical PerfCake scenario editor.
 */
public class Pc4ideWindow extends EditorPart {

    private EclipseLogger logger = Activator.getInstance().getLogger();

    private ScenarioModel scenarioModel;

    public Pc4ideWindow() {
        super();
    }

    @Override
    public void doSave(IProgressMonitor progressMonitor) {
        // TODO Auto-generated method stub

    }

    @Override
    public void doSaveAs() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {

        setSite(site);
        if (!(input instanceof GraphicalEditorInput)) {
            logger.warn("Cannot open file. File must be valid scenario!");
            throw new PartInitException("Cannot open file. It is not valid scenario.");
        }

        setInput(input);
        scenarioModel = ((GraphicalEditorInput) input).getModel();
    }

    @Override
    public boolean isDirty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void createPartControl(Composite parent) {
        // TODO Auto-generated method stub
        final Composite composite = new Composite(parent, SWT.EMBEDDED | SWT.NO_BACKGROUND);
        final Frame frame = SWT_AWT.new_Frame(composite);
        final Pc4ideEditor editorJPanel = new Pc4ideEditor(scenarioModel);
        frame.add(editorJPanel);

    }

    @Override
    public void setFocus() {
        // TODO Auto-generated method stub

    }
}

