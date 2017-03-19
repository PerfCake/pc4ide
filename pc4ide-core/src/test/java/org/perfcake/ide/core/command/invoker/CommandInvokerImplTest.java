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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.perfcake.ide.core.command.Command;
import org.perfcake.ide.core.exception.CommandException;

/**
 * Tests for {@link CommandInvokerImpl}.
 *
 * @author Jakub Knetl
 */
public class CommandInvokerImplTest {

    private CommandInvoker history;
    private Command c1;
    private Command c2;
    private Command c3;
    private Command notUndoable;

    @Before
    public void setUp() {
        history = new CommandInvokerImpl();
        c1 = mock(Command.class);
        c2 = mock(Command.class);
        c3 = mock(Command.class);
        notUndoable = mock(Command.class);
        when(c1.isUndoable()).thenReturn(true);
        when(c2.isUndoable()).thenReturn(true);
        when(c3.isUndoable()).thenReturn(true);
        when(c3.isUndoable()).thenReturn(false);
    }

    @Test
    public void executeTest() throws CommandException {
        history.executeCommand(c1);
        verify(c1).execute();
        verify(c2, never()).execute();

        history.executeCommand(c2);
        verify(c2).execute();
    }

    @Test
    public void undoTest() throws CommandException {
        history.executeCommand(c1);
        history.executeCommand(c2);
        verify(c1).execute();
        verify(c2).execute();
        verify(c1, never()).undo();
        verify(c2, never()).undo();

        history.undo();
        verify(c1, never()).undo();
        verify(c2).undo();
    }

    @Test
    public void redoTest() throws CommandException {
        history.executeCommand(c1);
        history.executeCommand(c2);
        verify(c1).execute();
        verify(c2).execute();
        verify(c1, never()).undo();
        verify(c2, never()).undo();

        history.undo();
        history.redo();
        verify(c1, never()).undo();
        verify(c1, times(1)).execute();
        verify(c2).undo();
        verify(c2, times(2)).execute();
    }

    /**
     * Tests for canUndo and canRedo methods.
     */
    @Test
    public void statusMethodTest() {
        assertThat(history.canRedo(), is(false));
        assertThat(history.canUndo(), is(false));

        history.executeCommand(notUndoable);
        assertThat(history.canUndo(), is(false));
        history.executeCommand(c1);
        assertThat(history.canUndo(), is(true));
        history.undo();
        assertThat(history.canRedo(), is(true));
        assertThat(history.canUndo(), is(false));
        history.redo();
        assertThat(history.canRedo(), is(false));
        assertThat(history.canUndo(), is(true));

    }
}
