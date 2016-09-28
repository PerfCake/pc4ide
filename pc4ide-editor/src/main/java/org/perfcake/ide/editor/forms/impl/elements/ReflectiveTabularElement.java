package org.perfcake.ide.editor.forms.impl.elements;

import org.perfcake.ide.editor.forms.FormElement;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import java.util.Arrays;
import java.util.List;

/**
 * ReflectiveTabularElement is a form element which displays collection of fields. It uses reflection
 * to determine the columns of the model.
 * @author jknetl
 *
 */
public class ReflectiveTabularElement implements FormElement {

	private static final String EDIT_BUTTON_TEXT = "edit";
	public static final String ADD_BUTTON_TEXT = "add";
	public static final String REMOVE_BUTTON_TEXT = "edit";
	private String name;
	private Object model;
	private List<Object> rows;

	private JLabel label;
	private JTable table;

	private JPanel controlPanel;
	private JButton addButton;
	private JButton editButton;
	private JButton removeButton;

	public ReflectiveTabularElement(String name, Object model, List<Object> rows) {
		super();
		this.name = name;
		this.model = model;
		this.rows = rows;

		label = new JLabel(name);

		//table
		final String[] headerNames = getHeaderNames(rows);
		final Object[][] rowData = getRowData(rows);
		table = new JTable(rowData, headerNames);
		//controls
		addButton = new JButton(ADD_BUTTON_TEXT);
		editButton = new JButton(EDIT_BUTTON_TEXT);
		removeButton = new JButton(REMOVE_BUTTON_TEXT);

		controlPanel = new JPanel();
		final BoxLayout layout = new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS);
		controlPanel.setLayout(layout);

		controlPanel.add(addButton);
		controlPanel.add(editButton);
		controlPanel.add(removeButton);



	}

	private Object[][] getRowData(List<Object> rows2) {
		// TODO reflectively get data
		final String[][] rowData = new String[rows2.size()][];

		for (int i = 0; i < rowData.length ; i++){
			rowData[i] = new String[] {rows2.get(i).getClass().getSimpleName()};
		}

		return rowData;
	}

	private String[] getHeaderNames(List<Object> rows2) {
		//TODO: reflectively get headers
		return new String[] {"row"};
	}

	@Override
	public List<JComponent> getGraphicalComponents() {
		return Arrays.asList(label, table, controlPanel);
	}

	@Override
	public int getMainComponent() {
		return 1;
	}

}
