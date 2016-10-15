/**
 *
 */
package org.perfcake.ide.editor.layout.impl;

import static org.kie.api.runtime.rule.Variable.v;

import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.LayoutManager;
import org.perfcake.ide.editor.view.ComponentView;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Simple circular manager lays out the children into circular secotrs. It computes angular
 * extent of each child equally so that each child has same extent.
 *
 * @author jknetl
 */
public class SimpleCircularLayoutManager extends org.perfcake.ide.editor.layout.AbstractLayoutManager {

	public SimpleCircularLayoutManager() {
		super();
	}

	@Override
	public void layout(Graphics2D g2d) {

		final int numOfChildren = computeChildren();

		final double angularExtendForChild = constraints.getAngularData().getAngleExtent() / numOfChildren;
		double startAngle = constraints.getAngularData().getStartAngle();

		for (ComponentView v : getChildren()) {
			final LayoutData data = new LayoutData(constraints);
			data.getAngularData().setAngleExtent(angularExtendForChild);
			data.getAngularData().setStartAngle(startAngle);
			//sets layout data to child layoutManager
			//TODO(jknetl): there should be called setLayoutData only once!!!
			v.setLayoutData(data);
			//sets layout data to child view
			v.setLayoutData(data);
			startAngle += angularExtendForChild;
		}
	}

	private int computeChildren() {
		return (getChildren() == null) ? 0 : getChildren().size();
	}
}
