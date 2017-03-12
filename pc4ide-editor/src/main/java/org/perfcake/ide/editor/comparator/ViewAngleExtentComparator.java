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

package org.perfcake.ide.editor.comparator;

import java.awt.Graphics2D;
import java.util.Comparator;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.view.View;

/**
 * Compares view by its requested minimum size angular extent.
 *
 * @author Jakub Knetl
 */
public class ViewAngleExtentComparator implements Comparator<View> {

    private LayoutData layoutData;
    private Graphics2D g2d;

    /**
     * Creates new angleExtentComparator.
     *
     * @param layoutData layout data
     * @param g2d        graphics context
     */
    public ViewAngleExtentComparator(LayoutData layoutData, Graphics2D g2d) {
        this.layoutData = layoutData;
        this.g2d = g2d;
    }

    @Override
    public int compare(View o1, View o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }

        if (o1 == null) {
            return 1;
        }

        if (o2 == null) {
            return -1;
        }

        LayoutData minSize1 = o1.getMinimumSize(layoutData, g2d);
        LayoutData minSize2 = o2.getMinimumSize(layoutData, g2d);

        if (minSize1 == null && minSize2 == null) {
            return 0;
        }

        if (minSize1 == null) {
            return -1;
        }
        if (minSize2 == null) {
            return 1;
        }

        double minAngle1 = minSize1.getAngularData().getAngleExtent();
        double minAngle2 = minSize2.getAngularData().getAngleExtent();

        int result = 0;
        if (minAngle1 < minAngle2) {
            result = -1;
        }
        if (minAngle1 > minAngle2) {
            result = 1;
        }

        return result;

    }
}
