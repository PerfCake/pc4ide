/**
 *
 */
package org.perfcake.ide.editor;

import org.perfcake.PerfCakeException;
import org.perfcake.ide.core.model.ModelLoader;
import org.perfcake.ide.core.model.ScenarioModel;
import org.perfcake.ide.editor.swing.EditorJPanel;

import javax.swing.JFrame;

import java.awt.EventQueue;
import java.io.File;
import java.net.MalformedURLException;

/**
 * @author jknetl
 *
 */
public class Main {

	/**
	 * @param args
	 * @throws PerfCakeException
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws MalformedURLException, PerfCakeException {
		// TODO Auto-generated method stub

		final ModelLoader loader = new ModelLoader();
		final File scenarioFile = new File("src/main/resources/scenario/http.xml");
		final ScenarioModel model = loader.loadModel(scenarioFile.toURI().toURL());

		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {

				//				final PerfCakeScenarioParser parser = new PerfCakeScenarioParser();
				//				ScenarioModel model = null;
				//				try {
				//					final Scenario s = parser.parse(new File("src/main/resources/scenario/http.xml").toURI().toURL());
				//					model = ModelConverter.getPc4ideModel(s);
				//				} catch (final PerfCakeException e) {
				//					e.printStackTrace();
				//				} catch (final MalformedURLException e) {
				//					e.printStackTrace();
				//				}

				final JFrame frame = new JFrame();
				frame.setTitle("Perfcake editor");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				final EditorJPanel editor = new EditorJPanel(model);
				frame.add(editor);
				frame.setVisible(true);
			}
		});
	}

}
