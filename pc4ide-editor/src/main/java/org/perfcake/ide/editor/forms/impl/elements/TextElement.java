package org.perfcake.ide.editor.forms.impl.elements;

import javax.swing.JTextField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
		component.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				System.out.print(e.getKeyChar());
				super.keyReleased(e);
			}
		});
	}

}
