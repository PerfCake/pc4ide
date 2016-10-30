package org.perfcake.pc4ide.eclipse.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.perfcake.ide.core.model.ScenarioModel;
import org.perfcake.ide.editor.swing.EditorJPanel;
import org.perfcake.pc4ide.eclipse.Activator;
import org.perfcake.pc4ide.eclipse.EclipseLogger;

import java.awt.Frame;

public class GraphicalScenarioEditor extends EditorPart {

	private EclipseLogger logger = Activator.getInstance().getLogger();

	private ScenarioModel scenarioModel;

	public GraphicalScenarioEditor() {
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
		if (!(input instanceof GraphicalEditorInput)){
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
		final EditorJPanel editorJPanel = new EditorJPanel(scenarioModel);
		frame.add(editorJPanel);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}

