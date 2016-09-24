package org.perfcake.ide.editor.forms.impl.elements;

import javax.swing.JComboBox;

import java.util.List;

public class ChoiceElement extends NamedDocumentedElement {

	private List<String> values;

	public ChoiceElement(String name, String documentation, String defaultValue, List<String> values) {
		super(name, documentation, defaultValue);
		if (values == null || values.isEmpty()){
			throw new IllegalArgumentException("values must not be neither null or empty.");
		}
		this.values = values;

		createMainComponent();
	}

	@Override
	void createMainComponent() {
		this.component = new JComboBox<>(values.toArray(new String[values.size()]));
	}

}
