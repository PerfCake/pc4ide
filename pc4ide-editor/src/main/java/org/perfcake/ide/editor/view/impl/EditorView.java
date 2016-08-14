/**
 *
 */
package org.perfcake.ide.editor.view.impl;

import org.perfcake.ide.editor.view.AbstractView;
import org.perfcake.ide.editor.view.ComponentView;
import org.perfcake.ide.editor.view.UnsupportedChildViewException;

import javax.swing.JComponent;

import java.awt.Graphics;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Iterator;

/**
 * @author jknetl
 *
 */
public class EditorView extends AbstractView {

	// Swing component containing the editor
	private JComponent jComponent;


	public EditorView(JComponent jComponent) {
		super();
		this.jComponent = jComponent;

		int numOfSectors = 5;
		int angleExtent = 180/numOfSectors;
		Point2D center = new Point2D.Double(300,300);
		for (int i = 0; i < numOfSectors; i++){
			SectorView sector = new SectorView("Section " + (i+1), center, (int) (center.getX()*0.85), 50, -i*angleExtent + 180 - angleExtent, angleExtent);
			addChild(sector);
			invalidate();
		}


	}

	@Override
	public void addChild(ComponentView child) throws UnsupportedChildViewException {
		if (child instanceof SectorView){
			super.addChild(child);
		} else {
			throw new UnsupportedChildViewException("Editor view can accept only SectorView object as child");
		}
	}



	@Override
	public void invalidate() {
		super.invalidate();
		jComponent.repaint();
	}

	/* (non-Javadoc)
	 * @see org.perfcake.ide.editor.view.ComponentView#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		Iterator<ComponentView> iterator = getChildrenIterator();
		while (iterator.hasNext()){
			ComponentView child = iterator.next();
			child.draw(g);
		}
	}

	/* (non-Javadoc)
	 * @see org.perfcake.ide.editor.view.ComponentView#getViewBounds()
	 */
	@Override
	public Shape getViewBounds() {
		return null;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Iterator<ComponentView> iterator = getChildrenIterator();
		while (iterator.hasNext()){
			ComponentView child = iterator.next();
			if (child.getViewBounds().contains(e.getX(), e.getY())){
				unselectOthers();
				child.setSelected(true);
			}
		}


	}

	private void unselectOthers() {
		Iterator<ComponentView> it2 = getChildrenIterator();
		while (it2.hasNext()){
			ComponentView child2 = it2.next();
			if (child2.isSelected()){
				child2.setSelected(false);
			}
		}
	}



}
