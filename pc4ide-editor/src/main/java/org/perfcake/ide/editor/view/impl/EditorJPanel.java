package org.perfcake.ide.editor.view.impl;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EditorJPanel extends JPanel {

	private EditorView editor;

	public EditorJPanel() {
		super();
		addMouseListener(new EditorMouseListener());
		editor = new EditorView(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		editor.draw(g);
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
			editor.mouseReleased(e);
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
