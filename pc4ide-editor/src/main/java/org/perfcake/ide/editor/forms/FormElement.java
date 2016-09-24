package org.perfcake.ide.editor.forms;

import javax.swing.JComponent;

import java.util.List;

/**
 * FormElement represents element of the form. It usually contains multiple swing elements which
 * are related to one model element.
 *
 * E.g. name of the element (JLabel) + inputField (JTextField) + description
 *
 * @author jknetl
 *
 */
public interface FormElement {

	/**
	 * @return Graphical component representing the element or EMPTY_LIST
	 */
	List<JComponent> getGraphicalComponents();

	/**
	 *
	 * This methods is hint for layout manager. If the manager needs to expand some component it may
	 * take a hint from this method.
	 *
	 * @return index of component in the {@link #getGraphicalComponents()} list which should be
	 * expanded by the layout manager if required.
	 */
	int indexOfExpandableComponent();
}
