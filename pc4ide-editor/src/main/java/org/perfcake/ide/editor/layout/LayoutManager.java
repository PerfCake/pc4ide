/**
 *
 */
package org.perfcake.ide.editor.layout;

import org.perfcake.ide.core.components.Component;
import org.perfcake.ide.editor.view.ComponentView;

import java.awt.Graphics2D;
import java.util.List;

/**
 * Layout manger computes {@link LayoutData} for children components.
 *
 * @author jknetl
 *
 */
public interface LayoutManager {

	/**
	 * Sets layout data to all children views. It may or may not take into consideration
	 * the preferred layout data of the children views.
	 */
	void layout(Graphics2D g2d);

	/**
	 * Sets {@link LayoutData} constraint for the layoutManager. This indicates what part of drawing surface
	 * may be used by this LayoutManager. If some dimension of constraint is zero then it means there is
	 * no constraint on that dimension.
	 * @param constraint
	 */
	void setConstraint(LayoutData constraint);

	/**
	 * Adds a graphical component to the layout so that this layout manager can manage the component.
	 *
	 * @param component component to be managed by this layout manager
	 */
	void add(ComponentView component);

	/**
	 * Removes a graphical component from the layout so that this layout manager won't manager component
	 * anymore.
	 *
	 * @param component component to be removed from this layout manager
	 * @return  true if the component was removed, or false if component couldn't be found.
	 */
	boolean remove(ComponentView component);

	/**
	 * @return <b>Unmodifiable List</b> of all managed component views
	 */
	List<ComponentView> getChildren();

}
