/**
 *
 */
package org.perfcake.ide.core.model;

import org.perfcake.model.PropertyType;
import org.perfcake.model.Scenario.Sequences.Sequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jknetl
 *
 */
public class SequenceModel extends AbstractModel implements PropertyContainer {

	public static final String PROPERTY_PROPERTIES = "sequence-property";
	public static final String PROPERTY_CLASS = "sequence-class";
	public static final String PROPERTY_ID = "sequence-id";

	private Sequence sequence;

	SequenceModel(Sequence sequence) {
		super();
		if (sequence == null) {
			throw new IllegalArgumentException("sequence may not be null");
		}
		this.sequence = sequence;

		for (final PropertyType p : sequence.getProperty()) {
			getMapper().bind(p, new PropertyModel(p));
		}
	}

	public SequenceModel() {
		super();
		this.sequence = new Sequence();
	}

	/**
	 * This method should not be used for modifying sequence (in a way getSequence().setClazz(clazz))
	 * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
	 *
	 * @return PerfCake model of Sequence
	 */
	Sequence getSequence() {
		return sequence;
	}

	public String getClazz() {
		return sequence.getClazz();
	}

	public void setClazz(String value) {
		final String oldClazz = sequence.getClazz();
		sequence.setClazz(value);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_CLASS, oldClazz, value);
	}

	public String getId() {
		return sequence.getId();
	}

	public void setId(String value) {
		final String oldId = sequence.getId();
		sequence.setId(value);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_ID, oldId, value);
	}

	@Override
	public List<PropertyModel> getProperty() {
		final List<PropertyModel> result = MapperUtils.getPc4ideList(sequence.getProperty(), getMapper());
		return Collections.unmodifiableList(result);
	}

	@Override
	public void addProperty(PropertyModel Property) {
		addProperty(sequence.getProperty().size(), Property);
	}

	@Override
	public void addProperty(int index, PropertyModel property) {
		sequence.getProperty().add(index, property.getProperty());
		getMapper().bind(property.getProperty(), property);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, null, property);
	}

	@Override
	public void removeProperty(PropertyModel property) {
		if (sequence.getProperty().remove(property.getProperty())) {
			getMapper().unbind(property.getProperty(), property);
			getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, property, null);
		}
	}

	@Override
	public List<AbstractModel> getModelChildren() {
		final List<AbstractModel> children = new ArrayList<>();
		children.addAll(getProperty());
		return children;
	}

}
