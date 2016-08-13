package org.perfcake.ide.editor.view.impl;

import org.perfcake.ide.editor.view.ComponentView;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditorView extends JPanel {

	private List<SectorView> sectors = new ArrayList<>();




	public EditorView() {
		super();
		addMouseListener(new EditorMouseListener());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (ComponentView sector: sectors){
			sector.draw(g);
		}

	}

	public void addSector(SectorView sector){
		sectors.add(sector);
	}

	public void removeSector(SectorView sector){
		sectors.remove(sector);
	}

	/**
	 *
	 * @return unmodifiable collection of sectors
	 */
	public List<SectorView> getSectors() {
		return Collections.unmodifiableList(sectors);
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
			for (SectorView s : sectors){
				if (s.getViewBounds().contains(e.getX(), e.getY())){
					System.out.println("Sector \"" + s.getComponentName() + "\" clicked!");
				}
			}

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
