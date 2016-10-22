package org.perfcake.ide.core.components;

import org.perfcake.ide.core.components.doclet.JavadocComponentParser;
import org.perfcake.util.properties.MandatoryProperty;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Manages PerfCake components and its binding to model classes.
 *
 * @author jknetl
 */
public class ComponentManager {

	static final Logger logger = LoggerFactory.getLogger(ComponentManager.class);

	/**
	 * Location of the javadoc property file on the claspath.
	 */
	public static final String JAVADOC_LOCATION_CLASSPATH = "/" + JavadocComponentParser.JAVADOC_PROPERTIES_FILE;

	public static final String[] PACKAGES_WITH_COMPONENTS = new String[] {"org.perfcake"};

	/**
	 * Mapping between componentKind and particular component implementations.
	 */
	private Map<ComponentKind, List<Component>> componentImplementationsMap;

	/**
	 * Mapping between componentKind and particular component which is either abstract
	 * (user defines implementation by setting classname) or fixed in
	 * the PerfCake so that user can't provide another implementation in the scenario.
	 */
	private Map<ComponentKind, Component> componentsMap;

	/**
	 * Property file with component javadoc.
	 */
	private Properties javadocCatalogue;

	/**
	 * List of packages prefixes where to search for the components
	 */
	private List<String> packagesToScan;

	public ComponentManager(InputStream propertiesInput, List<String> packagesToScan) {
		this.packagesToScan = packagesToScan;
		javadocCatalogue = new Properties();

		if (propertiesInput == null) {
			logger.warn("Cannot parse file with perfcake docs from classpath: {}."
					+ " No documentation will be displayed!", JAVADOC_LOCATION_CLASSPATH);
		}
		try {
			javadocCatalogue.load(propertiesInput);
		} catch (final IOException e) {
			logger.warn("Error when reading perfcake comonents documentation. No help for components will be displayed.", e);
		}

		componentsMap = new HashMap<>();
		componentImplementationsMap = new HashMap<>();

		discoverComponents();
	}

	/**
	 * Scans selected packages for the perfcake components.
	 */
	public void discoverComponents() {
		for (final ComponentKind kind : ComponentKind.values()) {
			// parse component

			Component component;
			if (kind.isAbstract()) {
				component = parseInterface(kind, kind.getComponentClazz());

				// if component is abstract then we want to search for its implementations
				final List<Component> componentsList = new ArrayList<>();
				final Reflections reflections = new Reflections(packagesToScan);
				for (final Class<?> clazz : reflections.getSubTypesOf(kind.getComponentClazz())) {
					final Component c = parseComponent(kind, clazz);
					componentsList.add(c);
				}

				componentImplementationsMap.put(kind, componentsList);
			} else {
				component = parseComponent(kind, kind.getComponentClazz());
			}

			componentsMap.put(kind, component);
		}
	}

	private Component parseInterface(ComponentKind kind, Class<?> clazz) {
		final String clazzDoc = javadocCatalogue.getProperty(clazz.getName());

		//scan the fields
		final List<PropertyField> fields = new ArrayList<>();
		for (final Method m : clazz.getDeclaredMethods()) {

			if (m.getName().startsWith("set") && m.getName().length() > 3) {
				final StringBuilder nameBuilder = new StringBuilder();
				nameBuilder.append(Character.toLowerCase(m.getName().charAt(3)));
				if (m.getName().length() > 4) {
					nameBuilder.append(m.getName().substring(4));
				}
				final String fieldName = nameBuilder.toString();
				final String fieldDocKey = clazz.getName() + "." + fieldName;
				final String fieldDoc = javadocCatalogue.getProperty(fieldDocKey);
				// we can't get information whether the field is mandatory here
				final PropertyField field = new PropertyField(fieldName, fieldDoc, false);
				fields.add(field);
			}
		}
		return new Component(kind, clazz, fields, clazzDoc);
	}

	private Component parseComponent(final ComponentKind kind, final Class<?> clazz) {
		final String clazzDoc = javadocCatalogue.getProperty(clazz.getName());

		//scan the fields
		final List<PropertyField> fields = new ArrayList<>();
		for (final java.lang.reflect.Field f : clazz.getDeclaredFields()) {

			final String fieldDocKey = clazz.getName() + "." + f.getName();
			final String fieldDoc = javadocCatalogue.getProperty(fieldDocKey);
			final boolean isFieldMandatory = isMandatory(f);
			final PropertyField field = new PropertyField(f.getName(), fieldDoc, isFieldMandatory);
			fields.add(field);
		}
		return new Component(kind, clazz, fields, clazzDoc);
	}

	private boolean isMandatory(java.lang.reflect.Field f) {
		boolean isMandatory = false;
		Annotation mandatoryAnnotation = f.getAnnotation(MandatoryProperty.class);
		if (mandatoryAnnotation != null) {
			isMandatory = true;
		}
		return isMandatory;
	}

	/**
	 * @return unmodifiable Mapping between componentKind and component implmentations
	 */
	public Map<ComponentKind, List<Component>> getComponentImplementationsMap() {
		return Collections.unmodifiableMap(componentImplementationsMap);
	}

	/**
	 * @return unmodifiable Mapping between componentKind and component interface in perfcake (note that interface for
	 * component may be also particular class, not only the interface)
	 */
	public Map<ComponentKind, Component> getComponentsMap() {
		return Collections.unmodifiableMap(componentsMap);
	}

	/**
	 * @param kind
	 * @return PerfCake component based on the type or null if no component is found
	 */
	public Component getComponent(ComponentKind kind) {
		return componentsMap.get(kind);
	}

	/**
	 * @param kind
	 * @return PerfCake component implementations list based on the type or null if no implementation is found.
	 */
	public List<Component> getComponentImplementations(ComponentKind kind) {
		return componentImplementationsMap.get(kind);
	}

	/**
	 * @param component
	 * @param fieldName simple name of the field
	 * @return Field of the component with given name or null if field or component cannot be found.
	 */
	public PropertyField getPropertyField(Component component, String fieldName) {
		PropertyField field = null;
		if (component != null) {
			field = component.getPropertyField(fieldName);
		}

		return field;
	}

	/**
	 * Locates documentation of the field in given component.
	 *
	 * @param component
	 * @param fieldName name of the field (just a simple name, not fully qualified)
	 * @return field documentation or null if cannot be found.
	 */
	public String getFieldDocumentation(Component component, String fieldName) {
		String docs = null;
		final PropertyField field = getPropertyField(component, fieldName);

		if (field != null) {
			docs = field.getDocs();
		}

		return docs;
	}
}
