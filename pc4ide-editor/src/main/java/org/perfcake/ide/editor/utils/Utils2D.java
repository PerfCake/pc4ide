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

package org.perfcake.ide.editor.utils;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.perfcake.ide.editor.layout.AngularData;

/**
 * This non-instantiable class provides set of static helper methods for working with java 2d.
 *
 * @author Jakub Knetl
 */
public class Utils2D {


    /**
     * Rotates point around center.
     *
     * @param point          point to be roated
     * @param angle          rotation angle in degrees
     * @param rotationCenter center of the rotation
     * @return rotated point
     */
    public static Point2D rotatePoint(Point2D point, double angle, Point2D rotationCenter) {

        AffineTransform transform = new AffineTransform();
        transform.setToRotation(Math.toRadians(angle), rotationCenter.getX(), rotationCenter.getY());
        Point2D rotatedPoint = new Point2D.Double();

        return transform.transform(point, rotatedPoint);
    }

    /**
     * Gets center of an rectangle.
     *
     * @param rectangle rectangle.
     * @return Center point of an rectangle.
     */
    public static Point2D getCenter(Rectangle2D rectangle) {
        return new Point2D.Double(rectangle.getCenterX(), rectangle.getCenterY());
    }

    /**
     * Compoutes upper left corner of an rectangle from its center, width and height.
     *
     * @param center center of an rectangle
     * @param width  width
     * @param height height
     * @return rectangle
     */
    public static Rectangle2D getUpperLeftCorner(Point2D center, double width, double height) {
        Rectangle2D rectangle2D = new Rectangle2D.Double(center.getX() - width / 2, center.getY() - height / 2, width, height);
        return rectangle2D;
    }

    /**
     * Computes diagonal of an rectangle.
     *
     * @param rectangle rectangle
     * @return diagonal length of an rectangle
     */
    public static Double getRectangleDiagonal(Rectangle2D rectangle) {
        return Math.sqrt(Math.pow(rectangle.getWidth(), 2) + Math.pow(rectangle.getHeight(), 2));
    }

    /**
     * Return an angle of the middle of this angular data. For example, if startAngle is 60 and angular extent is 90, then this method
     * will return 75.
     *
     * @param angularData angular data
     * @return half of angle size of angular data
     */
    public static double getMiddleAngle(AngularData angularData) {
        return angularData.getStartAngle() + (angularData.getAngleExtent() / 2);
    }
}
