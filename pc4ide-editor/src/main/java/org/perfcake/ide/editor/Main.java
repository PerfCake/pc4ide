/**
 *
 */
package org.perfcake.ide.editor;

import org.perfcake.ide.editor.view.impl.EditorJPanel;

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
				JFrame frame = new JFrame();
				frame.setTitle("Perfcake editor");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				EditorJPanel editor = new EditorJPanel();
				frame.add(editor);
				frame.setVisible(true);
			}
		});
	}

}
