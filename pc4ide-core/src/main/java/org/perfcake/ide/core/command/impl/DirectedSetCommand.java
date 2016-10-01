package org.perfcake.ide.core.command.impl;

import org.perfcake.ide.core.command.Command;
import org.perfcake.ide.core.exception.CommandExecutionException;
import org.perfcake.ide.core.exception.ModelDirectorException;
import org.perfcake.ide.core.model.director.ModelDirector;
import org.perfcake.ide.core.model.director.ModelField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DirectedSetCommand uses {@link org.perfcake.ide.core.model.director.ModelDirector} to set field to new value;
 */
public class DirectedSetCommand implements Command {
	static final Logger logger = LoggerFactory.getLogger(DirectedSetCommand.class);

	private ModelDirector director;
	private ModelField field;
	private Object oldValue;
	private Object newValue;

	public DirectedSetCommand(ModelDirector director, ModelField field, Object newValue) {
		this.director = director;
		this.field = field;
		this.newValue = newValue;
	}

	@Override
	public void execute() {
		try {
			oldValue = director.getModelFieldValue(field);
			director.setModelField(field, newValue);
		} catch (ModelDirectorException e) {
			logger.info("Cannot execute command: " + this, e);
		}
	}

	@Override
	public void undo() {
		try {
			director.setModelField(field, oldValue);
		} catch (ModelDirectorException e) {
			logger.info("Cannot execute command: " + this, e);
		}
	}

	@Override
	public boolean isUndoable() {
		return false;
	}

	@Override
	public String toString() {
		return "DirectedSetCommand{" +
				"director=" + director +
				", field=" + field +
				", oldValue=" + oldValue +
				", newValue=" + newValue +
				'}';
	}
}
