package org.perfcake.ide.editor.forms.impl;

import org.perfcake.ide.editor.forms.FormBuilder;
import org.perfcake.ide.editor.forms.FormElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JComponent;
import javax.swing.JPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

public class FormBuilderImpl implements FormBuilder {

	private static final double WEIGHT_OF_MAIN_COLUMN = 0.6;
	private static final double WEIGHT_OF_OTHER_COLUMNS = 0.2;

	static final Logger logger = LoggerFactory.getLogger(FormBuilderImpl.class);

	private JPanel panel;
	private GridBagLayout layout;

	// represents how many columns are in the layout
	int columns;

	// main column will be expanded horizontally
	int mainColumn;

	// weight of the column except the main
	double weightOfColumn;

	private List<FormElement> elements;

	public FormBuilderImpl(JPanel panel, int columns, int mainColumn) {
		if (panel == null) {
			throw new IllegalArgumentException("Panel must not be null");
		}
		if (columns <= 0) {
			throw new IllegalArgumentException("Max components in row must be positive");
		}
		if (mainColumn < 0 || mainColumn >= columns) {
			throw new IllegalArgumentException("Main column must be in range [0,columns).");
		}

		this.panel = panel;
		this.columns = columns;
		this.mainColumn = mainColumn;

		elements = new ArrayList<>();
		layout = new GridBagLayout();
		if (columns > 1) {
			this.weightOfColumn = WEIGHT_OF_OTHER_COLUMNS / (columns - 1);
		} else {
			//there is only one column so that it must be the main one
			weightOfColumn = WEIGHT_OF_MAIN_COLUMN;
		}

		panel.setLayout(layout);
	}

	@Override
	public void addElement(FormElement element) {
		elements.add(element);

		final int componentsInElement = element.getGraphicalComponents().size();

		int i = 0;
		for (final JComponent component : element.getGraphicalComponents()) {
			if (i == columns) {
				logger.warn("Form elements contains {} components while the Form can handle only {}."
						+ " Some components will not be displayed", componentsInElement, columns);
				break;
			}

			final GridBagConstraints constraints = new GridBagConstraints();
			constraints.gridx = i;
			constraints.gridy = elements.size();
			constraints.anchor = GridBagConstraints.CENTER;
			constraints.weightx = weightOfColumn;

			if (i == mainColumn) {
				constraints.weightx = WEIGHT_OF_MAIN_COLUMN;
				constraints.fill = GridBagConstraints.HORIZONTAL;
			}

			// if there is not enough components then span main one over free components
			if (i == element.getMainComponent()) {
				if (componentsInElement < columns) {
					constraints.gridwidth = columns - componentsInElement;
					i += (constraints.gridwidth - 1);
				}
			}

			panel.add(component, constraints);
			i++;
		}
	}

	@Override
	public boolean removeElement(FormElement element) {
		final boolean removed = elements.remove(element);
		if (removed) {
			for (final JComponent component : element.getGraphicalComponents()) {
				panel.remove(component);
			}
		}

		return removed;
	}

}
