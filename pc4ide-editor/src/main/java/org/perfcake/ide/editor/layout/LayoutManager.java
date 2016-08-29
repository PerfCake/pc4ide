/**
 *
 */
package org.perfcake.ide.editor.layout;

import org.perfcake.ide.editor.view.ComponentView;

/**
 * Layout manger computes {@link LayoutData} for children components.
 *
 * @author jknetl
 *
 */
public interface LayoutManager {

	/**
	 * Computes Layout data for children component.
	 * @param view view of the component which will be laid out
	 * @return LayoutData which can be passed to children component for painting.
	 */
	LayoutData getLayoutData(ComponentView view);

}
