/**
 *
 */
package org.perfcake.ide.editor.view;

import java.awt.Graphics;
import java.awt.geom.Point2D;

/**
 * Base type for component view in editor MVC.
 *
 * @author jknetl
 *
 */
public interface ComponentView {

	/**
	 * draw view on the surface.
	 *
	 */
	public void draw(Graphics g);

	/**
	 * Tests whether the view contains point
	 *
	 * @param point
	 * @return true if this view contains point
	 */
	public boolean containts(Point2D point);

}
