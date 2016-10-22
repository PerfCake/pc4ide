package org.perfcake.ide.editor.forms.impl.elements;

import org.perfcake.ide.core.command.Command;
import org.perfcake.ide.core.command.impl.DirectedSetCommand;
import org.perfcake.ide.core.model.director.ModelDirector;
import org.perfcake.ide.core.model.director.ModelField;

import javax.swing.JComboBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ChoiceElement extends FieldElement {

	private List<String> values;

	public ChoiceElement(ModelDirector director, ModelField field, List<String> values) {
		super(director, field);
		if (values == null || values.isEmpty()){
			throw new IllegalArgumentException("values must not be neither null or empty.");
		}
		this.values = values;

		createMainComponent();
	}

	@Override
	void createMainComponent() {
		JComboBox<String> comboBox = new JComboBox<>(values.toArray(new String[values.size()]));
		comboBox.setSelectedItem(director.getModelFieldValue(field));
		this.component = comboBox;

		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Command command = new DirectedSetCommand(director, field, String.valueOf(comboBox.getSelectedItem()));
				command.execute();
			}
		});

	}

}
