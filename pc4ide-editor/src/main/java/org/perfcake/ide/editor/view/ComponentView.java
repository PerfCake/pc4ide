/**
 *
 */
package org.perfcake.ide.editor.view;

import java.awt.Graphics;
import java.awt.Shape;

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
	 *
	 * @return {@link Shape} which completely encloses this view graphical representation
	 */
	public Shape getViewBounds();

}
