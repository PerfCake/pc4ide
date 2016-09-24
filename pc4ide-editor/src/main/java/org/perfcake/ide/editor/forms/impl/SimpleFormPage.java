package org.perfcake.ide.editor.forms.impl;

import org.perfcake.ide.editor.forms.FormGenerator;
import org.perfcake.ide.editor.forms.FormManager;
import org.perfcake.ide.editor.forms.FormPage;
import org.perfcake.ide.editor.forms.FormPageDirector;

import javax.swing.JPanel;

/**
 * Default implementation for {@link FormPage}.
 *
 * @author jknetl
 *
 */
public class SimpleFormPage implements FormPage {

	private Object model;
	private FormPageDirector pageDirector;
	private JPanel form;
	private FormGenerator formGenerator;
	private FormManager formManager;

	public SimpleFormPage(FormManager formManager, Object model) {
		super();
		if (formManager == null) {
			throw new IllegalArgumentException("formManager cannot be null");
		}
		if (model == null) {
			throw new IllegalArgumentException("model cannot be null");
		}
		this.formManager = formManager;
		this.model = model;

		form = new JPanel();
		formGenerator = new ReflectionFormGenerator(model, pageDirector, formManager.getComponentManager(), form);
		formGenerator.createForm();
	}

	@Override
	public boolean isValid() {
		return pageDirector.isValid();
	}

	@Override
	public FormManager getFormManager() {
		return formManager;
	}

	@Override
	public JPanel getContentPanel() {
		return form;
	}

	@Override
	public String getMessage() {
		return pageDirector.getMessage();
	}

	@Override
	public void updateForm() {
		// TODO Auto-generated method stub
	}

	@Override
	public void applyChanges() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getModel() {
		return model;
	}

}
