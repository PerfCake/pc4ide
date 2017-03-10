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

package org.perfcake.ide.editor.view;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.view.AbstractView;

/**
 * Sector view represents a view with circular sector shape. It defines useful method for managing sector.
 * @author Jakub Knetl
 */
public abstract class SectorView extends AbstractView {
    protected Shape bounds;

    protected Point2D.Double getChordCenter(Point2D startOuterArcPoint, Point2D endOuterArcPoint) {
        return new Point2D.Double(
                (startOuterArcPoint.getX() + endOuterArcPoint.getX()) / 2,
                (startOuterArcPoint.getY() + endOuterArcPoint.getY()) / 2);
    }

    protected Point2D.Double getEndOuterArcPoint(LayoutData data) {
        return new Point2D.Double(
                layoutData.getCenter().getX() + layoutData.getRadiusData().getOuterRadius()
                        * Math.cos(Math.toRadians(
                        -(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent()))),
                layoutData.getCenter().getY() + layoutData.getRadiusData().getOuterRadius()
                        * Math.sin(Math.toRadians(
                        -(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent()))));
    }

    protected Point2D.Double getStartOuterArcPoint(LayoutData data) {
        return new Point2D.Double(layoutData.getCenter().getX() + layoutData.getRadiusData().getOuterRadius()
                * Math.cos(Math.toRadians(-layoutData.getAngularData().getStartAngle())),
                layoutData.getCenter().getY() + layoutData.getRadiusData().getOuterRadius()
                        * Math.sin(Math.toRadians(-layoutData.getAngularData().getStartAngle())));
    }

    protected double getChordDistanceFromOuterRadius(Point2D center, Point2D chordCenter, double outerRadius) {
        return outerRadius - center.distance(chordCenter);
    }

    protected Area drawBounds() {

        // we multiply with -1 since setArcByCenter handles angle in counter clockwise direction
        double angularExtent = -1 * layoutData.getAngularData().getAngleExtent();
        double startAngle = -1 * layoutData.getAngularData().getStartAngle();


        // angular overlap needed since we need to make sure that innerArc overlaps completely over innerArc
        // if we use same angles than it could happen that innerArc is little bit less because of double conversion
        final double angularOverlap = 1;

        final Arc2D outerArc = new Arc2D.Double();
        outerArc.setArcByCenter(layoutData.getCenter().getX(), layoutData.getCenter().getY(), layoutData.getRadiusData().getOuterRadius(),
                startAngle, angularExtent,
                Arc2D.PIE);

        final Arc2D innerArc = new Arc2D.Double();
        innerArc.setArcByCenter(layoutData.getCenter().getX(), layoutData.getCenter().getY(), layoutData.getRadiusData().getInnerRadius(),
                startAngle + angularOverlap,
                angularExtent - 2 * angularOverlap, Arc2D.PIE);


        final Area boundArea = new Area(outerArc);
        boundArea.subtract(new Area(innerArc));

        bounds = boundArea;
        return boundArea;
    }

    @Override
    public Shape getViewBounds() {
        return bounds;
    }
}
