package org.perfcake.ide.editor.view.impl;

import org.perfcake.ide.editor.view.ComponentView;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditorView extends JPanel {

	private List<SectorView> sectors = new ArrayList<>();


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



}
