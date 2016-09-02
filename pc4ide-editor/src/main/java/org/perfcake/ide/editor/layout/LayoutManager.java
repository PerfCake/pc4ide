/**
 *
 */
package org.perfcake.ide.editor.layout;

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
	void layoutChildren();

}
