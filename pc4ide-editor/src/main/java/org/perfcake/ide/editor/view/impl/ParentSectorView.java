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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parent sector view splits sectors into two layers. Inner layer is used for actual view. The outer layer is
 * used for children views.
 *
 * @author Jakub Knetl
 */
public abstract class ParentSectorView extends SimpleSectorView {

    static final Logger logger = LoggerFactory.getLogger(ParentSectorView.class);

    /**
     * Constructor for Layered sector view.
     *
     * @param icon icon of the view.
     */
    public ParentSectorView(ResizableIcon icon) {
        super(icon);
        layoutManager = new CircularSectorLayoutManager(true, false);
        layoutManager.setConstraint(new LayoutData());
    }

    @Override
    public double getMinimumAngularExtent(LayoutData constraint, Graphics2D g2d) {
        LayoutData constraintForReporter = computeLowerLayerData(constraint);
        LayoutData constraintForChildren = computeUpperLayerData(constraint);

        double reporterExtent = super.getMinimumAngularExtent(constraintForReporter, g2d);
        double destinationExtent = layoutManager.getMinimumAngularExtent(constraintForChildren, g2d);

        logger.trace("Computing minimum extent from layout data: {}. Reporter extent: {}, Destination extent: {}",
                constraint, reporterExtent, destinationExtent);
        double requiredTotal = Math.max(reporterExtent, destinationExtent);

        return requiredTotal;
    }

    @Override
    public double getPreferredAngularExtent(LayoutData constraint, Graphics2D g2d) {
        LayoutData constraintForReporter = computeLowerLayerData(constraint);
        LayoutData constraintForChildren = computeUpperLayerData(constraint);

        double reporterExtent = super.getPreferredAngularExtent(constraintForReporter, g2d);
        double destinationExtent = layoutManager.getPreferredAngularExtent(constraintForChildren, g2d);

        logger.trace("Computing preffered extent from layout data {}. Reporter extent: {}, Destinations extent: {}",
                constraint, reporterExtent, destinationExtent);
        double requiredTotal = Math.max(reporterExtent, destinationExtent);

        return requiredTotal;
    }

    /**
     * Computes layout data for lower layer.
     *
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
     *
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
     *
     * @param data layout data
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
            if (child != null && child.getViewBounds() != null) {
                Area childArea = new Area(child.getViewBounds());
                area.add(childArea);
            }
        }

        return area;
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (layoutData == null) {
            return;
        }
        for (View child : getChildren()) {
            child.draw(g2d);
        }

        double preferredExtent = getPreferredAngularExtent(layoutData, g2d);
        boolean minimumView = false;


        // shrink layout data only for data to lower layer, since this view uses only lower layer (upper layer is for children)
        LayoutData wholeData = this.layoutData;
        this.layoutData = computeLowerLayerData(wholeData);

        if (layoutData.getAngularData().getAngleExtent() < preferredExtent) {
            minimumView = true;
        }

        // antialiasing of the shapes
        addRenderingHints(g2d);

        // draw the icon
        drawIcon(g2d, minimumView);

        // draw bounds
        drawBounds(g2d);

        // draw text
        drawText(g2d, minimumView);

        // draw managementIcons
        drawManagementIcons(g2d);

        // draw execution info
        drawExecutionInfo(g2d);

        // set layout data of this view back to whole data containing both layers.
        this.layoutData = wholeData;

    }

    @Override
    public void setLayoutData(LayoutData layoutData) {
        // do not call super since it sets also layout manager data
        // super.setLayoutData(computeLowerLayerData(layoutData));
        this.layoutData = layoutData;

        // all data provided serves as constraint for child views
        layoutManager.setConstraint(computeUpperLayerData(layoutData));
    }
}
