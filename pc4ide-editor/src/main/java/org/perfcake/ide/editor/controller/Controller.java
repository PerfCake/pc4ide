/**
 *
 */
package org.perfcake.ide.editor.controller;

import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.view.ComponentView;
import org.perfcake.ide.editor.view.UnsupportedChildViewException;

import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.util.Iterator;

/**
 * @author jknetl
 *
 */
public interface Controller extends MouseListener {

	/**
	 *
	 * @return controller of the parent component. The root controller returns null.
	 */
	public Controller getParent();

	/**
	 * Sets parent controller of this controller. This method should be called only when
	 * the controller is added/removed as children view to other view.
	 */
	public void setParent(Controller parent);

	/**
	 * Adds controller as a child controller
	 * @param child
	 * @throws UnsupportedChildViewException when the controller is not supported by this {@link ComponentView}
	 */
	public void addChild(Controller child) throws UnsupportedChildViewException;

	/**
	 * Remove child.
	 * @param child
	 * @return true if child was removed. False if the child was not found.
	 */
	public boolean removeChild(Controller child);

	/**
	 *
	 * @return iterator over children.
	 */
	public Iterator<Controller> getChildrenIterator();

	/**
	 *
	 * @return true if the view is valid (up to date)
	 */
	public boolean isValid();

	/**
	 * Invalidates view to indicate that it needs to be redrawn
	 */
	public void invalidate();

	/**
	 * Validates the views of the children. It means that it sets view sizes and positions so that consequent draw operation
	 * will draw it on proper place with proper size.
	 */
	public void validate();


	/**
	 * Draws the corresponding view.
	 * @param g
	 */
	public void drawView(Graphics g);

	/**
	 *
	 * @return associated view
	 */
	public ComponentView getView();

	/**
	 * Sets {@link LayoutData} for the controller. This indicates what part of drawing surface may
	 * be used for the controller.
	 * @param data
	 */
	public void setLayoutData(LayoutData data);

	/**
	 *
	 * @return LayoutData which this component is using.
	 */
	public LayoutData getLayoutData();

}