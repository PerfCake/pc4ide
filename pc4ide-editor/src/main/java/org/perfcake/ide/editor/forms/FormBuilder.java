package org.perfcake.ide.editor.forms;

import javax.swing.JComponent;

/**
 *
 * FormBuilder incrementally builds a from by adding/removing the components. It should manage layout
 * of the components by its own.
 *
 * @author jknetl
 *
 */
public interface FormBuilder {

	/**
	 * Adds component in the form.
	 * @param component
	 */
	void addComponent(JComponent component);

	/**
	 * Remove component from the form.
	 * @param component
	 */
	boolean removeComponent(JComponent component);
}
