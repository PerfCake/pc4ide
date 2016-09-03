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

	/**
	 * Sets {@link LayoutData} for the layoutManager. This indicates what part of drawing surface may
	 * be used for the children
	 * @param data
	 */
	public void setLayoutData(LayoutData data);

	/**
	 *
	 * @return LayoutData which this component is using.
	 */
	public LayoutData getLayoutData();
}
