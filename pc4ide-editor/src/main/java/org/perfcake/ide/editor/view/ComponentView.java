/**
 *
 */
package org.perfcake.ide.editor.view;

import java.awt.Graphics;
import java.awt.Shape;
import java.awt.event.MouseListener;
import java.util.Iterator;

/**
 * Base type for component view in editor MVC.
 *
 * @author jknetl
 *
 */
public interface ComponentView extends MouseListener {


	/**
	 *
	 * @return parent view of the component. The root view returns null.
	 */
	public ComponentView getParentView();

	/**
	 * Sets parent view of this view. This method should be called only when the view is added/removed as children view to other view.
	 */
	public void setParent(ComponentView parent);

	/**
	 * Adds view as a child view
	 * @param child
	 * @throws UnsupportedChildViewException when the child is not supported by this {@link ComponentView}
	 */
	public void addChild(ComponentView child) throws UnsupportedChildViewException;

	/**
	 * Remove child.
	 * @param child
	 * @return true if child was removed. False if the child was not found.
	 */
	public boolean removeChild(ComponentView child);

	/**
	 *
	 * @return iterator over children.
	 */
	public Iterator<ComponentView> getChildrenIterator();
	/**
	 * @return true if the view is currently selected
	 */
	public boolean isSelected();

	/**
	 * toggle the view selection
	 * @param selected
	 */
	public void setSelected(boolean selected);

	/**
	 * draw view on the surface.
	 *
	 */
	public void draw(Graphics g);

	/**
	 *
	 * @return {@link Shape} which completely encloses this view graphical representation
	 */
	public Shape getViewBounds();

	/**
	 *
	 * @return true if the view graphical representation is properly rendered
	 */
	public boolean isValid();

	/**
	 * Invalidates view to indicate that it needs to be redrawn
	 */
	public void invalidate();
}
