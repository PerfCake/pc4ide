package org.perfcake.ide.editor.controller.visitor;

import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.view.ComponentView;

import java.util.Iterator;

/**
 * Traverses through all controllers and unselects its view
 */
public class UnselectVisitor implements ControllerVisitor {

	@Override
	public void visit(Controller controller) {
		ComponentView v  = controller.getView();

		if (v != null){
			v.setSelected(false);
		}

		Iterator<Controller> it = controller.getChildrenIterator();

		while (it.hasNext()){
			it.next().accept(this);
		}
	}
}
