/**
 *
 */
package org.perfcake.ide.editor;

import org.perfcake.ide.editor.swing.EditorJPanel;

import javax.swing.JFrame;

import java.awt.EventQueue;

/**
 * @author jknetl
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
				final EditorJPanel editor = new EditorJPanel(null);
				frame.add(editor);
				frame.setVisible(true);
			}
		});
	}

}
