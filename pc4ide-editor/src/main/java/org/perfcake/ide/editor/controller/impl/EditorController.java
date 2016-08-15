package org.perfcake.ide.editor.controller.impl;

import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.view.ComponentView;
import org.perfcake.ide.editor.view.UnsupportedChildViewException;
import org.perfcake.ide.editor.view.impl.EditorView;

import javax.swing.JComponent;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Iterator;

public class EditorController extends AbstractController {

	private JComponent jComponent;
	private EditorView view;
	private Point2D center;


	public EditorController(JComponent jComponent) {
		super();
		this.jComponent = jComponent;
		int numOfSectors = 5;
		int angleExtent = 180/numOfSectors;
		view = new EditorView();
		center = new Point2D.Double(300,300);
		for (int i = 0; i < numOfSectors; i++){
//			SectorView sector = new SectorView("Section " + (i+1), center, (int) (center.getX()*0.85), 50, -i*angleExtent + 180 - angleExtent, angleExtent, new GeneratorIcon());
			SectionController section = new SectionController(center, (int) (center.getX()*0.85), 50, -i*angleExtent + 180 - angleExtent, angleExtent);
			addChild(section);
			invalidate();
		}
	}

	@Override
	public void addChild(Controller child) throws UnsupportedChildViewException {
		if (child instanceof SectionController){
			super.addChild(child);
		} else {
			throw new UnsupportedChildViewException("Editor controller can accept only SectorController object as child");
		}
	}



	@Override
	public void invalidate() {
		super.invalidate();
		jComponent.repaint();
	}

	@Override
	public void drawView(Graphics g) {
		view.draw(g);

		Iterator<Controller> it = getChildrenIterator();
		while (it.hasNext()){
			Controller child = it.next();
			child.drawView(g);
		}

	}

	@Override
	public ComponentView getView() {
		return view;
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		Iterator<Controller> iterator = getChildrenIterator();
		while (iterator.hasNext()){
			Controller child = iterator.next();
			if (child.getView().getViewBounds().contains(e.getX(), e.getY())){
				unselectOthers();
				child.getView().setSelected(true);
				child.invalidate();
			}
		}
	}

	private void unselectOthers() {
		Iterator<Controller> it = getChildrenIterator();
		while (it.hasNext()){
			Controller child = it.next();
			if (child.getView().isSelected()){
				child.getView().setSelected(false);
				child.invalidate();
			}
		}
	}
}
