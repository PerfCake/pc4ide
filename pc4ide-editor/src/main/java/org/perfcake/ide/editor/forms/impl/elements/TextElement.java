package org.perfcake.ide.editor.forms.impl.elements;

import org.perfcake.ide.core.command.Command;
import org.perfcake.ide.core.command.impl.DirectedSetCommand;
import org.perfcake.ide.core.model.director.ModelDirector;
import org.perfcake.ide.core.model.director.ModelField;

import javax.swing.JTextField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Represents form element which expects text data.
 * @author jknetl
 *
 */
public class TextElement extends FieldElement {

	public TextElement(ModelDirector director, ModelField field) {
		super(director, field);
		createMainComponent();
	}

	@Override
	void createMainComponent() {
		this.component = new JTextField(String.valueOf(director.getModelFieldValue(field)));
		component.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField tf = (JTextField) component;
				Command command = new DirectedSetCommand(director, field, ((JTextField) component).getText());
				command.execute();
			}
		});
	}

}
