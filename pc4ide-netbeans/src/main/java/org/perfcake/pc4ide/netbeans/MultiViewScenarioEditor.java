/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.perfcake.pc4ide.netbeans;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.netbeans.spi.palette.PaletteActions;
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

/**
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
    
    private JToolBar toolBar = new JToolBar();

    private PerfCakeScenarioDataObject dataObject;
        private PaletteController paletteController = null;

    public MultiViewScenarioEditor(Lookup lookup) {
        dataObject = lookup.lookup(PerfCakeScenarioDataObject.class);
        assert dataObject != null;
        JLabel label = new JLabel("Hello world");
        add(label);
        setVisible(true);
        
        Node palette =new AbstractNode(Children.LEAF);
        paletteController = PaletteFactory.createPalette(palette, new PaletteActions() {
            @Override
            public Action[] getImportActions() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Action[] getCustomPaletteActions() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Action[] getCustomCategoryActions(Lookup lkp) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Action[] getCustomItemActions(Lookup lkp) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Action getPreferredAction(Lookup lkp) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        dataObject.getPrimaryFile().addFileChangeListener(new FileChangeAdapter() {
            @Override
            public void fileChanged(FileEvent fe) {
                refreshUI();
            }

        });
    }

    private void refreshUI() {

    }

    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolBar;
    }

    @Override
    public Action[] getActions() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return new Action[0];
    }

    @Override
    public Lookup getLookup() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return new ProxyLookup(dataObject.getLookup(), Lookups.fixed(paletteController));
    }

    @Override
    public void componentOpened() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentClosed() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentShowing() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentHidden() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentActivated() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentDeactivated() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UndoRedo getUndoRedo() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return UndoRedo.NONE;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public CloseOperationState canCloseElement() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return CloseOperationState.STATE_OK;
    }

}
