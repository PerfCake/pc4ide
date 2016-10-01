package org.perfcake.ide.core.command;

import org.perfcake.ide.core.exception.CommandExecutionException;

/**
 * Represent command which may be executed and undone.
 */
public interface Command {

	/**
	 * Executes command.
	 */
	void execute();

	/**
	 * Undo command
	 */
	void undo();

	/**
	 *
	 * @return true if the command can be undone. False otherwise
	 */
	boolean isUndoable();
}
