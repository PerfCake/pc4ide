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

package org.perfcake.ide.editor.view.impl;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.RadiusData;
import org.perfcake.ide.editor.swing.icons.ResizableIcon;
import org.perfcake.ide.editor.utils.Utils2D;
import org.perfcake.ide.editor.view.AbstractView;
import org.perfcake.ide.editor.view.Pair;

/**
 * Represents a view of the sector.
 *
 * @author jknetl
 */
public abstract class SectorView extends AbstractView {

    private static final int HEADER_BOTTOM_SPACE = 50;
    private static final int PADDING = 10;

    protected ResizableIcon icon;
    protected String header;
    protected Shape bounds;

    /**
     * creates new sector view.
     *
     * @param icon icon of the inspector in the sector
     */
    public SectorView(ResizableIcon icon) {
        this.icon = icon;
    }

    /* (non-Javadoc)
     * @see org.perfcake.ide.editor.view.View#draw(java.awt.Graphics)
     */
    @Override
    public void draw(Graphics2D g2d) {

        // if we have no layout data set, then we cannot draw
        if (layoutData == null) {
            return;
        }
        //antialiasing
        final Map<Object, Object> hints = new HashMap<>();
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.addRenderingHints(hints);

        // draw shape of the sector
        final Area boundArea = drawBounds();

        // draw the icon
        drawIcon(g2d);

        final Stroke defaultStorke = g2d.getStroke();
        if (isSelected()) {
            final Stroke selectedStroke = new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            g2d.setStroke(selectedStroke);
        }

        g2d.draw(boundArea);
        g2d.setStroke(defaultStorke);

        drawText(g2d);

    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    private Area drawBounds() {

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

    private void drawIcon(Graphics2D g2d) {
        if (icon != null) {
            Rectangle2D iconBounds = getIconBounds(layoutData);

            // we may pass null as inspector since our icon implementation completely ignores this argument
            icon.paintIcon(null, g2d, (int) iconBounds.getX(), (int) iconBounds.getY());
        }
    }

    protected Rectangle2D getIconBounds(LayoutData layoutData) {

        double iconCenterX = layoutData.getCenter().getX()
                + (layoutData.getRadiusData().getInnerRadius() + PADDING + icon.getIconWidth() / 2);
        double iconCenterY = layoutData.getCenter().getY();

        double theta = Utils2D.getMiddleAngle(layoutData.getAngularData());
        Point2D location = Utils2D.rotatePoint(new Point2D.Double(iconCenterX, iconCenterY), theta, layoutData.getCenter());

        Rectangle2D iconBounds = Utils2D.getUpperLeftCorner(location, icon.getIconWidth(), icon.getIconHeight());

        return iconBounds;
    }

    protected void drawText(Graphics2D g2d) {
        final Point2D startOuterArcPoint = getStartOuterArcPoint(layoutData);
        final Point2D endOuterArcPoint = getEndOuterArcPoint(layoutData);
        final Point2D chordCenter = getChordCenter(startOuterArcPoint, endOuterArcPoint);

        final AffineTransform defaultTransform = g2d.getTransform();
        final FontRenderContext frc = g2d.getFontRenderContext();
        final Font font = g2d.getFont();
        final Rectangle2D headerBounds = font.getStringBounds(header, frc);

        Rectangle2D textDimension = computeTextBounds(g2d, layoutData.getWidth());

        //do not compoute text position from inside, but rather place it near to the outer radius:
        double textCenterX =
                layoutData.getCenter().getX()
                        + layoutData.getRadiusData().getOuterRadius()
                        - getChordDistanceFromOuterRadius(layoutData.getCenter(), chordCenter, layoutData.getRadiusData().getOuterRadius())
                        - PADDING
                        - (textDimension.getWidth() / 2);

        Point2D textCenter = new Point2D.Double(
                textCenterX,
                layoutData.getCenter().getY()
        );

        double theta = Utils2D.getMiddleAngle(layoutData.getAngularData());
        Point2D rotatedCenter = Utils2D.rotatePoint(textCenter, theta, layoutData.getCenter());

        FontMetrics headerMetrics = g2d.getFontMetrics(font);
        Rectangle2D textRectangle = Utils2D.getUpperLeftCorner(rotatedCenter, textDimension.getWidth(), textDimension.getHeight());

        // if the angle is between 90 and 270, then we need to rotate the text by 180 degrees, otherwise it would be upside down
        if (theta > 90 && theta < 270) {
            theta += 180;
        }
        g2d.rotate(Math.toRadians(theta), rotatedCenter.getX(), rotatedCenter.getY());

        // TODO: find out why 1*ascent() is not enough ???
        double y = textRectangle.getY() + 2.5 * headerMetrics.getAscent() + PADDING;
        g2d.drawString(header, (float) textRectangle.getX(), (float) y);

        List<Pair> additionalData = getAdditionalData();
        FontMetrics additionalTextMetrics = g2d.getFontMetrics(font);

        for (Pair p : additionalData) {
            y += additionalTextMetrics.getHeight();
            g2d.drawString(p.getKey() + ": " + p.getValue(), (float) (textRectangle.getX()), (float) y);
        }
        g2d.setTransform(defaultTransform);
    }

    private Point2D.Double getChordCenter(Point2D startOuterArcPoint, Point2D endOuterArcPoint) {
        return new Point2D.Double(
                (startOuterArcPoint.getX() + endOuterArcPoint.getX()) / 2,
                (startOuterArcPoint.getY() + endOuterArcPoint.getY()) / 2);
    }

    private Point2D.Double getEndOuterArcPoint(LayoutData data) {
        return new Point2D.Double(
                layoutData.getCenter().getX() + layoutData.getRadiusData().getOuterRadius()
                        * Math.cos(Math.toRadians(
                        -(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent()))),
                layoutData.getCenter().getY() + layoutData.getRadiusData().getOuterRadius()
                        * Math.sin(Math.toRadians(
                        -(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent()))));
    }

    private Point2D.Double getStartOuterArcPoint(LayoutData data) {
        return new Point2D.Double(layoutData.getCenter().getX() + layoutData.getRadiusData().getOuterRadius()
                * Math.cos(Math.toRadians(-layoutData.getAngularData().getStartAngle())),
                layoutData.getCenter().getY() + layoutData.getRadiusData().getOuterRadius()
                        * Math.sin(Math.toRadians(-layoutData.getAngularData().getStartAngle())));
    }

    private double getChordDistanceFromOuterRadius(Point2D center, Point2D chordCenter, double outerRadius) {
        return outerRadius - center.distance(chordCenter);
    }

    private Rectangle2D computeTextBounds(Graphics2D g2d, double maximumWidth) {
        final FontRenderContext frc = g2d.getFontRenderContext();
        final Font font = g2d.getFont();
        final Rectangle2D headerBounds = font.getStringBounds(header, frc);

        FontMetrics metrics = g2d.getFontMetrics(font);

        List<Pair> additionalText = getAdditionalData();

        int additionalLines = 0;
        int longestLineWidth = 0;
        for (Pair pair : additionalText) {
            int width = metrics.stringWidth(pair.getKey() + ": " + pair.getValue());
            if (width > longestLineWidth) {
                longestLineWidth = width;
            }
            additionalLines += (1 + width / maximumWidth);
        }

        int additionalHeight = additionalLines * metrics.getHeight() + metrics.getAscent();

        double totalWidth = Math.max(headerBounds.getWidth(), longestLineWidth);
        double totalHeight = headerBounds.getHeight() + HEADER_BOTTOM_SPACE + additionalHeight;

        return new Rectangle2D.Double(0, 0, totalWidth, totalHeight);
    }

    private String joinText(List<Pair> text) {
        if (text == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < text.size(); i++) {
            Pair p = text.get(i);
            builder.append(p.getKey()).append(": ").append(p.getValue());

            if (i + 1 < text.size()) { // if there will be next iteration
                builder.append(", ");
            }
        }

        return builder.toString();
    }

    @Override
    public Shape getViewBounds() {
        return bounds;
    }

    @Override
    public LayoutData getMinimumSize(LayoutData constraint, Graphics2D g2d) {

        if (constraint.getRadiusData() == null
                || constraint.getRadiusData().getInnerRadius() == 0
                || constraint.getRadiusData().getOuterRadius() == 0) {
            // it has no point to compute anything if we dont know radius
            return constraint;
        }

        Rectangle2D iconBounds = getIconBounds(constraint);

        // we assume that width constraint is positive and large enough!
        // it should be ensured by sufficient minimum size on editor as a whole
        double textMaximumWidth = computeTextMaximumWidth(constraint.getRadiusData(), Utils2D.getRectangleDiagonal(iconBounds));
        Rectangle2D textBounds = computeTextBounds(g2d, textMaximumWidth);


        Double minimumAngluarExtentForIcon = getMinimumAngle(iconBounds, constraint.getRadiusData().getInnerRadius());
        double distanceOfObject = constraint.getRadiusData().getInnerRadius() + 2 * PADDING + iconBounds.getWidth();
        Double minimumAngluarExtentForText = getMinimumAngle(textBounds, distanceOfObject);
        LayoutData minimumSize = new LayoutData(constraint);
        minimumSize.getAngularData().setAngleExtent(Math.min(minimumAngluarExtentForIcon, minimumAngluarExtentForText));
        return minimumSize;
    }

    /**
     * Compoutes text maximum width.
     *
     * @param radiusData   radius data
     * @param iconDiagonal diagonal length of icon
     * @return maximum possible width of text.
     */
    private double computeTextMaximumWidth(RadiusData radiusData, double iconDiagonal) {
        return radiusData.getOuterRadius() // outer radius
                - radiusData.getInnerRadius()   // minus inner radius
                - PADDING // minus space between inner radius and icon
                - iconDiagonal // minus icon diagonal
                - PADDING // minus space between icon and text
                - PADDING;
    }

    /**
     * Computes minimum angle required so that object will fit within the radius from the given distance.
     *
     * @param object           object
     * @param distanceOfObject distance of object
     * @return minimum angle
     */
    private Double getMinimumAngle(Rectangle2D object, double distanceOfObject) {

        // We cannot use width nor height of the object since we don't know from which direction we want to enclose the object
        // therefore we will use half of diagonal ( it effectively computes required distance of circle which encloses the object)
        double diagonal = Utils2D.getRectangleDiagonal(object);

        return Math.toDegrees(2 * Math.atan((diagonal / 2) / distanceOfObject));
    }

    /**
     * @return List of additional pairs of key value, which this view will draw into surface along with header.
     */
    protected abstract List<Pair> getAdditionalData();
}
