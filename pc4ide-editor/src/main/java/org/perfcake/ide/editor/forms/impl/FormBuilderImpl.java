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

	static final Logger logger = LoggerFactory.getLogger(FormBuilderImpl.class);

	private JPanel panel;
	private GridBagLayout layout;
	private GridBagConstraints constraints;

	// represents how many columns are in the layout
	int maxComponentsInRow;

	private List<FormElement> elements;

	public FormBuilderImpl(JPanel panel, int maxComponentsInRow) {
		if (panel == null) {
			throw new IllegalArgumentException("Panel must not be null");
		}
		if (maxComponentsInRow <= 0) {
			throw new IllegalArgumentException("Max components in row must be positive");
		}
		this.panel = panel;
		this.maxComponentsInRow = maxComponentsInRow;

		elements = new ArrayList<>();
		layout = new GridBagLayout();
		constraints = new GridBagConstraints();

		panel.setLayout(layout);
	}

	@Override
	public void addElement(FormElement element) {
		elements.add(element);

		final int componentsInElement = element.getGraphicalComponents().size();

		int i = 0;
		for (final JComponent component : element.getGraphicalComponents()) {
			if (i == maxComponentsInRow) {
				logger.warn("Form elements contains {} components while the Form can handle only {}."
						+ " Some components will not be displayed", componentsInElement, maxComponentsInRow);
				break;
			}

			final GridBagConstraints constraints = new GridBagConstraints();
			constraints.gridx = i;
			constraints.gridy = elements.size();
			constraints.anchor = GridBagConstraints.CENTER;

			if (i == element.indexOfExpandableComponent()) {
				constraints.fill = GridBagConstraints.HORIZONTAL;
				if (componentsInElement < maxComponentsInRow) {
					constraints.gridwidth = maxComponentsInRow - componentsInElement;
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
