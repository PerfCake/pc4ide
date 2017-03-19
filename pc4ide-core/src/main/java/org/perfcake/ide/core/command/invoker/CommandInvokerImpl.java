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

package org.perfcake.ide.core.command.invoker;

import java.util.ArrayList;
import java.util.List;
import org.perfcake.ide.core.command.Command;
import org.perfcake.ide.core.exception.CommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link CommandInvoker}.
 *
 * @author Jakub Knetl
 */
public class CommandInvokerImpl implements CommandInvoker {

    static final Logger logger = LoggerFactory.getLogger(CommandInvokerImpl.class);

    // invoker list
    private List<Command> historyList = new ArrayList<>();

    // position in command history list. Points to last command which has been executed in the history list.
    private int position = -1;

    @Override
    public void executeCommand(Command command) {
        if (command == null) {
            throw new IllegalArgumentException("Command is null.");
        }

        try {
            command.execute();

            // drop invoker which has been undone
            if (position < historyList.size() - 1) {
                historyList = historyList.subList(0, position + 1);
            }

            if (command.isUndoable()) {
                historyList.add(command);
                position++;
            }
        } catch (CommandException e) {
            logger.warn("Cannot execute command.", e);
        }

    }

    @Override
    public boolean canUndo() {
        return position >= 0;
    }

    @Override
    public boolean canRedo() {
        return position < (historyList.size() - 1);
    }

    @Override
    public void undo() {
        if (position < 0 || position > (historyList.size() - 1)) {
            logger.warn("Position out of range. Position: {}, Range: [0,{}]", position, historyList.size() - 1);
        }
        Command c = historyList.get(position);
        try {
            c.undo();
            position--;
        } catch (CommandException e) {
            logger.warn("Cannot undo command.", e);
        }

    }

    @Override
    public void redo() {
        int indexOfRedone = position + 1;
        if (indexOfRedone < 0 || indexOfRedone > (historyList.size() - 1)) {
            logger.warn("Position out of range. Position: {}, Range: [0,{}]", indexOfRedone, historyList.size() - 1);
        }
        Command c = historyList.get(indexOfRedone);
        try {
            c.execute();
            position++;
        } catch (CommandException e) {
            logger.warn("Cannot redo command", e);
        }
    }
}
