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
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.perfcake.ide.editor.comparator.ViewComparator;
import org.perfcake.ide.editor.layout.AbstractLayoutManager;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CircularSectorLayoutManager manages the layout of graphical part of PerfCake editor.
 */
public class CircularSectorLayoutManager extends AbstractLayoutManager {

    static final Logger logger = LoggerFactory.getLogger(CircularSectorLayoutManager.class);
    public static final int BOTTOM_PADDING = 30;

    private Comparator<View> viewComparator = new ViewComparator();

    private boolean fill;

    /**
     * if true, then center of sectors is not in the real center of the panel, but it is moved, so that sectors are as big as possible.
     */
    private boolean adjustCenter;
    private Point2D adjustedCenter;

    /**
     * Creates new CircularSectorLayoutManager.
     */
    public CircularSectorLayoutManager() {
        this(false, false);
    }

    /**
     * Creates new CircularSectorLayoutManager.
     *
     * @param fillExtent   should layout manager fill whole angle extent provided?
     * @param adjustCenter adjust center of the pane so that view is as big as possible.
     */
    public CircularSectorLayoutManager(boolean fillExtent, boolean adjustCenter) {
        this.fill = fillExtent;
        this.adjustCenter = adjustCenter;
    }

    @Override
    public void layout(Graphics2D g2d) {
        Map<View, Double> extentMap = new HashMap<>();

        double requestedExtentByChildren = 0.0;

        for (View child : children) {
            double preferredSize = child.getPreferredAngularExtent(constraints, g2d);
            extentMap.put(child, preferredSize);
            requestedExtentByChildren += preferredSize;
        }


        double angleExtentConstraint = constraints.getAngularData().getAngleExtent();
        if (requestedExtentByChildren <= angleExtentConstraint) {
            layoutComponents(extentMap, requestedExtentByChildren);
        } else {
            // use minimum view
            double adjustedExtent = requestedExtentByChildren;
            Map<View, Double> minimumViews = new HashMap<>();
            while (adjustedExtent > angleExtentConstraint && !extentMap.isEmpty()) {
                // TODO: choose view not by iteration order but rather by rules
                View view = extentMap.keySet().iterator().next();

                double minimumViewExtent = view.getMinimumAngularExtent(constraints, g2d);
                adjustedExtent -= extentMap.get(view);
                extentMap.remove(view);
                adjustedExtent += minimumViewExtent;
                minimumViews.put(view, minimumViewExtent);
            }

            if (adjustedExtent <= angleExtentConstraint) {

                minimumViews.putAll(extentMap); // put all views into minimum views
                double sum = 0;
                for (Map.Entry<View, Double> entry : minimumViews.entrySet()) {
                    sum += entry.getValue();
                }

                layoutComponents(minimumViews, sum);
            } else {
                //even when using components minimum sizes, the angular extent overflows constraints
                // TODO: inspector requested larger angular extent than is available
                // we need to somehow implement shirnking of some views!
                logger.warn("Too large angular extent. Requested: {}, Provided: {}",
                        requestedExtentByChildren,
                        constraints.getAngularData().getAngleExtent());
            }
        }


    }

    protected void layoutComponents(Map<View, Double> extentMap, double extentSum) {
        double angleExtentConstraint = constraints.getAngularData().getAngleExtent();
        double startAngle = constraints.getAngularData().getStartAngle();

        LayoutData adjustedConstraints = constraints;

        if (adjustCenter) {
            adjustedConstraints = adjustCenter(extentSum);
        }

        for (View v : children) {
            final LayoutData data = new LayoutData(adjustedConstraints);
            Double angleExtent = extentMap.get(v);

            if (fill) {
                double percentage = angleExtent / extentSum;
                angleExtent = percentage * angleExtentConstraint;
            }
            data.getAngularData().setAngleExtent(angleExtent);
            data.getAngularData().setStartAngle(startAngle);

            v.setLayoutData(data); // sets layout data

            // move startAgle by angular extent of view v
            startAngle += angleExtent;
        }
    }

    private LayoutData adjustCenter(double extentSum) {
        LayoutData adjustedConstraints;
        adjustedConstraints = new LayoutData(constraints);

        double y1 = constraints.getCenter().getY()
                + constraints.getRadiusData().getOuterRadius() * Math.sin(Math.toRadians(
                constraints.getAngularData().getStartAngle()));

        // angle of the closing arc of the last sector
        double endAngle = constraints.getAngularData().getStartAngle() + extentSum;

        // if angle is larger than 450, than the view contains sector with 450 angle, which is has greater y coordinate
        // so we must compute with the worst situation in order not to strip part of the view out.
        if (endAngle > 450) {
            endAngle = 450;
        }
        double y2 = constraints.getCenter().getY()
                + constraints.getRadiusData().getOuterRadius() * Math.sin(Math.toRadians(
                endAngle));

        logger.trace("Center vertical adjustment. Current position: {}, y1: {}, y2: {}", constraints.getCenter().getY(), y1, y2);
        logger.trace("Center vertical adjustment. y1 angle: {}, y2 angle: {}",
                constraints.getAngularData().getStartAngle(),
                endAngle);


        double y = constraints.getCenter().getY();
        double angle = 0;

        if (y1 > y) {
            y = y1;
            angle = constraints.getAngularData().getStartAngle();
        }

        if (y2 > y) {
            y = y2;
            angle = endAngle;
        }

        double verticalMove = Math.max(0, constraints.getHeight() - y - BOTTOM_PADDING);
        logger.trace("Possible vertical move: {}. Total height: {}", verticalMove, constraints.getHeight());
        if (verticalMove > 0) { // is it possible to move view vertically?

            double possibleVerticalIncrease = verticalMove;

            // we don't use all increase in order to leave some blank space below
            //possibleVerticalIncrease = 0.85 * possibleVerticalIncrease;

            // we multiple by 0.9 since we want to leave 10% of width free
            double possibleHorizontalIncrease = (constraints.getWidth() / 2) * 0.9 - constraints.getRadiusData().getOuterRadius();
            // because of multiplication by 0.9, it is possible that horizontal increase is negative, so we set it to zero in that case
            possibleHorizontalIncrease = Math.max(possibleHorizontalIncrease, 0);
            double increase = Math.min(possibleHorizontalIncrease, possibleVerticalIncrease);
            logger.trace("horizontalIncrease: {}, verticalIncrease: {}, increase: {}",
                    possibleHorizontalIncrease, possibleVerticalIncrease, increase);

            /* we cannot move increase with whole vertical move, because extending outer radius could cause
            getting part of a view out of bounds */
            double subtrahend = increase * Math.sin(Math.toRadians(angle));
            logger.trace("Using increase: {}, increse correction: {}", increase, subtrahend);
            increase = increase - subtrahend;
            assert increase >= 0;

            double adjustedCenterY = constraints.getCenter().getY() + increase;
            adjustedCenter = new Point2D.Double(constraints.getCenter().getX(), adjustedCenterY);
            adjustedConstraints.setExplicitCenter(adjustedCenter);

            adjustedConstraints.getRadiusData().setOuterRadius(constraints.getRadiusData().getOuterRadius() + increase);
        } else {
            adjustedCenter = new Point2D.Double(constraints.getWidth() / 2, constraints.getHeight() / 2);
            adjustedConstraints.setExplicitCenter(null);
        }

        return adjustedConstraints;
    }

    /*
     * This layout manager computes required angular data based on radius data constraint.
     */
    @Override
    public double getMinimumAngularExtent(LayoutData constraint, Graphics2D g2d) {
        double requiredExtent = 0;

        if (children == null) {
            return 0;
        }
        for (View child : children) {
            requiredExtent += child.getMinimumAngularExtent(constraint, g2d);
        }

        return requiredExtent;
    }

    @Override
    public double getPreferredAngularExtent(LayoutData constraint, Graphics2D g2d) {
        double requiredExtent = 0;

        if (children == null) {
            return 0;
        }
        for (View child : children) {
            double childExtent = child.getPreferredAngularExtent(constraint, g2d);
            logger.trace("Child extent: {}", childExtent);
            requiredExtent += childExtent;
        }

        logger.trace("Preferred extent: {}", requiredExtent);

        return requiredExtent;
    }

    @Override
    public void add(View component) {
        super.add(component);
        Collections.sort(children, viewComparator);
    }

    public Point2D getAdjustedCenter() {
        return adjustedCenter;
    }

}
