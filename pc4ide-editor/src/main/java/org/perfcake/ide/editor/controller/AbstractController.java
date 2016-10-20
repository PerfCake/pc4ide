/**
 *
 */
package org.perfcake.ide.editor.controller;

import org.perfcake.ide.editor.controller.visitor.ControllerVisitor;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.LayoutManager;
import org.perfcake.ide.editor.view.UnsupportedChildViewException;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
*  AbstractController provides default implementation for some methods from {@link Controller} interface.
*
* AbstractController implements:
* <ol>
* <li> no operation handlers for {@link MouseEvent} </li>
* <li> child management operations </li>
* </ol>
*
* @author jknetl
*
*/
public abstract class AbstractController implements Controller {

	private boolean isValid = false;
	private List<Controller> children = new ArrayList<>();
	private Controller parent = null;

	public AbstractController() {
		super();
	}

	@Override
	public Controller getParent() {
		return parent;
	}

	@Override
	public RootController getRoot() {
		Controller root;
		root = this;
		while (this.getParent() != null) {
			root = this.getParent();
		}

		return (RootController) root;
	}

	@Override
	public void setParent(Controller parent) {
		this.parent = parent;
	}

	@Override
	public void addChild(Controller child) throws UnsupportedChildViewException {
		children.add(child);
		child.setParent(this);
		getView().addChild(child.getView());
		child.getView().setParent(this.getView());
		child.getView().invalidate();
	}

	@Override
	public Iterator<Controller> getChildrenIterator() {
		return children.iterator();
	}

	@Override
	public boolean removeChild(Controller child) {
		final boolean removed = children.remove(child);
		if (removed) {
			child.getView().setParent(null);
			getView().removeChild(getView());
			child.setParent(null);
			getView().invalidate();
		}
		return removed;
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		//empty on purpose
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//empty on purpose
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//empty on purpose
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//empty on purpose
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//empty on purpose
	}

	@Override
	public void accept(ControllerVisitor visitor) {
		visitor.visit(this);
	}
}
