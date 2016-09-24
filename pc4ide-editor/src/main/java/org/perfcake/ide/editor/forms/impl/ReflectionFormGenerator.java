package org.perfcake.ide.editor.forms.impl;

import org.perfcake.ide.core.components.Component;
import org.perfcake.ide.core.components.ComponentKind;
import org.perfcake.ide.core.components.ComponentManager;
import org.perfcake.ide.editor.forms.FormBuilder;
import org.perfcake.ide.editor.forms.FormElement;
import org.perfcake.ide.editor.forms.FormGenerator;
import org.perfcake.ide.editor.forms.FormPageDirector;
import org.perfcake.ide.editor.forms.impl.elements.ChoiceElement;
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

	private static final int MAIN_COLUMN = 1;
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
		formBuilder = new FormBuilderImpl(form, DEFAULT_MAX_COMPONETNS_IN_ROW, MAIN_COLUMN);
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
						final String fieldName = getFieldName(method);
						final String defaultValue = getFormElementDefaultValue(fieldName);
						final String docs = getElementDocumentation(fieldName);

						final FormElement element;
						if ("clazz".equals(fieldName)) {
							final List<String> values = getImplementationNames(modelClazz);
							element = new ChoiceElement(fieldName, docs, defaultValue, values);
						} else {
							element = new TextElement(fieldName, docs, defaultValue);
						}
						elements.add(element);
					}
				}
			}

			if (method.getName().startsWith("add") && method.getParameterCount() == 1) {

			}

		}

		return elements;
	}

	private String getElementDocumentation(final String fieldName) {
		String docs = null;
		final Class<?> modelClazz = model.getClass();
		final ComponentKind kind = ComponentKind.getComponentKindByModelClazz(modelClazz);
		if (kind != null) {
			final Component component = componentManager.getComponent(kind);
			if (component != null) {
				docs = componentManager.getFieldDocumentation(component, fieldName);
			}
		}

		return docs;
	}

	private String getFormElementDefaultValue(String fieldName) {
		String defaultValue = "";

		Method getMethod = null;
		final String getMethodName = "get" + firstToUpperCase(fieldName);
		final Class<?> modelClazz = model.getClass();
		try {
			getMethod = modelClazz.getMethod(getMethodName);
			defaultValue = String.valueOf(getMethod.invoke(model, new Object[] {}));

		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.warn("Cannot obtain value using method {}()", getMethodName);
		}
		return defaultValue;
	}

	/**
	 *
	 * @param method
	 * @return FieldName derived from the get method
	 */
	private String getFieldName(final Method method) {
		final String name = firstToLowerCase(method.getName().substring(3));
		return name;
	}

	private List<String> getImplementationNames(Class<?> modelClazz) {
		final List<String> list = new ArrayList<>();

		final ComponentKind kind = ComponentKind.getComponentKindByModelClazz(modelClazz);

		final List<Component> implementations = componentManager.getComponentImplementations(kind);

		for (final Component c : implementations){
			list.add(c.getImplementation().getSimpleName());
		}

		return list;
	}

	private String firstToUpperCase(String s) {
		if (s == null || s.isEmpty()) {
			return s;
		}

		final StringBuilder builder = new StringBuilder()
				.append(Character.toUpperCase(s.charAt(0)));
		if (s.length() > 1) {
			builder.append(s.substring(1));
		}

		return builder.toString();
	}

	private String firstToLowerCase(String s) {
		if (s == null || s.isEmpty()) {
			return s;
		}

		final StringBuilder builder = new StringBuilder()
				.append(Character.toLowerCase(s.charAt(0)));
		if (s.length() > 1) {
			builder.append(s.substring(1));
		}

		return builder.toString();
	}

}
