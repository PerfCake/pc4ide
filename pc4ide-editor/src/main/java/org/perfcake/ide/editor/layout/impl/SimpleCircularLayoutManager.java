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

package org.perfcake.ide.editor.layout.impl;

import java.awt.Graphics2D;

import org.perfcake.ide.editor.layout.AbstractLayoutManager;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.view.View;

/**
 * Simple circular manager lays out the children into circular sectors. It computes angular
 * extent of each child equally so that each child has same extent.
 *
 * @author jknetl
 */
public class SimpleCircularLayoutManager extends AbstractLayoutManager {

    public SimpleCircularLayoutManager() {
        super();
    }

    @Override
    public void layout(Graphics2D g2d) {

        final int numOfChildren = computeChildren();

        final double angularExtendForChild = constraints.getAngularData().getAngleExtent() / numOfChildren;
        double startAngle = constraints.getAngularData().getStartAngle();

        for (View v : getChildren()) {
            final LayoutData data = new LayoutData(constraints);
            data.getAngularData().setAngleExtent(angularExtendForChild);
            data.getAngularData().setStartAngle(startAngle);
            //sets layout data to child layoutManager
            //TODO(jknetl): there should be called setLayoutData only once!!!
            v.setLayoutData(data);
            //sets layout data to child view
            v.setLayoutData(data);
            startAngle += angularExtendForChild;
        }
    }

    @Override
    public LayoutData getMinimumSize(LayoutData constraint, Graphics2D g2d) {
        return constraint;
    }

    private int computeChildren() {
        return (getChildren() == null) ? 0 : getChildren().size();
    }
}
