package org.perfcake.ide.editor.swing;

import org.perfcake.ide.core.model.ScenarioModel;
import org.perfcake.ide.editor.controller.impl.EditorController;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GraphicalEditorJPanel extends JPanel {

	private EditorController editorController;

	public GraphicalEditorJPanel(ScenarioModel scenarioModel) {
		super();
		addMouseListener(new EditorMouseListener());
		addComponentListener(new EditorComponentListener());
		editorController = new EditorController(this, scenarioModel);
		this.setBackground(Color.WHITE);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// first paint of the editor happens before the views are validated
		// so we need to validate them first if they are invalid
		if (!editorController.getView().isValid()){
			editorController.getView().validate((Graphics2D) getGraphics());
		}
		editorController.getView().draw((Graphics2D) g);
	}

	private class EditorComponentListener implements ComponentListener {

		@Override
		public void componentResized(ComponentEvent e) {
			editorController.getView().validate((Graphics2D) getGraphics());
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub

		}

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
