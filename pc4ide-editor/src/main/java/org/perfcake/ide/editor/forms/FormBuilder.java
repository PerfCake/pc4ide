package org.perfcake.ide.editor.forms;

/**
 *
 * FormBuilder incrementally builds a from by adding/removing the form elements. It should manage layout
 * of the components by its own.
 *
 * @author jknetl
 *
 */
public interface FormBuilder {

	/**
	 * Adds form element into the form.
	 * @param element
	 */
	void addElement(FormElement element);

	/**
	 * Remove form element from the form.
	 * @param element
	 */
	boolean removeElement(FormElement element);
}
