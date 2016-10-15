/**
 *
 */
package org.perfcake.ide.editor.view.impl;

import org.perfcake.ide.core.components.Component;
import org.perfcake.ide.editor.layout.AngularData;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.RadiusData;
import org.perfcake.ide.editor.layout.SimpleCircularLayoutManager;
import org.perfcake.ide.editor.view.AbstractView;
import org.perfcake.ide.editor.view.ComponentView;

import javax.swing.JComponent;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * @author jknetl
 *
 */
public class EditorView extends AbstractView {



	private static final int DEFAULT_ANGLE_EXTENT = 340;
	private static final int DEFAULT_START_ANGLE = -80;
	private static final int MAXIMUM_INNER_RADIUS = 50;

	private JComponent jComponent;

	public EditorView(JComponent jComponent) {
		super(null);
		this.jComponent = jComponent;
		layoutManager = new SimpleCircularLayoutManager();
		layoutManager.setConstraint(getConstraints());
	}

	/* (non-Javadoc)
	 * @see org.perfcake.ide.editor.view.ComponentView#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics2D g2d) {
		for (ComponentView child : getChildren()){
			child.draw(g2d);
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
	public LayoutData getMinimumSize(LayoutData constraint, Graphics2D g2d) {

//		TODO: Compute size from the content (+ child)
		LayoutData minimumSize = new LayoutData();
		minimumSize.setAngularData(new AngularData(0,30));
		return minimumSize;
	}

	public void invalidate(){
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
		final AngularData angularData = new AngularData(DEFAULT_START_ANGLE, DEFAULT_ANGLE_EXTENT);
		return new LayoutData(jComponent.getWidth(), jComponent.getHeight(), radiusData, angularData);
	}
}
