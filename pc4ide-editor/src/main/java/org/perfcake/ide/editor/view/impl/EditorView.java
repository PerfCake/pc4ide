/**
 *
 */
package org.perfcake.ide.editor.view.impl;

import static org.kie.api.runtime.rule.Variable.v;

import static javafx.scene.input.KeyCode.V;

import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.layout.AngularData;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.RadiusData;
import org.perfcake.ide.editor.layout.impl.PerfCakeEditorLayoutManager;
import org.perfcake.ide.editor.view.AbstractView;
import org.perfcake.ide.editor.view.ComponentView;

import javax.swing.JComponent;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.util.Iterator;

/**
 * @author jknetl
 */
public class EditorView extends AbstractView {

	private static final int MAXIMUM_ANGLE_EXTENT = 340;
	private static final int MAXIMUM_INNER_RADIUS = 150;

	private JComponent jComponent;

	public EditorView(JComponent jComponent) {
		super(null);
		this.jComponent = jComponent;
		layoutManager = new PerfCakeEditorLayoutManager();
		layoutManager.setConstraint(getConstraints());
	}

	/* (non-Javadoc)
	 * @see org.perfcake.ide.editor.view.ComponentView#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics2D g2d) {
		for (ComponentView child : getChildren()) {
			child.draw(g2d);
		}
	}

	/* (non-Javadoc)
	 * @see org.perfcake.ide.editor.view.ComponentView#getViewBounds()
	 */
	@Override
	public Shape getViewBounds() {
		Area area = new Area();
		for (ComponentView v : getChildren()) {
			Area childArea = new Area(v.getViewBounds());
			area.add(childArea);
		}

		return area;
	}

	@Override
	public LayoutData getMinimumSize(LayoutData constraint, Graphics2D g2d) {
		LayoutData minimumSize = new LayoutData(constraint);
		double angularExtent = 0.0;
		for (ComponentView child : getChildren()) {
			angularExtent += child.getMinimumSize(constraint, g2d).getAngularData().getAngleExtent();
		}
		minimumSize.getAngularData().setAngleExtent(angularExtent);
		return minimumSize;
	}

	public void invalidate() {
		//EditorView is a root so it may trigger validation
		Graphics2D g2d = (Graphics2D) jComponent.getGraphics();
		validate(g2d);
	}

	@Override
	public void validate(Graphics2D g2d) {
		// get drawing surface constraints
		final LayoutData data = getConstraints();
		layoutManager.setConstraint(data);

		super.validate(g2d);

		jComponent.repaint();
	}

	private LayoutData getConstraints() {
		final double outerRadius = (0.9 * Math.min(jComponent.getWidth(), jComponent.getHeight())) / 2;
		final double innerRadius = Math.min(MAXIMUM_INNER_RADIUS, (0.2 * Math.min(jComponent.getWidth(), jComponent.getHeight())) / 2);
		final RadiusData radiusData = new RadiusData(innerRadius, outerRadius);
		final AngularData angularData = new AngularData(0, MAXIMUM_ANGLE_EXTENT);
		return new LayoutData(jComponent.getWidth(), jComponent.getHeight(), radiusData, angularData);
	}
}
