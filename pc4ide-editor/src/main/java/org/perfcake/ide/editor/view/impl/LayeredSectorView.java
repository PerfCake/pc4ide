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

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.impl.CircularSectorLayoutManager;
import org.perfcake.ide.editor.swing.icons.ResizableIcon;
import org.perfcake.ide.editor.view.View;

/**
 * Layered sector view splits sectors into two layers. Inner layer is used for actuall view. The outer layer is
 * used for children views.
 *
 * @author Jakub Knetl
 */
public abstract class LayeredSectorView extends SimpleSectorView {

    /**
     * Constructor for Layered sector view.
     *
     * @param icon icon of the view.
     */
    public LayeredSectorView(ResizableIcon icon) {
        super(icon);
        layoutManager = new CircularSectorLayoutManager(true);
        layoutManager.setConstraint(new LayoutData());
    }

    @Override
    public LayoutData getMinimumSize(LayoutData constraint, Graphics2D g2d) {

        LayoutData constraintForReporter = computeLowerLayerData(constraint);
        LayoutData constraintForChildren = computeUpperLayerData(constraint);

        LayoutData reporterViewMinimumSize = super.getMinimumSize(constraintForReporter, g2d);
        LayoutData destinationsMinimumSize = layoutManager.getMinimumSize(constraintForChildren, g2d);

        LayoutData requiredTotal = new LayoutData(constraint);

        requiredTotal.getAngularData().setAngleExtent(Math.max(reporterViewMinimumSize.getAngularData().getAngleExtent(),
                destinationsMinimumSize.getAngularData().getAngleExtent()));

        return requiredTotal;
    }

    /**
     * Computes layout data for lower layer.
     * @param data data
     * @return copy of layout data shrunk only to lower layer
     */
    protected LayoutData computeLowerLayerData(LayoutData data) {
        LayoutData lowerLayer = new LayoutData(data);
        lowerLayer.getRadiusData().setOuterRadius(computeLayerBorder(data));
        return lowerLayer;
    }

    /**
     * Computes layout data for upper layer.
     * @param data data
     * @return copy of layout data shrunk only to upper layer
     */
    protected LayoutData computeUpperLayerData(LayoutData data) {
        LayoutData upperLayer = new LayoutData(data);
        upperLayer.getRadiusData().setInnerRadius(computeLayerBorder(data));
        return upperLayer;
    }

    /**
     * Compute border radius between layers.
     * @param data layout data
     *
     * @return radius of border between layers.
     */
    protected double computeLayerBorder(LayoutData data) {
        double borderRadius = (data.getRadiusData().getInnerRadius() + data.getRadiusData().getOuterRadius()) * 0.55;
        return borderRadius;
    }

    @Override
    public Shape getViewBounds() {
        Area area = new Area(super.getViewBounds());
        for (View child : getChildren()) {
            Area childArea = new Area(child.getViewBounds());
            area.add(childArea);
        }

        return area;
    }

    @Override
    public void draw(Graphics2D g2d) {
        for (View child : getChildren()) {
            child.draw(g2d);
        }
        super.draw(g2d);
    }

    @Override
    public void setLayoutData(LayoutData layoutData) {
        super.setLayoutData(computeLowerLayerData(layoutData));

        // all data provided serves as constraint for child views
        layoutManager.setConstraint(computeUpperLayerData(layoutData));
    }
}
