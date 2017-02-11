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

package org.perfcake.pc4ide.netbeans;

import javax.swing.Action;

import org.netbeans.spi.palette.PaletteActions;
import org.openide.util.Lookup;

/**
 * Represents a no-operation palette actions.
 */
public class NoopPaletteActions extends PaletteActions {

    @Override
    public Action[] getImportActions() {
        return new Action[0];
    }

    @Override
    public Action[] getCustomPaletteActions() {
        return new Action[0];
    }

    @Override
    public Action[] getCustomCategoryActions(Lookup category) {
        return new Action[0];
    }

    @Override
    public Action[] getCustomItemActions(Lookup item) {
        return new Action[0];
    }

    @Override
    public Action getPreferredAction(Lookup item) {
        return null;
    }
}
