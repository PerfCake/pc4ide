/**
 *
 */
package org.perfcake.ide.editor.view;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * AbstractView provides default implementation for some methods from {@link ComponentView} interface.
 *
 * AbstractView implements:
 * <ol>
 * <li> no operation handlers for {@link MouseEvent} </li>
 * <li> child management operations </li>
 * </ol>
 *
 * @author jknetl
 *
 */
public abstract class AbstractView implements ComponentView {

	private boolean isValid = false;
	private boolean isSelected = false;
	private List<ComponentView> children = new ArrayList<>();
	private ComponentView parent = null;

	@Override
	public ComponentView getParentView() {
		return parent;
	}

	@Override
	public void setParent(ComponentView parent) {
		this.parent = parent;
	}

	@Override
	public void addChild(ComponentView child) throws UnsupportedChildViewException {
		children.add(child);
		child.setParent(this);
		invalidate();
	}

	@Override
	public Iterator<ComponentView> getChildrenIterator() {
		return children.iterator();
	}

	@Override
	public boolean removeChild(ComponentView child) {
		boolean removed = children.remove(child);
		if (removed){
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
		if (parent != null){
			parent.invalidate();
		}
	}

	@Override
	public boolean isSelected() {
		return isSelected;
	}

	@Override
	public void setSelected(boolean selected) {
		if (this.isSelected != selected){
			this.isSelected = selected;
			invalidate();
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

}
