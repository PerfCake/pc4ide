/*
 *-----------------------------------------------------------------------------
 * pc4ide
 *
 * Copyright 2017 Jakub Knetl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *-----------------------------------------------------------------------------
 */
/**
 *
 */

package org.perfcake.ide.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.perfcake.model.PropertyType;
import org.perfcake.model.Scenario.Sequences.Sequence;

/**
 * Model of PerfCake's sequence.
 * @author jknetl
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

    /**
     * Creates new sequence model.
     */
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

    /**
     * Sets sequence implementation class.
     * @param value fully qualified name of the sequence implementation class
     */
    public void setClazz(String value) {
        final String oldClazz = sequence.getClazz();
        sequence.setClazz(value);
        getPropertyChangeSupport().firePropertyChange(PROPERTY_CLASS, oldClazz, value);
    }

    public String getId() {
        return sequence.getId();
    }

    /**
     * Sets seqeunce id.
     * @param value sequence id
     */
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
