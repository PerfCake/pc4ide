package org.perfcake.ide.editor.forms;

/**
 * FormPageDirector may be used within the form page. It manages application data and input from the
 * user and coordinates operations which requires both types the data.
 *
 * @author jknetl
 *
 */
public interface FormPageDirector {

	/**
	 * @return True if data in the form are valid according to underlying application data model.
	 */
	boolean isValid();

	/**
	 * @return True if the data in the model and data in the form are in sync.
	 */
	boolean isSynced();

	/**
	 * Adds {@link EventHandler}.
	 * @param handler
	 */
	void addHandler(EventHandler handler);

	/**
	 * Remove {@link EventHandler}.
	 * @param handler
	 * @return True if the handler was successfuly removed. False if it was not found in the handlers.
	 */
	boolean removeHandler(EventHandler handler);

	/**
	 * Applies changes from the form to the underlying data model.
	 */
	void applyChanges();

	/**
	 * @return Message which may be used as a hint for the user.
	 */
	String getMessage();
}
