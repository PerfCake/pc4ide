package org.perfcake.ide.editor.controller.visitor;

import org.perfcake.ide.core.model.director.ReflectiveModelDirector;
import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.forms.FormManager;
import org.perfcake.ide.editor.forms.FormPage;
import org.perfcake.ide.editor.forms.impl.SimpleFormPage;

import java.awt.geom.Point2D;

/**
 * Selects a view of the most specific controller.
 *
 * @see ViewTargetedVisitor
 */
public class SelectVisitor extends ViewTargetedVisitor {

	private FormManager formManager;

	public SelectVisitor(Point2D location, FormManager formManager) {
		super(location);
		this.formManager = formManager;
	}

	@Override
	protected void performOperation(Controller controller) {
		controller.getView().setSelected(true);
		formManager.removeAllPages();
		//TODO: (you should have some kind of factory method for the creating directors!!!)
		FormPage page = new SimpleFormPage(formManager, new ReflectiveModelDirector(controller.getModel(),formManager.getComponentManager()));
		formManager.addFormPage(page);
	}
}
