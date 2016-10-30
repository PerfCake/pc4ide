package org.perfcake.pc4ide.eclipse.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.part.FileEditorInput;
import org.perfcake.PerfCakeException;
import org.perfcake.ide.core.model.ModelLoader;
import org.perfcake.ide.core.model.ScenarioModel;
import org.perfcake.pc4ide.eclipse.Activator;
import org.perfcake.pc4ide.eclipse.EclipseLogger;

import java.net.MalformedURLException;

public class GraphicalEditorInput extends FileEditorInput {

	private static final String SCENARIO_DESIGN_EDITOR = "Scenario Design editor";
	final static EclipseLogger log = Activator.getInstance().getLogger();
	private ScenarioModel model;

	public GraphicalEditorInput(IFile file) {
		super(file);
		createModel();
	}

	@Override
	public String getName() {
		return SCENARIO_DESIGN_EDITOR;
	}

	public ScenarioModel getModel() {
		return model;
	}

	public void createModel() {
		final ModelLoader loader = new ModelLoader();
		try {
			model = loader.loadModel(getURI().toURL());
		} catch (final PerfCakeException e) {
			e.printStackTrace();
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
