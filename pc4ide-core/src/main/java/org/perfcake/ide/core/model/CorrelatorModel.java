/**
 *
 */
package org.perfcake.ide.core.model;

import org.perfcake.model.PropertyType;
import org.perfcake.model.Scenario.Receiver.Correlator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jknetl
 *
 */
public class CorrelatorModel extends AbstractModel implements PropertyContainer {

	public static final String PROPERTY_PROPERTIES = "correlator-properties";
	public static final String PROPERTY_CLASS = "correlator-class";

	private Correlator correlator;

	CorrelatorModel(Correlator correlator) {
		super();
		if (correlator == null){
			throw new IllegalArgumentException("Correlator must not be null");
		}
		this.correlator = correlator;

		for (final PropertyType p : correlator.getProperty()) {
			getMapper().bind(p, new PropertyModel(p));
		}
	}

	public CorrelatorModel() {
		super();
		this.correlator = new Correlator();
	}

	/**
	 * This method should not be used for modifying correlator (in a way getCorrelator().setClazz(clazz)))
	 * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
	 *
	 * @return PerfCake model of Correlator
	 */
	Correlator getCorrelator() {
		return correlator;
	}

	@Override
	public void addProperty(PropertyModel Property) {
		addProperty(correlator.getProperty().size(), Property);
	}

	@Override
	public void addProperty(int index, PropertyModel property) {
		correlator.getProperty().add(index, property.getProperty());
		getMapper().bind(property.getProperty(), property);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, null, property);
	}

	@Override
	public void removeProperty(PropertyModel property) {
		if (correlator.getProperty().remove(property.getProperty())) {
			getMapper().unbind(property.getProperty(), property);
			getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, property, null);
		}
	}

	@Override
	public List<PropertyModel> getProperty() {
		final List<PropertyModel> result = MapperUtils.getPc4ideList(correlator.getProperty(), getMapper());
		return Collections.unmodifiableList(result);
	}

	public String getClazz() {
		return correlator.getClazz();
	}

	public void setClazz(String value) {
		final String oldClazz = correlator.getClazz();
		correlator.setClazz(value);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_CLASS, oldClazz, value);
	}

	@Override
	public List<AbstractModel> getModelChildren() {
		final List<AbstractModel> children = new ArrayList<>();
		children.addAll(getProperty());
		return children;
	}

}
