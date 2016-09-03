/**
 *
 */
package org.perfcake.ide.editor.controller;

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

	protected LayoutManager layoutManager;

	public AbstractController() {
		super();
	}

	@Override
	public Controller getParent() {
		return parent;
	}

	@Override
	public void setParent(Controller parent) {
		this.parent = parent;
	}

	@Override
	public void addChild(Controller child) throws UnsupportedChildViewException {
		children.add(child);
		child.setParent(this);
		invalidate();
	}

	@Override
	public Iterator<Controller> getChildrenIterator() {
		return children.iterator();
	}

	@Override
	public boolean removeChild(Controller child) {
		final boolean removed = children.remove(child);
		if (removed) {
			child.setParent(null);
			invalidate();
		}
		return removed;
	}

	@Override
	public boolean isValid() {
		return isValid;
	}

	@Override
	public void invalidate() {
		isValid = false;
		if (parent != null) {
			// indicate to parent that this controller needs validation
			parent.invalidate();
		} else {
			// if this controller is root then perform validation
			validate();
		}
	}

	@Override
	public void validate() {
		layoutManager.layoutChildren();
		for (final Controller controller : children) {
			controller.validate();
		}
	}

	@Override
	public LayoutData getLayoutData() {
		return layoutManager.getLayoutData();
	}

	@Override
	public void setLayoutData(LayoutData layoutData) {
		layoutManager.setLayoutData(layoutData);
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

}
