package org.perfcake.ide.editor.forms.impl;

import org.perfcake.ide.core.components.Component;
import org.perfcake.ide.core.components.ComponentKind;
import org.perfcake.ide.core.components.ComponentManager;
import org.perfcake.ide.editor.forms.FormBuilder;
import org.perfcake.ide.editor.forms.FormElement;
import org.perfcake.ide.editor.forms.FormGenerator;
import org.perfcake.ide.editor.forms.FormPageDirector;
import org.perfcake.ide.editor.forms.impl.elements.TextElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JPanel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * ReflectionFormGenerator reflectively inspects the model of the form and creates
 * graphical control elements based on its inspection.
 *
 * @author jknetl
 *
 */
public class ReflectionFormGenerator implements FormGenerator {

	private static final int DEFAULT_MAX_COMPONETNS_IN_ROW = 3;

	static final Logger logger = LoggerFactory.getLogger(ReflectionFormGenerator.class);

	private Object model;
	private FormPageDirector director;
	private ComponentManager componentManager;
	private JPanel form;

	private FormBuilder formBuilder;


	public ReflectionFormGenerator(Object model, FormPageDirector director, ComponentManager componentManager, JPanel form) {
		super();
		this.model = model;
		this.director = director;
		this.componentManager = componentManager;
		this.form = form;
		formBuilder = new FormBuilderImpl(form, DEFAULT_MAX_COMPONETNS_IN_ROW);
	}

	void setModel(Object model, FormPageDirector formDirector) {
		if (model == null) {
			throw new IllegalArgumentException("Model cannot be null");
		}
		this.model = model;
	}

	@Override
	public void createForm() {

		final List<FormElement> elements = inspectModelFields();

		for (final FormElement element : elements) {
			formBuilder.addElement(element);
		}

	}

	private List<FormElement> inspectModelFields() {

		final List<FormElement> elements = new ArrayList<>();

		final Class<?> modelClazz = model.getClass();

		for (final Method method : modelClazz.getMethods()) {

			if (method.getName().startsWith("set")) {
				if (method.getParameterCount() == 1) {
					final Class<?> argumentType = method.getParameterTypes()[0];
					if (argumentType.isPrimitive() || String.class.equals(argumentType)) {
						final String name = method.getName().substring(3);
						String defaultValue = "";

						Method getMethod = null;
						final String getMethodName = "get" + name;
						try {
							getMethod = modelClazz.getMethod(getMethodName);
							defaultValue = String.valueOf(getMethod.invoke(model, new Object[] {}));

						} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							logger.warn("Cannot obtain value using method {}()", getMethodName);
						}

						String docs = null;

						final ComponentKind kind = ComponentKind.getComponentKindByClazz(modelClazz);
						if (kind != null) {
							final Component component = componentManager.getComponent(kind);
							if (component != null) {
								final String fieldName = name.substring(0, 1).toLowerCase() + name.substring(1);
								docs = componentManager.getFieldDocumentation(component, fieldName);
							}

						}

						elements.add(new TextElement(name, defaultValue, docs));
					}
				}
			}

			if (method.getName().startsWith("add") && method.getParameterCount() == 1) {

			}

		}

		return elements;
	}
}
