package org.perfcake.ide.editor.controller.impl;

import org.perfcake.ide.core.model.AbstractModel;
import org.perfcake.ide.core.model.GeneratorModel;
import org.perfcake.ide.core.model.ReportingModel;
import org.perfcake.ide.core.model.ScenarioModel;
import org.perfcake.ide.core.model.SenderModel;
import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.layout.AngularData;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.RadiusData;
import org.perfcake.ide.editor.layout.SimpleCircularLayoutManager;
import org.perfcake.ide.editor.view.ComponentView;
import org.perfcake.ide.editor.view.UnsupportedChildViewException;
import org.perfcake.ide.editor.view.icons.GeneratorIcon;
import org.perfcake.ide.editor.view.impl.EditorView;

import javax.swing.JComponent;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Iterator;

public class EditorController extends AbstractController {

	private static final int DEFAULT_ANGLE_EXTENT = 180;
	private static final int DEFAULT_START_ANGLE = 0;
	private static final int MAXIMUM_INNER_RADIUS = 50;

	private JComponent jComponent;
	private EditorView view;
	private Point2D center;

	private ScenarioModel scenarioModel;


	public EditorController(JComponent jComponent, ScenarioModel model) {
		super();
		this.layoutManager = new SimpleCircularLayoutManager(this);
		this.scenarioModel = model;
		this.jComponent = jComponent;
//		final int numOfSectors = 4;
//		final int angleExtent = 180/numOfSectors;
		view = new EditorView(null);

		for (final AbstractModel child : scenarioModel.getModelChildren()){
			if (child instanceof GeneratorModel){
				final Controller generator = new SectionController("Generator", new GeneratorIcon(), child);
				addChild(generator);
			} else if (child instanceof SenderModel) {
				final Controller sender = new SectionController("Sender", new GeneratorIcon(), child);
				addChild(sender);
			} else if (child instanceof ReportingModel) {
				final Controller reporting = new SectionController("Reporting", new GeneratorIcon(), child);
				addChild(reporting);

			}
		}
		//		for (int i = 0; i < numOfSectors; i++){
		//			final LayoutData childData = new LayoutData(data);
		//			childData.getAngularData().setStartAngle(data.getAngularData().getStartAngle() + i * angleExtent);
		//			childData.getAngularData().setAngleExtent(angleExtent);
		//			final SectionController section = new SectionController("Section", new GeneratorIcon(), childData);
		//			addChild(section);
		//			invalidate();
		//		}
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
		final double outerRadius = (0.9 * Math.min(jComponent.getWidth(), jComponent.getHeight())) / 2;
		final double innerRadius = Math.min(MAXIMUM_INNER_RADIUS, (0.2 * Math.min(jComponent.getWidth(), jComponent.getHeight())) / 2);
		final RadiusData radiusData = new RadiusData(innerRadius, outerRadius);
		final AngularData angularData = new AngularData(DEFAULT_START_ANGLE, DEFAULT_ANGLE_EXTENT);
		final LayoutData data = new LayoutData(jComponent.getWidth(), jComponent.getHeight(), radiusData, angularData);

		setLayoutData(data);

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
