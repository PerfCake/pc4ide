/**
 *
 */
package org.perfcake.ide.editor.view;

import org.perfcake.ide.editor.layout.AngularData;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.RadiusData;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.List;

/**
 * Base type for component view in editor MVC.
 *
 * @author jknetl
 *
 */
public interface ComponentView {

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
	 * @param g2d Graphics context
	 *
	 */
	public void draw(Graphics2D g2d);

	/**
	 *
	 * @return {@link Shape} which completely encloses this view graphical representation
	 */
	public Shape getViewBounds();

	/**
	 * Computes a minimum size of the view. The constraint argument is used as a constraint for the size. So if
	 * some dimension of constraint argument is N, then returned value in that dimension cannot be larger than N.
	 * If some dimension of constraint argument is zero then there is no constraint on that dimension
	 * @param constraint constraint
	 * @param  g2d Graphics context
	 * @return Minimum size of the component according to given constraints.
	 */
	public LayoutData getMinimumSize(LayoutData constraint, Graphics2D g2d);

	/**
	 *
	 * @return Actual layoutData of the view.
	 */
	public LayoutData getLayoutData();

	/**
	 * Sets layout which will be used for the component on {@link #draw(Graphics)} method.
	 *
	 * @param data
	 */
	public void setLayoutData(LayoutData data);

	/**
	 *
	 * @return <b>unmodifiable list of</b> views that acts as a child of current view (they are inside of the view)
	 */
	public List<ComponentView> getChildren();

	/**
	 * @return the view which is parent of the view. Root view will return null.
	 */
	public ComponentView getParent();

	/**
	 *  Sets a parent of this view.
	 * @param parent
	 */
	public void setParent(ComponentView parent);

	/**
	 * Adds child view
	 * @param view
	 */
	public void addChild(ComponentView view);

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
	 * Validates the view and the view of the children. It means that it sets view sizes and positions so that consequent draw operation
	 * will draw it on proper place with proper size.
	 * @param g2d Graphics context
	 */
	public void validate(Graphics2D g2d);


	/**
	 * Remove child view
	 * @param view
	 * @return true if the view was removed or false if the view is not children of this view.
	 */
	public boolean removeChild(ComponentView view);

}
