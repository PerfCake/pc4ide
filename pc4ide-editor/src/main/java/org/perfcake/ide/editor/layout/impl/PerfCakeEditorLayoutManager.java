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

package org.perfcake.ide.editor.layout.impl;

import java.awt.Graphics2D;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.perfcake.ide.editor.comparator.ViewComparator;
import org.perfcake.ide.editor.layout.AbstractLayoutManager;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.view.View;

/**
 * PerfCakeEditorLayoutManager manages the layout of graphical part of PerfCake editor.
 */
public class PerfCakeEditorLayoutManager extends AbstractLayoutManager {

    public static final int DEFAULT_START_ANGLE = 120;

    private Comparator<View> viewComparator = new ViewComparator();

    @Override
    public void layout(Graphics2D g2d) {

        Map<View, Double> extentMap = new HashMap<>();

        double requestedExtentByChildren = 0.0;

        for (View child : children) {
            double extent = child.getMinimumSize(constraints, g2d).getAngularData().getAngleExtent();
            extentMap.put(child, extent);
            requestedExtentByChildren += extent;
        }

        double startAngle = DEFAULT_START_ANGLE;

        if (requestedExtentByChildren < constraints.getAngularData().getAngleExtent()) {
            for (View v : children) {
                final LayoutData data = new LayoutData(constraints);
                data.getAngularData().setAngleExtent(extentMap.get(v));
                data.getAngularData().setStartAngle(startAngle);
                v.setLayoutData(data);

                // move startAgle by angular extent of view v
                startAngle += extentMap.get(v);
            }
        } else {
            // TODO: inspector requested larger angular extent than is available
            // we need to somehow implement shirnking of some views!
        }
    }

    @Override
    public void add(View component) {
        super.add(component);
        Collections.sort(children, viewComparator);
    }
}
