package org.perfcake.ide.editor.forms.impl.elements;

import org.perfcake.ide.editor.forms.FormElement;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.util.Arrays;
import java.util.List;

/**
 * Represents form element which expects text data.
 * @author jknetl
 *
 */
public class TextElement implements FormElement {

	private String name;
	private String defaultValue;
	private String docs;

	private JTextField textField;
	private JLabel label;
	private JLabel help;

	public TextElement(String name, String defaultValue, String docs) {
		super();
		this.name = name;
		this.docs = docs;
		this.defaultValue = defaultValue;

		buildComponents();
	}

	private void buildComponents() {
		label = new JLabel(name);
		textField = new JTextField(defaultValue);
		//TODO(jknetl) change for icon
		help = new JLabel(docs);
	}

	@Override
	public List<JComponent> getGraphicalComponents() {
		return Arrays.asList(label, textField, help);
	}

	@Override
	public int indexOfExpandableComponent() {
		return 1;
	}

}
