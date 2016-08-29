package org.perfcake.ide.editor.view.impl;

import org.perfcake.ide.core.model.ScenarioModel;
import org.perfcake.ide.editor.controller.impl.EditorController;
import org.perfcake.ide.editor.layout.AngularData;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.RadiusData;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EditorJPanel extends JPanel {

	private static final int DEFAULT_ANGLE_EXTENT = 180;
	private static final int DEFAULT_START_ANGLE = 0;
	private static final int MAXIMUM_INNER_RADIUS = 50;
	private EditorController editorController;

	public EditorJPanel(ScenarioModel scenarioModel) {
		super();
		addMouseListener(new EditorMouseListener());
		final double outerRadius = (0.9*Math.min(getWidth(), getHeight()))/2;
		final double innerRadius = Math.min(MAXIMUM_INNER_RADIUS, (0.2*Math.min(getWidth(), getHeight()))/2);
		final RadiusData radiusData = new RadiusData(innerRadius, outerRadius);
		final AngularData angularData = new AngularData(DEFAULT_START_ANGLE, DEFAULT_ANGLE_EXTENT);
		final LayoutData data = new LayoutData(500, 500, new RadiusData(50, 240), new AngularData(0, 180));
		editorController = new EditorController(this, data, scenarioModel);
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
