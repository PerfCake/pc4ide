package org.perfcake.ide.editor.forms;

import org.perfcake.ide.core.components.ComponentManager;

import javax.swing.JPanel;

import java.util.List;

/**
 * FormManger serves as container for the actual forms. Actual forms with text fields and buttons
 * are represented by {@link FormPage}. FormManager aggregates these pages and adds control for whole
 * forms.
 *
 * The FormManager has ContainerPanel which contains all swing components. It has also content panel which is
 * part of ContainerPanel. ContentPanel contains only the swing components whose user will fill in (not the general form
 * control buttons).
 *
 * @author jknetl
 *
 */
public interface FormManager {

	/**
	 * @return True if the data in the form are valid.
	 */
	boolean isValid();

	/**
	 * @return Component manager used in program.
	 */
	ComponentManager getComponentManager();

	/**
	 * ContainerPanel contains all form including its control and its content.
	 *
	 * @return JPanel including all visuals in the form.
	 */
	JPanel getContainerPanel();

	/**
	 * Content panel contains actual form. It does not include pane with form control buttons.
	 */
	JPanel getContentPanel();

	/**
	 * Adds page of the form.
	 *
	 * @param page Page to be added
	 */
	void addFormPage(FormPage page);

	/**
	 * Remove page from the form
	 * @param page page to be removed
	 * @return true if the page was successfuly removed or false if it was not found in the form.
	 *
	 */
	boolean removePage(FormPage page);

	/**
	 * Removes all pages from the manager.
	 */
	void removeAllPages();

	/**
	 * Return <b>unmodifiable</b> collection of form pages.
	 * @return
	 */
	List<FormPage> getFormPages();


	/**
	 * Apply changes in the all form pages to the model of the data.
	 */
	void applyChanges();
}
