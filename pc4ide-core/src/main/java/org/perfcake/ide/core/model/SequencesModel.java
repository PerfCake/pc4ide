/**
 *
 */
package org.perfcake.ide.core.model;

import org.perfcake.model.Scenario.Sequences;
import org.perfcake.model.Scenario.Sequences.Sequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jknetl
 *
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

	public void addSequence(SequenceModel sequence) {
		addSequence(sequences.getSequence().size(), sequence);
	}

	public void addSequence(int index, SequenceModel sequence) {
		sequences.getSequence().add(index, sequence.getSequence());
		getMapper().bind(sequence.getSequence(), sequence);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_SEQUENCE, null, sequence);
	}

	public void removeSequence(SequenceModel sequence) {
		if (sequences.getSequence().remove(sequence.getSequence())) {
			getMapper().unbind(sequence.getSequence(), sequence);
			getPropertyChangeSupport().firePropertyChange(PROPERTY_SEQUENCE, sequence, null);
		}
	}

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
