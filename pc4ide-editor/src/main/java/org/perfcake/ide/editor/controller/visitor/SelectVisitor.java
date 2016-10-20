package org.perfcake.ide.editor.controller.visitor;

import org.perfcake.ide.editor.controller.Controller;

import java.awt.geom.Point2D;

/**
 * Selects a view of the most specific controller.
 *
 * @see ViewTargetedVisitor
 */
public class SelectVisitor extends ViewTargetedVisitor {

	public SelectVisitor(Point2D location) {
		super(location);
	}

	@Override
	protected void performOperation(Controller controller) {
		controller.getView().setSelected(true);
	}
}
