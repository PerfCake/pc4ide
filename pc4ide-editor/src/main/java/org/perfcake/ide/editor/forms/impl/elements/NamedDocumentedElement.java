/**
 *
 */
package org.perfcake.ide.editor.forms.impl.elements;

import org.perfcake.ide.editor.forms.FormElement;

import javax.swing.JComponent;
import javax.swing.JLabel;

import java.util.Arrays;
import java.util.List;

/**
 * Abstract class for the element which consists of three components:
 * <ol>
 * <li>label with name</li>
 * <li>specific component defined by subclass</li>
 * <li>hint or help for the user </li>
 * </ol>
 *
 * Subclasses are required to override {@link #createMainComponent()} and call this method  at the end of initialization
 *
 * @author jknetl
 *
 */
public abstract class NamedDocumentedElement implements FormElement {

	protected String name;
	protected String documentation;
	protected String defaultValue;

	protected JLabel label;
	protected JComponent component;
	protected JLabel docsLabel;

	public NamedDocumentedElement(String name, String documentation, String defaultValue) {
		this.name = name;
		this.documentation = documentation;
		this.defaultValue = defaultValue;

		label = new JLabel(name);

		//TODO(jknetl) change for icon
		docsLabel = new JLabel("<info>");
		docsLabel.setToolTipText(documentation);
	}

	/**
	 * Creates main component of the element ans stores it into {@link #component} field. This method is
	 * not called automatically so the subclass must call this method on its own.
	 *
	 */
	abstract void createMainComponent();

	@Override
	public List<JComponent> getGraphicalComponents() {
		return Arrays.asList(label, component, docsLabel);
	}

	@Override
	public int getMainComponent() {
		return 1;
	}
}
