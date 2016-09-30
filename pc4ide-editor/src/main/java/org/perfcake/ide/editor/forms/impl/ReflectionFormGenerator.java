package org.perfcake.ide.editor.forms.impl;

import org.perfcake.ide.core.components.Component;
import org.perfcake.ide.core.components.ComponentKind;
import org.perfcake.ide.core.components.ComponentManager;
import org.perfcake.ide.core.model.director.FieldType;
import org.perfcake.ide.core.model.director.ModelDirector;
import org.perfcake.ide.core.model.director.ModelField;
import org.perfcake.ide.editor.forms.FormBuilder;
import org.perfcake.ide.editor.forms.FormElement;
import org.perfcake.ide.editor.forms.FormGenerator;
import org.perfcake.ide.editor.forms.FormPageDirector;
import org.perfcake.ide.editor.forms.impl.elements.ChoiceElement;
import org.perfcake.ide.editor.forms.impl.elements.ReflectiveTabularElement;
import org.perfcake.ide.editor.forms.impl.elements.TextElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JPanel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
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

	private ModelDirector model;
	private FormPageDirector director;
	private ComponentManager componentManager;
	private JPanel form;

	private FormBuilder formBuilder;

	public ReflectionFormGenerator(ModelDirector model, FormPageDirector director, ComponentManager componentManager, JPanel form) {
		super();
		this.model = model;
		this.director = director;
		this.componentManager = componentManager;
		this.form = form;
		formBuilder = new FormBuilderImpl(form, DEFAULT_MAX_COMPONETNS_IN_ROW, MAIN_COLUMN);
	}

	@Override
	public void createForm() {

		final List<FormElement> elements = createFormElements();

		for (final FormElement element : elements) {
			formBuilder.addElement(element);
		}

	}

	private List<FormElement> createFormElements() {

		final List<FormElement> elements = new ArrayList<>();

		final Class<?> modelClazz = model.getClass();

		for (ModelField f : model.getModelFields()){

			if (f.getFieldType() == FieldType.SIMPLE){
				FormElement element;
				if ("clazz".equals(f.getName())){
					element = new ChoiceElement(f.getName(), f.getDocs(), String.valueOf(model.getModelFieldValue(f)), getImplementationNames(model.getModel().getClass()));
				} else {
					element = new TextElement(f.getName(), f.getDocs(), String.valueOf(model.getModelFieldValue(f)));
				}

				elements.add(element);
			}

		}

		return elements;
	}


	private List<String> getImplementationNames(Class<?> modelClazz) {
		final List<String> list = new ArrayList<>();

		final ComponentKind kind = ComponentKind.getComponentKindByModelClazz(modelClazz);

		final List<Component> implementations = componentManager.getComponentImplementations(kind);

		for (final Component c : implementations) {
			list.add(c.getImplementation().getSimpleName());
		}

		return list;
	}
}
