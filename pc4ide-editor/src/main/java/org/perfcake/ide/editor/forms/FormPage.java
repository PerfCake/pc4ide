package org.perfcake.ide.editor.forms;

import javax.swing.JPanel;

/**
 * Represent page of the form. The page contains input swing controls which
 * are applied to application data.
 *
 * The FormPage has its model object which serves as container for the data.
 *
 * @author jknetl
 *
 */
public interface FormPage {

	/**
	 * @return True if the data enterend into the form are valid.
	 */
	boolean isValid();

	/**
	 * @return {@link FormManager} which manages this FormPage.
	 */
	FormManager getFormManager();

	/**
	 * @return JPanel with the form.
	 */
	JPanel getContentPanel();

	/**
	 * @return message which may be used by some {@link FormManager}s to display hint to the user.
	 */
	String getMessage();

	/**
	 * Update visual representation of the form.
	 */
	void updateForm();

	/**
	 * Apply changes in this FormPage to the model of the data.
	 */
	void applyChanges();

	/**
	 * @return Object which is model for the form data.
	 */
	Object getModel();

}
