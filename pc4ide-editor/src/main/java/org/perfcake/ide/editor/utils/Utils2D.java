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

import java.awt.FontMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
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
     * Computes diagonal of an rectangle defined by its dimension.
     *
     * @param dimension dimension of an rectangle
     * @return diagonal length of an rectangle
     */
    public static Double getRectangleDiagonal(Dimension2D dimension) {
        return Math.sqrt(Math.pow(dimension.getWidth(), 2) + Math.pow(dimension.getHeight(), 2));
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

    /**
     * Computes part of the string which will fit into the width. If string do not fit into width limit, then only part of the
     * string is returned. Returned text may contain additional characters indicating, that text is not rendered completely.
     *
     * @param text        text which should be rendered
     * @param fontMetrics metrics for the text
     * @param widthLimit  maximum widht of the text.
     * @return Whole or part of the string which will fit into widthLimit. If only part of the text is returned, then
     *     String may contain additional characters indicating incompleteness of the text.
     */
    public static String computeRenderedPart(String text, FontMetrics fontMetrics, double widthLimit) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        if (widthLimit <= 0) {
            return "";
        }
        double textWidth = fontMetrics.stringWidth(text);
        String result;

        if (textWidth > widthLimit) {
            String dots = "...";
            double averageCharWidth = textWidth / text.length();
            int estimatedChars = (int) (widthLimit / averageCharWidth);

            String shortenedText = text.substring(0, Math.min(estimatedChars, text.length()));
            textWidth = fontMetrics.stringWidth(shortenedText + dots);

            // if shortening by average width is not enough then try to remove char one by one
            while (textWidth > widthLimit && !shortenedText.isEmpty()) {
                shortenedText = shortenedText.substring(0, shortenedText.length() - 1);
                textWidth = fontMetrics.stringWidth(shortenedText + dots);
            }

            if (!shortenedText.isEmpty()) {  // if shortened text is not empty then there is a text which fits together with dots
                shortenedText = shortenedText + dots;
            }

            result = shortenedText;
        } else {
            result = text;
        }
        return result;

    }
}
