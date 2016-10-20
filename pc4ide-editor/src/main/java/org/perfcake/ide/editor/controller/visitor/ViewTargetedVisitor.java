package org.perfcake.ide.editor.controller.visitor;

import org.perfcake.ide.editor.controller.Controller;

import java.awt.geom.Point2D;
import java.util.Iterator;

/**
 * Targeted visitor is a visitor which does not visit all Controllers in the hierarchy but
 * traverses through a tree to the particular controller where it performs an operation. It aims to
 * the controller which has a view on a given location.
 *
 * There may be multiple views at the given coordinates. In that case the visitor aims to the most
 * specific controller (the one which has the most depth in controller hierarchy).
 *
 * If no controller has view on the given coordinates then operation is not executed.
 *
 */
public abstract class ViewTargetedVisitor implements ControllerVisitor{

	protected Point2D location;

	public ViewTargetedVisitor(Point2D location) {
		this.location = location;
	}

	/**
	 * Doesn't traverse all hierarchy but aims to specific controller
	 *
	 * @param controller Controller where to start a traversal
	 */
	@Override
	public void visit(Controller controller) {
		if (controller.getView() != null && controller.getView().getViewBounds().contains(location)){
			Controller moreSpecificTarget = findMoreSpecifcTarget(controller);

			if (moreSpecificTarget == null){
				performOperation(controller);
			} else {
				moreSpecificTarget.accept(this);
			}
		}
	}

	private Controller findMoreSpecifcTarget(Controller controller){
		Controller moreSpecificController = null;

		Iterator<Controller> it = controller.getChildrenIterator();
		while (it.hasNext()){
			Controller childController = it.next();
			if (childController.getView() != null && childController.getView().getViewBounds().contains(location)){
				moreSpecificController = childController;
				break;
			}
		}

		return moreSpecificController;
	}

	/**
	 * Performs a given operation on the most specifc controller which has a view on given coordinates.
	 * Most specific controller means the one which has largest depth in the controllers hierarchy. If there are
	 * more contorllers on the same level which both have their view on the location then most specific controller
	 * is undefined and any of them may be chosen as a targed of the operation.
	 * @param controller Controller where operation is performed
	 */
	protected abstract void performOperation(Controller controller);
}
