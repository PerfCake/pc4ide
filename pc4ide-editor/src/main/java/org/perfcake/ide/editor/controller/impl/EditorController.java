package org.perfcake.ide.editor.controller.impl;

import org.perfcake.ide.core.model.ScenarioModel;
import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.SimpleCircularLayoutManager;
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

	private ScenarioModel scenarioModel;


	public EditorController(JComponent jComponent, LayoutData data, ScenarioModel model) {
		super();
		this.layoutManager = new SimpleCircularLayoutManager(data, this);
		this.scenarioModel = model;
		this.jComponent = jComponent;
		final int numOfSectors = 4;
		final int angleExtent = 180/numOfSectors;
		view = new EditorView(null);
		for (int i = 0; i < numOfSectors; i++){
			final LayoutData childData = new LayoutData(data);
			childData.getAngularData().setStartAngle(data.getAngularData().getStartAngle() + i * angleExtent);
			childData.getAngularData().setAngleExtent(angleExtent);
			//			final SectorView sector = new SectorView(null, "Section " + (i + 1), childData, new GeneratorIcon());
			final SectionController section = new SectionController(childData);
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
	}

	@Override
	public void validate() {
		super.validate();
		jComponent.repaint();
	}

	@Override
	public void drawView(Graphics g) {
		view.draw(g);

		final Iterator<Controller> it = getChildrenIterator();
		while (it.hasNext()){
			final Controller child = it.next();
			child.drawView(g);
		}

	}

	@Override
	public ComponentView getView() {
		return view;
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		final Iterator<Controller> iterator = getChildrenIterator();
		while (iterator.hasNext()){
			final Controller child = iterator.next();
			if (child.getView().getViewBounds().contains(e.getX(), e.getY())){
				unselectOthers();
				child.getView().setSelected(true);
				child.invalidate();
			}
		}
	}

	private void unselectOthers() {
		final Iterator<Controller> it = getChildrenIterator();
		while (it.hasNext()){
			final Controller child = it.next();
			if (child.getView().isSelected()){
				child.getView().setSelected(false);
				child.invalidate();
			}
		}
	}
}
