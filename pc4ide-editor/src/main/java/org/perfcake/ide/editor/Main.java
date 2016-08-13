/**
 *
 */
package org.perfcake.ide.editor;

import org.perfcake.ide.editor.view.impl.EditorView;
import org.perfcake.ide.editor.view.impl.SectorView;

import javax.swing.JFrame;

import java.awt.EventQueue;
import java.awt.geom.Point2D;

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
				frame.setTitle("Java2d example");
//				frame.setSize(300, 200);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				EditorView editor = new EditorView();

				int numOfSectors = 7;
				int angleExtent = 40;
				Point2D center = new Point2D.Double(300,300);
				for (int i = 0; i < numOfSectors; i++){
					SectorView sector = new SectorView("Section " + i, center, (int) (center.getX()*0.85), 50, i*angleExtent, angleExtent);
					editor.addSector(sector);
				}


				frame.add(editor);

				frame.setVisible(true);

			}
		});
	}

}
