/**
 *
 */
package org.perfcake.pc4ide.eclipse.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.perfcake.pc4ide.eclipse.Activator;
import org.perfcake.pc4ide.eclipse.EclipseLogger;

/**
 * @author jknetl
 *
 */
public class ScenarioMultiPageEditor extends MultiPageEditorPart {

	final static EclipseLogger log = Activator.getInstance().getLogger();

	private static final String XML_EDITOR_TAB_LABEL = "Source";

	private static final String DESIGN_EDITOR_TAB_LABEL = "Designer";

	private GraphicalScenarioEditor graphicalEditor;
	private int graphicalEditorIndex;

	private TextEditor xmlEditor;
	private int xmlEditorIndex;

	@Override
	protected void createPages() {
		createTextEditorPage();
		createDesignEditorPage();
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
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	private void createTextEditorPage() {
		try {
			xmlEditor = new StructuredTextEditor();
			xmlEditorIndex = addPage(xmlEditor, getEditorInput());
			setPageText(xmlEditorIndex, XML_EDITOR_TAB_LABEL);
		} catch (final PartInitException e) {
			log.warn("Cannot create Scenario text editor", e);
			ErrorDialog.openError(getSite().getShell(), "Error",
					"Error creating scenario text editor.", e.getStatus());

		}
	}

	private void createDesignEditorPage() {
		try {
			graphicalEditor = new GraphicalScenarioEditor();
			final IFileEditorInput input = (IFileEditorInput) getEditorInput();
			final GraphicalEditorInput editorInput = new GraphicalEditorInput(input.getFile());
			graphicalEditorIndex = addPage(graphicalEditor, editorInput);
			setPageText(graphicalEditorIndex, DESIGN_EDITOR_TAB_LABEL);
		} catch (final PartInitException e) {
			log.warn("Cannot create Scenario design editor: ", e);
			ErrorDialog.openError(getSite().getShell(), "Error",
					"Error creating scenario design editor.", e.getStatus());
		}

	}
}
