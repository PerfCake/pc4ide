package org.perfcake.ide.editor.forms.impl.elements;

import javax.swing.JTextField;

/**
 * Represents form element which expects text data.
 * @author jknetl
 *
 */
public class TextElement extends NamedDocumentedElement {



	public TextElement(String name, String documentation, String defaultValue) {
		super(name, documentation, defaultValue);

		createMainComponent();
	}

	@Override
	void createMainComponent() {
		this.component = new JTextField(defaultValue);
	}

}
