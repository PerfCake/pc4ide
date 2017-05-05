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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Collections;
import java.util.List;
import org.perfcake.ide.editor.colors.NamedColor;
import org.perfcake.ide.editor.swing.icons.ResizableIcon;
import org.perfcake.ide.editor.view.Pair;

/**
 * View which serves as "add" button for adding other components.
 *
 * @author Jakub Knetl
 */
public class ComponentAddView extends SimpleSectorView {

    /**
     * creates new sector view.
     *
     * @param icon icon of the inspector in the sector
     */
    public ComponentAddView(ResizableIcon icon) {
        super(icon);
        header = "Add component";
    }

    @Override
    protected List<Pair> getAdditionalData() {
        return Collections.emptyList();
    }

    @Override
    protected Color getIconColor() {
        return colorScheme.getColor(NamedColor.ACCENT_4);
    }

    @Override
    protected void initManagementIcons() {
        // null
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
    }

    @Override
    protected Stroke getBoundsStroke(Graphics2D g2d) {
        final float[] dash1 = {10.0f};
        final BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
        return dashed;
    }

    @Override
    protected int getIconSide() {
        return 20;
    }

    @Override
    protected int getSmallIconSide() {
        return 10;
    }
}
