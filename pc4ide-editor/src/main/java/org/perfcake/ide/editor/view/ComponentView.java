/**
 *
 */
package org.perfcake.ide.editor.view;

import org.perfcake.ide.editor.layout.AngularData;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.RadiusData;

import java.awt.Graphics;
import java.awt.Shape;
import java.util.List;

/**
 * Base type for component view in editor MVC.
 *
 * @author jknetl
 *
 */
public interface ComponentView{


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
	 * Get view preferd angular extent.
	 *
	 * @param radius The radiuses which will be used.
	 *
	 * @return {@link AngularData} as a hint for layout manager. If some field is zero then it means no preference.
	 */
	public AngularData getPrefferedAngularData(RadiusData radius);

	/**
	 *
	 * @return Actual layoutData assigned by LayoutManager.
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
	 * @return Views that acts as a child of current view (they are inside of the view)
	 */
	public List<ComponentView> getChildren();

	/**
	 *
	 * @return the view which is parent of the view. Root view will return null.
	 */
	public ComponentView getParent();

	/**
	 * Adds child view
	 * @param view
	 */
	public void addChild(ComponentView view);

	/**
	 * Remove child view
	 * @param view
	 * @return true if the view was removed or false if the view is not children of this view.
	 */
	public boolean removeChild(ComponentView view);

}
