package org.perfcake.ide.editor.swing;

import org.perfcake.ide.core.model.ScenarioModel;

import javax.swing.JSplitPane;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class EditorJPanel extends JSplitPane {

	private ScenarioModel scenario;
	private GraphicalEditorJPanel graphicalEditorPanel;
	private FormPanel formPanel;

	public EditorJPanel(ScenarioModel scenario) {
		super(JSplitPane.HORIZONTAL_SPLIT);
		//		final BorderLayout layout = new BorderLayout();
		//		setLayout(layout);
		this.scenario = scenario;

		graphicalEditorPanel = new GraphicalEditorJPanel(scenario);
		formPanel = new FormPanel();

		setLeftComponent(graphicalEditorPanel);
		setRightComponent(formPanel);

		setDividerLocation(getWidth() - 100);
		//		this.add(graphicalEditorPanel, BorderLayout.CENTER);
		//		this.add(formPanel, BorderLayout.LINE_END);
		//		formPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				setDividerLocation(0.9);
			}
		});
	}


}
