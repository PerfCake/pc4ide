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

package org.perfcake.ide.editor.view.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.perfcake.ide.editor.colors.NamedColor;
import org.perfcake.ide.editor.swing.icons.components.SequenceIcon;
import org.perfcake.ide.editor.swing.icons.control.MinusIcon;
import org.perfcake.ide.editor.swing.icons.control.PlusIcon;
import org.perfcake.ide.editor.view.Pair;

/**
 * Represents view of an sequence.
 *
 * @author Jakub Knetl
 */
public class SequenceView extends CondensedSectorView {

    private String id;

    /**
     * creates new sector view.
     */
    public SequenceView() {
        super(new SequenceIcon(ICON_SIDE, ICON_SIDE));
    }

    @Override
    protected List<Pair> getAdditionalData() {
        List<Pair> data = new ArrayList<>();
        data.add(new Pair("id", id));
        return data;
    }

    @Override
    protected Color getIconColor() {
        return colorScheme.getColor(NamedColor.COMPONENT_SEQUENCE);
    }

    @Override
    protected void initManagementIcons() {
        managementIcons.add(new MinusIcon(colorScheme.getColor(NamedColor.ACCENT_1)));
        managementIcons.add(new PlusIcon(colorScheme.getColor(NamedColor.ACCENT_4)));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
