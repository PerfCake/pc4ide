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

import org.perfcake.model.Scenario.Sequences;
import org.perfcake.model.Scenario.Sequences.Sequence;

/**
 * Model of sequences.
 * @author jknetl
 */
public class SequencesModel extends AbstractModel {

    public static final String PROPERTY_SEQUENCE = "sequences-sequence";

    private Sequences sequences;

    SequencesModel(Sequences sequences) {
        super();
        if (sequences == null) {
            throw new IllegalArgumentException("Sequences must not be null.");
        }
        this.sequences = sequences;

        for (final Sequence s : sequences.getSequence()) {
            getMapper().bind(s, new SequenceModel(s));
        }
    }

    /**
     * Creates new model of sequences.
     */
    public SequencesModel() {
        super();
        this.sequences = new Sequences();
    }

    /**
     * This method should not be used for modifying sequences (in a way getSequences().add()))
     * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
     *
     * @return PerfCake model of sequences
     */
    Sequences getSequences() {
        return sequences;
    }

    /**
     * Adds a sequence.
     * @param sequence sequence to be added.
     */
    public void addSequence(SequenceModel sequence) {
        addSequence(sequences.getSequence().size(), sequence);
    }

    /**
     * Adds a sequence.
     * @param index index of a sequence
     * @param sequence sequence to be added.
     */
    public void addSequence(int index, SequenceModel sequence) {
        sequences.getSequence().add(index, sequence.getSequence());
        getMapper().bind(sequence.getSequence(), sequence);
        getPropertyChangeSupport().firePropertyChange(PROPERTY_SEQUENCE, null, sequence);
    }

    /**
     * Removes a sequence.
     *
     * @param sequence sequence to be removed.
     */
    public void removeSequence(SequenceModel sequence) {
        if (sequences.getSequence().remove(sequence.getSequence())) {
            getMapper().unbind(sequence.getSequence(), sequence);
            getPropertyChangeSupport().firePropertyChange(PROPERTY_SEQUENCE, sequence, null);
        }
    }

    /**
     * Gets an unmodifiable list of sequences.
     *
     * @return Unmodifiable list of sequences
     */
    public List<SequenceModel> getSequence() {
        final List<SequenceModel> result = MapperUtils.getPc4ideList(sequences.getSequence(), getMapper());
        return Collections.unmodifiableList(result);
    }

    @Override
    public List<AbstractModel> getModelChildren() {
        final List<AbstractModel> children = new ArrayList<>();
        children.addAll(getSequence());
        return children;
    }

}
