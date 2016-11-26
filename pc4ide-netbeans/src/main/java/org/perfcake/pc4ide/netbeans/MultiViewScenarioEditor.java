/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.perfcake.pc4ide.netbeans;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
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
import org.perfcake.PerfCakeException;
import org.perfcake.ide.core.model.ModelLoader;
import org.perfcake.ide.core.model.ScenarioModel;
import org.perfcake.ide.editor.swing.EditorJPanel;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.perfcake.ide.core.components.ComponentManager;

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

	private static final Logger logger = Logger.getLogger(MultiViewScenarioEditor.class.getName());

	private JToolBar toolBar = new JToolBar();

	private PerfCakeScenarioDataObject dataObject;
	private PaletteController paletteController = null;
	private MultiViewElementCallback callback;
	private EditorJPanel pc4ideEditor;

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
		}

		final InputStream javadocStream = EditorJPanel.class.getResourceAsStream(ComponentManager.JAVADOC_LOCATION_CLASSPATH);
		List<String> packages = Arrays.asList(new String[]{"org.perfcake.message", "org.perfcake.reporting", "org.perfcake.validation", "org.perfcake"});
		ComponentManager manager = new ComponentManager(javadocStream, packages);
		pc4ideEditor = new EditorJPanel(model, manager);

		Node palette = new AbstractNode(Children.LEAF);
		paletteController = PaletteFactory.createPalette(palette, new NoopPaletteActions());

		dataObject.getPrimaryFile().addFileChangeListener(new FileChangeAdapter() {
			@Override
			public void fileChanged(FileEvent fe) {
				refreshUI();
			}

		});
	}

	private void refreshUI() {
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
