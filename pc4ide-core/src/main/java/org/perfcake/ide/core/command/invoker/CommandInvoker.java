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

import org.perfcake.ide.core.command.Command;

/**
 * Command invoker manages commands and its history. It care about executing commands, undoing commands, and re-executing commands.
 *
 * @author Jakub Knetl
 */
public interface CommandInvoker {

    /**
     * Executes command and puts it into history.
     *
     * @param command command to be executed.
     */
    void executeCommand(Command command);

    /**
     * @return true if we can move back in history. ie. there were some commands executed previously and they can be undone by invoker.
     */
    boolean canUndo();

    /**
     * @return true if it is possible to move forward in hisotry. ie. there is command which has been undone and can be re-executed.
     */
    boolean canRedo();

    /**
     * Undo last command.
     */
    void undo();

    /**
     * Redo last command.
     */
    void redo();

}
