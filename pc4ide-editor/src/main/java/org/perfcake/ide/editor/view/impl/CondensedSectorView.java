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
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.RadiusData;
import org.perfcake.ide.editor.swing.icons.ResizableIcon;
import org.perfcake.ide.editor.utils.Utils2D;

/**
 * Condensed sector view is a {@link SimpleSectorView}, which places icon above the text rather than next to a text.
 *
 * @author Jakub Knetl
 */
public abstract class CondensedSectorView extends SimpleSectorView {

    /**
     * creates new sector view.
     *
     * @param icon icon of the inspector in the sector
     */
    public CondensedSectorView(ResizableIcon icon) {
        super(icon);
    }

    @Override
    protected Rectangle2D computeIconBounds(Graphics2D g2d, LayoutData layoutData) {
        double iconCenterX = layoutData.getCenter().getX() + (computeMiddleRadius(layoutData));
        double iconCenterY = layoutData.getCenter().getY();

        double textHeight = computeTextDimension(g2d, layoutData).getHeight();
        double totalHeight = icon.getIconHeight() + PADDING + textHeight;

        double iconDiagonal = Utils2D.getRectangleDiagonal(new Rectangle2D.Double(0, 0, icon.getIconWidth(), icon.getIconHeight()));

        double theta = Utils2D.getMiddleAngle(layoutData.getAngularData());
        if (isReversed(theta)) {
            iconCenterY = iconCenterY + totalHeight / 2 - iconDiagonal / 2;
        } else {

            iconCenterY = iconCenterY - totalHeight / 2 + iconDiagonal / 2;
        }

        Point2D location = Utils2D.rotatePoint(new Point2D.Double(iconCenterX, iconCenterY), theta, layoutData.getCenter());

        Rectangle2D iconBounds = Utils2D.getUpperLeftCorner(location, icon.getIconWidth(), icon.getIconHeight());

        return iconBounds;
    }

    protected double computeMiddleRadius(LayoutData data) {
        return (data.getRadiusData().getInnerRadius() + data.getRadiusData().getOuterRadius()) / 2;
    }

    @Override
    protected double computeTextMaximumWidth(RadiusData radiusData, double iconDiagonal) {
        return radiusData.getOuterRadius() // outer radius
                - radiusData.getInnerRadius()   // minus inner radius
                - PADDING // minus space between inner radius and icon
                - PADDING;
    }

    @Override
    protected Rectangle2D computeTextBounds(Graphics2D g2d, LayoutData layoutData) {
        final Point2D startOuterArcPoint = getStartOuterArcPoint(layoutData);
        final Point2D endOuterArcPoint = getEndOuterArcPoint(layoutData);
        final Point2D chordCenter = getChordCenter(startOuterArcPoint, endOuterArcPoint);

        Rectangle2D textDimension = computeTextDimension(g2d, layoutData);

        double chordDistanceFromOuterRadius = getChordDistanceFromOuterRadius(layoutData.getCenter(),
                chordCenter,
                layoutData.getRadiusData().getOuterRadius());

        //do not compute text position from inside, but rather place it near to the outer radius:
        double textCenterX =
                layoutData.getCenter().getX()
                        + layoutData.getRadiusData().getOuterRadius()
                        - chordDistanceFromOuterRadius
                        - PADDING
                        - (textDimension.getWidth() / 2);

        double textCenterY = layoutData.getCenter().getY();
        double theta = Utils2D.getMiddleAngle(layoutData.getAngularData());
        double iconDiagonal = Utils2D.getRectangleDiagonal(computeIconDimension());
        if (isReversed(theta)) {
            textCenterY = textCenterY - iconDiagonal / 2 - PADDING;
        } else {
            textCenterY = textCenterY + iconDiagonal / 2 + PADDING;
        }
        Point2D textCenter = new Point2D.Double(textCenterX, textCenterY);

        Point2D rotatedCenter = Utils2D.rotatePoint(textCenter, theta, layoutData.getCenter());
        Rectangle2D textRectangle = Utils2D.getUpperLeftCorner(rotatedCenter, textDimension.getWidth(), textDimension.getHeight());

        return textRectangle;
    }

    @Override
    public LayoutData getMinimumSize(LayoutData constraint, Graphics2D g2d) {
        if (constraint == null
                || constraint.getRadiusData() == null
                || constraint.getRadiusData().getInnerRadius() == 0
                || constraint.getRadiusData().getOuterRadius() == 0) {
            return constraint;
        }

        Rectangle2D iconBounds = computeIconBounds(g2d, constraint);
        Rectangle2D textBounds = computeTextDimension(g2d, constraint);

        double iconDiagonal = Utils2D.getRectangleDiagonal(iconBounds);

        double minimumAngularExtent = getMinimumAngle(iconDiagonal + iconDiagonal,
                constraint.getRadiusData().getInnerRadius() + PADDING);
        LayoutData minimumSize = new LayoutData(constraint);
        minimumSize.getAngularData().setAngleExtent(minimumAngularExtent);

        return minimumSize;
    }
}
