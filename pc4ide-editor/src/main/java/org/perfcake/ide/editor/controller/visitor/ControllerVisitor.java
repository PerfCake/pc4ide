package org.perfcake.ide.editor.controller.visitor;

import org.perfcake.ide.editor.controller.Controller;

/**
 * Controller visitor traverses through Controller composite structure.
 */
public interface ControllerVisitor {

	/**
	 * Visits given controller and all its child controllers.
	 *
	 * @param controller Controller where a traversal is started.
	 */
	void visit(Controller controller);
}
