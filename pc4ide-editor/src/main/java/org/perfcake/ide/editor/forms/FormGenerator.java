package org.perfcake.ide.editor.forms;

import javax.swing.JPanel;

/**
 * FormGenerator serves as tool for creating a forms dynamically.
 *
 * @author jknetl
 *
 */
public interface FormGenerator {

	/**
	 * Generate form elements and fill them into the JPanel.
	 *
	 * @param jPanel
	 */
	void createForm(JPanel jPanel);
}
