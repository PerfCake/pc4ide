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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.perfcake.pc4ide.netbeans;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.awt.UndoRedo;
import org.openide.filesystems.FileChangeAdapter;
import org.openide.filesystems.FileEvent;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;
import org.perfcake.PerfCakeException;
import org.perfcake.ide.core.components.ComponentCatalogue;
import org.perfcake.ide.core.components.ReflectionComponentCatalogue;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.loader.ModelLoader;
import org.perfcake.ide.editor.swing.editor.Pc4ideEditor;

/**
 * Netbeans PerfCake editor with multiple views.
 *
 * @author jknetl
 */
@MultiViewElement.Registration(
        displayName = "Scenario designer",
        mimeType = "text/perfcake+xml",
        persistenceType = TopComponent.PERSISTENCE_NEVER,
        preferredID = "org.perfcake.pc4ide.netbeans.MultiViewScenarioEditor",
        position = 1000
)
public class MultiViewScenarioEditor extends JPanel implements MultiViewElement {

    private static final Logger logger = Logger.getLogger(MultiViewScenarioEditor.class.getName());

    private JToolBar toolBar = new JToolBar();

    private PerfCakeScenarioDataObject dataObject;
    private PaletteController paletteController = null;
    private MultiViewElementCallback callback;
    private Pc4ideEditor pc4ideEditor;

    /**
     * Creates new NetBeans multi-view editor.
     *
     * @param lookup lookup
     */
    public MultiViewScenarioEditor(Lookup lookup) {
        dataObject = lookup.lookup(PerfCakeScenarioDataObject.class);
        assert dataObject != null;
        JLabel label = new JLabel("Hello world");
        add(label);
        setVisible(true);

        ModelLoader loader = new ModelLoader();
        ScenarioModel model = null;
        try {
            model = loader.loadModel(dataObject.getPrimaryFile().toURL());
        } catch (PerfCakeException e) {
            logger.log(Level.SEVERE, "Cannot open scenario", e);
        } catch (ModelConversionException e) {
            logger.log(Level.SEVERE, "Cannot open scenario", e);
        }

        String[] packages = new String[] {"org.perfcake.message", "org.perfcake.reporting", "org.perfcake.validation", "org.perfcake"};
        ComponentCatalogue manager = new ReflectionComponentCatalogue(packages);
        pc4ideEditor = new Pc4ideEditor(model, manager);

        Node palette = new AbstractNode(Children.LEAF);
        paletteController = PaletteFactory.createPalette(palette, new NoopPaletteActions());

        dataObject.getPrimaryFile().addFileChangeListener(new FileChangeAdapter() {
            @Override
            public void fileChanged(FileEvent fe) {
                refreshUi();
            }

        });
    }

    private void refreshUi() {
        //pc4ideEditor.repaint();
    }

    @Override
    public JComponent getVisualRepresentation() {
        return pc4ideEditor;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolBar;
    }

    @Override
    public Action[] getActions() {
        return new Action[0];
    }

    @Override
    public Lookup getLookup() {
        return new ProxyLookup(dataObject.getLookup(), Lookups.fixed(paletteController));
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    @Override
    public void componentShowing() {
    }

    @Override
    public void componentHidden() {
    }

    @Override
    public void componentActivated() {
    }

    @Override
    public void componentDeactivated() {
    }

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;

    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

}
