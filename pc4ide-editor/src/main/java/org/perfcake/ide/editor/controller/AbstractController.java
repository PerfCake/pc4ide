/**
 *
 */
package org.perfcake.ide.editor.controller;

import org.perfcake.ide.editor.layout.LayoutData;
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
	private LayoutData planeLayoutData;

	public AbstractController(LayoutData planeLayoutData) {
		super();
		this.planeLayoutData = planeLayoutData;
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
			parent.invalidate();
		}
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

	public LayoutData getPlaneLayoutData() {
		return planeLayoutData;
	}

	public void setPlaneLayoutData(LayoutData planeLayoutData) {
		this.planeLayoutData = planeLayoutData;
	}

}
