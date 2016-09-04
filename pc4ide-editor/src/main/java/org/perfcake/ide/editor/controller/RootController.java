/**
 *
 */
package org.perfcake.ide.editor.controller;

import javax.swing.JComponent;

/**
 * Represents top-level controller.
 *
 * @author jknetl
 *
 */
public interface RootController extends Controller {

	/**
	 *
	 * @return Jcomponent on which the editor is drawn.
	 */
	JComponent getJComponent();
}
