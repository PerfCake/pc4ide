package org.perfcake.ide.editor.swing;

import org.perfcake.ide.core.components.ComponentManager;
import org.perfcake.ide.core.model.ScenarioModel;
import org.perfcake.ide.core.model.director.ReflectiveModelDirector;
import org.perfcake.ide.editor.forms.FormManager;
import org.perfcake.ide.editor.forms.FormPage;
import org.perfcake.ide.editor.forms.impl.FormManagerImpl;
import org.perfcake.ide.editor.forms.impl.SimpleFormPage;

import javax.swing.JSplitPane;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class EditorJPanel extends JSplitPane {

	private ScenarioModel scenario;
	private GraphicalEditorJPanel graphicalEditorPanel;
	private FormManager formManager;

	public EditorJPanel(ScenarioModel scenario) {
		super(JSplitPane.HORIZONTAL_SPLIT);
		//		final BorderLayout layout = new BorderLayout();
		//		setLayout(layout);
		this.scenario = scenario;


		final InputStream javadocStream = this.getClass().getResourceAsStream(ComponentManager.JAVADOC_LOCATION_CLASSPATH);
		final List<String> packagesList = Arrays.asList(ComponentManager.PACKAGES_WITH_COMPONENTS);
		final ComponentManager componentManager = new ComponentManager(javadocStream, packagesList);
		formManager = new FormManagerImpl(componentManager);

		graphicalEditorPanel = new GraphicalEditorJPanel(scenario, formManager);
//		final FormPage generatorPage = new SimpleFormPage(formManager, new ReflectiveModelDirector(scenario.getGenerator(), componentManager));
//		formManager.addFormPage(generatorPage);

		setLeftComponent(graphicalEditorPanel);
		setRightComponent(formManager.getContainerPanel());

		//		setDividerLocation(getWidth() - 200);
		//		this.add(graphicalEditorPanel, BorderLayout.CENTER);
		//		this.add(formPanel, BorderLayout.LINE_END);
		//		formPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				setDividerLocation(0.7);
			}
		});
	}


}
