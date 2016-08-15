package org.perfcake.ide.editor.view.impl;

import org.perfcake.ide.editor.controller.impl.EditorController;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EditorJPanel extends JPanel {

	private EditorController editorController;

	public EditorJPanel() {
		super();
		addMouseListener(new EditorMouseListener());
		editorController = new EditorController(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		editorController.drawView(g);
	}

	private class EditorMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			editorController.mouseReleased(e);
//			for (SectorView s : sectors){
//				if (s.getViewBounds().contains(e.getX(), e.getY())){
//					//unselect sectors
//					for (SectorView s2 : sectors){
//						if (!s2.equals(s)){
//							s2.setSelected(false);
//						}
//					}
//					s.setSelected(true);
//					s.mouseReleased(e);
//					repaint();
//				}
//			}

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}



}
