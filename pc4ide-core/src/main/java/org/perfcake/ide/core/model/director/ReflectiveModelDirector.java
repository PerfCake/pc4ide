package org.perfcake.ide.core.model.director;

import static org.eclipse.jdt.internal.compiler.parser.Parser.name;

import org.perfcake.ide.core.Field;
import org.perfcake.ide.core.components.Component;
import org.perfcake.ide.core.components.ComponentKind;
import org.perfcake.ide.core.components.ComponentManager;
import org.perfcake.ide.core.components.PropertyField;
import org.perfcake.ide.core.exception.ModelDirectorException;
import org.perfcake.ide.core.model.PropertiesModel;
import org.perfcake.ide.core.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reflective model directors controls the model using the reflection. It scans its model class and calls methods by the reflection.
 * It uses component manager to get documentation and also to work with custom properties which are dependent on the implementation.
 *
 * @author jknetl
 */
public class ReflectiveModelDirector implements ModelDirector {

	static final Logger logger = LoggerFactory.getLogger(ReflectiveModelDirector.class);
	public static final String CLAZZ_FIELD = "clazz";
	public static final String ADD_PROPERTY_METHOD = "addProperty";
	public static final String GET_PROPERTY_METHOD = "getProperty";

	private Object model;
	private ComponentManager componentManager;

	public ReflectiveModelDirector(Object model, ComponentManager componentManager) {
		if (model == null) {
			throw new IllegalArgumentException("Model can't be null");
		}
		if (componentManager == null) {
			throw new IllegalArgumentException("Component manager can't be null.");
		}
		this.model = model;
		this.componentManager = componentManager;
	}

	@Override
	public Object getModel() {
		return model;
	}

	@Override
	public String getDocs() {
		String docs = null;
		ComponentKind kind = ComponentKind.getComponentKindByModelClazz(model.getClass());
		Component component = componentManager.getComponent(kind);
		if (component != null) {
			docs = component.getDocumentation();
		}

		return docs;
	}

	@Override
	public List<Field> getAllFields() {
		List<Field> result = new ArrayList<Field>(getModelFields());
		result.addAll(getCustomPropertyFields());

		return result;
	}

	@Override
	public List<ModelField> getModelFields() {
		List<ModelField> fields = new ArrayList<>();

		final Class<?> modelClazz = model.getClass();

		for (final Method method : modelClazz.getMethods()) {

			if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
				final Class<?> argumentType = method.getParameterTypes()[0];
				if (argumentType.isPrimitive() || String.class.equals(argumentType)) {
					final String fieldName = getFieldName(method);
					final String docs = getFieldDocumentation(fieldName);

					Class<?> parameter = method.getParameterTypes()[0];
					FieldType type;
					if (!parameter.isPrimitive() && parameter.getCanonicalName().startsWith("org.perfcake")) {
						type = FieldType.PERFCAKE;
					} else {
						type = FieldType.SIMPLE;
					}

					ModelField field = new ModelField(type, fieldName, docs);
					fields.add(field);
				}
			}

			if (method.getName().startsWith("add") && method.getParameterCount() == 1
					&& !method.getName().equals("addPropertyChangeListener")
					&& !method.getName().equals("addProperty")) {
				final String fieldName = getFieldName(method);
				final String docs = getFieldDocumentation(fieldName);

				ModelField field = new ModelField(FieldType.COLLECTION, fieldName, docs);
			}
		}

		return fields;
	}

	@Override
	public Field getFieldByName(String name) {
		if (name == null || name.isEmpty()) {
			return null;
		}

		List<ModelField> fields = getModelFields();
		Field result = null;

		for (ModelField field : fields) {
			if (name.equals(field.getName())) {
				result = field;
				break;
			}
		}

		if (result == null) {
			for (PropertyField field : getCustomPropertyFields()) {
				if (name.equals(field.getName())) {
					result = field;
					break;
				}
			}
		}

		return result;
	}

	@Override
	public Object getFieldValue(Field field) throws ModelDirectorException {
		Object value = null;
		if (field.getFieldType() == FieldType.PROPERTY) {
			value = getCustomPropertyValue(field.getName());
		} else {
			String getMethodName = "get" + firstToUpperCase(field.getName());
			String isMethodName = "is" + firstToUpperCase(field.getName());
			Method getMethod = findMethod(model.getClass(), getMethodName, 0);
			Method isMethod = findMethod(model.getClass(), isMethodName, 0);
			Method method = (getMethod != null) ? getMethod : isMethod;

			if (method == null) {
				logger.warn("Cannot find get method for the field: {}", getMethodName + " or " + isMethodName);
				throw new ModelDirectorException("Cannot find getter for field: " + field);
			}

			try {
				value = method.invoke(model);
			} catch (IllegalAccessException | InvocationTargetException e) {
				logger.warn("Cannot invoke method: {}, object: {} ", method.getName(), model);
			}
		}
			return value;
		}

		@Override
		public void setField(Field field, Object value) throws ModelDirectorException {
			if (field.getFieldType() == FieldType.PROPERTY){
				setCustomProperty((PropertyField) field, String.valueOf(value));
			} else {
				String setMethodName = "set" + firstToUpperCase(field.getName());
				Method method = findMethod(model.getClass(), setMethodName, 1);

				if (method == null) {
					logger.warn("Cannot find set method for the field: {}", setMethodName);
					throw new ModelDirectorException("Cannot find setter for field: " + field);
				}

				try {
					method.invoke(model, value);
				} catch (IllegalAccessException | InvocationTargetException e) {
					logger.warn("Cannot invoke method: {}, object: {}, value: {}", method.getName(), model, value);
				}
			}
		}

		@Override
		public List<PropertyField> getCustomPropertyFields(){
			ComponentKind kind = ComponentKind.getComponentKindByModelClazz(model.getClass());

			Field clazzField = getFieldByName(CLAZZ_FIELD);
			String currentClazz = String.valueOf(getFieldValue(clazzField));

			if (clazzField == null) {
				return Collections.emptyList();
			}

			if (!kind.isAbstract()) {
				return Collections.emptyList();
			}

			List<Component> componentImplementations = componentManager.getComponentImplementations(kind);
			Component currentComponent = null;

			for (Component c : componentImplementations) {
				if (c.getImplementation().getSimpleName().equals(currentClazz)) {
					currentComponent = c;
					break;
				}
			}

			if (currentComponent == null) {
				return Collections.emptyList();
			}

			return currentComponent.getPropertyFields();
		}

	private String getCustomPropertyValue(String name) {
		List<PropertyField> properties = getCustomPropertyFields();
		PropertyField property = null;

		for (PropertyField p : properties) {
			if (p.getName().equals(name)) {
				property = p;
				break;
			}
		}

		if (property == null) {
			return null;
		}

		Method getPropertyModelMethod = findMethod(model.getClass(), "getProperty", 0);
		if (getPropertyModelMethod == null) {
			return null;
		}

		String value = null;
		try {
			List<PropertyModel> propertiesInModel = (List<PropertyModel>) getPropertyModelMethod.invoke(model);
			for (PropertyModel p : propertiesInModel) {
				if (p.getName().equals(name)) {
					value = p.getValue();
				}
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.warn("Cannot invoke method: {} on object: {}", getPropertyModelMethod, model);
		}

		return value;
	}

	private void setCustomProperty(PropertyField property, String value) throws ModelDirectorException {
		List<PropertyField> properties = getCustomPropertyFields();

		Method getPropertyModelMethod = findMethod(model.getClass(), GET_PROPERTY_METHOD, 0);

		if (getPropertyModelMethod == null) {
			return;
		}

		// check if the property is already in the list
		PropertyModel pModel = null;
		try {
			List<PropertyModel> propertiesInModel = (List<PropertyModel>) getPropertyModelMethod.invoke(model);
			for (PropertyModel p : propertiesInModel) {
				if (p.getName().equals(property.getName())) {
					pModel = p;
				}
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.warn("Cannot invoke method: {} on object: {}", getPropertyModelMethod, model);
		}

		if (pModel == null) {
			// property wasn't set before => add new
			Method m = findMethod(model.getClass(), ADD_PROPERTY_METHOD, 1);
			if (m == null) {
				return;
			}
			pModel = new PropertyModel();
			pModel.setName(property.getName());
			pModel.setValue(value);
			try {
				m.invoke(model, pModel);
			} catch (IllegalAccessException | InvocationTargetException e) {
				logger.warn("Cannot invoke method: {}, object: {}, value: {}", m, model, pModel);
			}
		} else {
			pModel.setValue(value);
		}
	}

	private String getFieldDocumentation(final String fieldName) {
		String docs = null;
		final Class<?> modelClazz = model.getClass();
		final ComponentKind kind = ComponentKind.getComponentKindByModelClazz(modelClazz);
		final Component component = componentManager.getComponent(kind);
		if (component != null) {
			docs = componentManager.getFieldDocumentation(component, fieldName);
		}

		return docs;
	}

	/**
	 * @param method
	 * @return FieldName derived from the get method
	 */
	private String getFieldName(final Method method) {
		final String name = firstToLowerCase(method.getName().substring(3));
		return name;
	}

	/**
	 * Finds method in the clazz with defined name and parameter count.
	 *
	 * @param clazz
	 * @param name
	 * @param parameterCount
	 * @return Method or null if cannot be found
	 */
	private Method findMethod(Class<?> clazz, String name, int parameterCount) {
		Method result = null;
		for (Method m : clazz.getMethods()) {
			if (m.getName().equals(name) && m.getParameterCount() == parameterCount) {
				result = m;
				break;
			}
		}

		return result;
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
