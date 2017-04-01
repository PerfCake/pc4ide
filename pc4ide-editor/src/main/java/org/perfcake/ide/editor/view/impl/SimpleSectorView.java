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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.perfcake.ide.editor.awt.geom.DimensionDouble;
import org.perfcake.ide.editor.colors.NamedColor;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.RadiusData;
import org.perfcake.ide.editor.swing.icons.ResizableIcon;
import org.perfcake.ide.editor.utils.Utils2D;
import org.perfcake.ide.editor.view.Pair;
import org.perfcake.ide.editor.view.SectorView;

/**
 * Represents a view of the sector.
 *
 * @author jknetl
 */
public abstract class SimpleSectorView extends SectorView {

    /**
     * Space between header and additional text.
     */
    public static final int HEADER_BOTTOM_SPACE = 5;

    /**
     * padding between objects (e.g. header and icon)
     */
    public static final int PADDING = 10;

    /**
     * Space between management icons.
     */
    public static final int MANAGEMENT_ICON_SPACE = 5;

    protected int headerFontSize = 12;
    protected int additionalTextFontSize;

    protected ResizableIcon icon;
    protected String header;

    /**
     * creates new sector view.
     *
     * @param icon icon of the inspector in the sector
     */
    public SimpleSectorView(ResizableIcon icon) {
        super();
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

        // antialiasing of the shapes
        addRenderingHints(g2d);

        // draw the icon
        drawIcon(g2d);

        // draw bounds
        drawBounds(g2d);

        // draw text
        drawText(g2d);

        // draw managementIcons
        drawManagementIcons(g2d);
    }

    /**
     * Draws view bounds.
     *
     * @param g2d graphics context
     */
    protected void drawBounds(Graphics2D g2d) {
        // compute bounds
        final Area boundArea = computeBounds(layoutData);

        final Stroke defaultStroke = g2d.getStroke();
        if (isSelected()) {
            final Stroke selectedStroke = new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            g2d.setStroke(selectedStroke);
        }

        g2d.setColor(colorScheme.getColor(NamedColor.BASE_4));
        g2d.draw(boundArea);
        g2d.setStroke(defaultStroke);
    }

    /**
     * Draws icon.
     *
     * @param g2d graphics context.
     */
    protected void drawIcon(Graphics2D g2d) {
        if (icon != null) {
            Rectangle2D iconBounds = computeIconBounds(g2d, layoutData);

            Color iconColor = getIconColor();
            icon.setColor(iconColor);
            // we may pass null as inspector since our icon implementation completely ignores this argument
            icon.paintIcon(null, g2d, (int) iconBounds.getX(), (int) iconBounds.getY());
        }
    }

    /**
     * Draws the text.
     *
     * @param g2d graphics context.
     */
    protected void drawText(Graphics2D g2d) {
        Font defaultFont = g2d.getFont();
        final AffineTransform defaultTransform = g2d.getTransform();

        Rectangle2D textRectangle = computeTextBounds(g2d, layoutData);
        Point2D textCenter = Utils2D.getCenter(textRectangle);
        double iconDiagonal = Utils2D.getRectangleDiagonal(computeIconBounds(g2d, layoutData));
        double maximumWidthForText = computeTextMaximumWidth(layoutData.getRadiusData(), iconDiagonal);

        // if the angle is between 90 and 270, then we need to rotate the text by 180 degrees, otherwise it would be upside down
        double theta = Utils2D.getMiddleAngle(layoutData.getAngularData());
        if (isReversed(theta)) {
            theta += 180;
        }
        g2d.rotate(Math.toRadians(theta), textCenter.getX(), textCenter.getY());

        final Font headerFont = getHeaderFont(g2d);
        FontMetrics headerMetrics = g2d.getFontMetrics(headerFont);
        double y = textRectangle.getY() + headerMetrics.getAscent();

        g2d.setColor(colorScheme.getColor(NamedColor.BASE_6));
        // render header
        g2d.setFont(headerFont);
        String renderedHeader = Utils2D.computeRenderedPart(header, headerMetrics, maximumWidthForText);
        g2d.drawString(renderedHeader, (float) textRectangle.getX(), (float) y);
        //g2d.draw(textRectangle);

        y += HEADER_BOTTOM_SPACE;

        // render additional text
        List<Pair> additionalData = getAdditionalData();
        Font additionalTextFont = getAdditionalTextFont(g2d);
        FontMetrics additionalTextMetrics = g2d.getFontMetrics(additionalTextFont);
        g2d.setFont(additionalTextFont);
        g2d.setColor(colorScheme.getColor(NamedColor.BASE_4));
        for (Pair p : additionalData) {
            y += additionalTextMetrics.getHeight();
            String renderedPair = Utils2D.computeRenderedPart(p.getKey() + ": " + p.getValue(), headerMetrics, maximumWidthForText);
            g2d.drawString(renderedPair, (float) (textRectangle.getX()), (float) y);
        }
        g2d.setTransform(defaultTransform);
        g2d.setFont(defaultFont);
    }

    protected void drawManagementIcons(Graphics2D g2d) {

        List<Rectangle2D> iconBounds = computeManagementIconBounds(layoutData);

        for (int i = 0; i < iconBounds.size(); i++) {
            Rectangle2D bounds = iconBounds.get(i);
            ResizableIcon icon = managementIcons.get(i);

            icon.paintIcon(null, g2d, (int) bounds.getX(), (int) bounds.getY());
        }
    }

    protected boolean isReversed(double theta) {
        return theta > 90 && theta < 270;
    }

    /**
     * Computes icon bounds.
     *
     * @param layoutData layout data
     * @return bounds of icon.
     */
    protected Rectangle2D computeIconBounds(Graphics2D g2d, LayoutData layoutData) {

        double iconCenterX = layoutData.getCenter().getX()
                + (layoutData.getRadiusData().getInnerRadius() + PADDING + icon.getIconWidth() / 2);
        double iconCenterY = layoutData.getCenter().getY();

        double theta = Utils2D.getMiddleAngle(layoutData.getAngularData());
        Point2D location = Utils2D.rotatePoint(new Point2D.Double(iconCenterX, iconCenterY), theta, layoutData.getCenter());

        Rectangle2D iconBounds = Utils2D.getUpperLeftCorner(location, icon.getIconWidth(), icon.getIconHeight());

        return iconBounds;
    }

    /**
     * Computes bounds of the text before applying rotation. Therefore this rectangle edges are always parallel to
     * XIcon and Y axes. Before using this rectangle, you should apply rotation to it.
     *
     * @param g2d        graphics context
     * @param layoutData layout data
     * @return Rectangle bounds of a text before applying rotation.
     */
    protected Rectangle2D computeTextBounds(Graphics2D g2d, LayoutData layoutData) {
        final Point2D startOuterArcPoint = getStartOuterArcPoint(layoutData);
        final Point2D endOuterArcPoint = getEndOuterArcPoint(layoutData);
        final Point2D chordCenter = getChordCenter(startOuterArcPoint, endOuterArcPoint);

        Dimension2D textDimension = computeTextDimension(g2d, layoutData);

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

        Point2D textCenter = new Point2D.Double(
                textCenterX,
                layoutData.getCenter().getY()
        );

        double theta = Utils2D.getMiddleAngle(layoutData.getAngularData());
        Point2D rotatedCenter = Utils2D.rotatePoint(textCenter, theta, layoutData.getCenter());
        Rectangle2D textRectangle = Utils2D.getUpperLeftCorner(rotatedCenter, textDimension.getWidth(), textDimension.getHeight());

        return textRectangle;
    }

    /**
     * Computes dimension of a text.
     *
     * @param g2d        graphics context
     * @param layoutData layout data which require at least radius to be set
     * @return Rectangle which contains dimension of a text bounds.
     */
    protected Dimension2D computeTextDimension(Graphics2D g2d, LayoutData layoutData) {
        final FontRenderContext frc = g2d.getFontRenderContext();
        final Font font = g2d.getFont();
        final Rectangle2D headerBounds = font.getStringBounds(header, frc);

        double iconDiagonal = Utils2D.getRectangleDiagonal(computeIconDimension());
        double maximumWidth = computeTextMaximumWidth(layoutData.getRadiusData(), iconDiagonal);

        FontMetrics metrics = g2d.getFontMetrics(font);

        List<Pair> additionalText = getAdditionalData();

        int additionalLines = 0;
        int longestLineWidth = 0;
        for (Pair pair : additionalText) {
            int width = metrics.stringWidth(pair.getKey() + ": " + pair.getValue());
            if (width > longestLineWidth) {
                longestLineWidth = width;
            }
            additionalLines++;
        }

        int additionalHeight = additionalLines * metrics.getHeight() + metrics.getAscent();

        double totalWidth = Math.min(Math.max(headerBounds.getWidth(), longestLineWidth), maximumWidth);
        double totalHeight = headerBounds.getHeight() + HEADER_BOTTOM_SPACE + additionalHeight;

        return new DimensionDouble(totalWidth, totalHeight);
    }

    /**
     * Computes maximum possible width for a text.
     *
     * @param radiusData   radius data
     * @param iconDiagonal diagonal length of icon
     * @return maximum possible width of text.
     */
    protected double computeTextMaximumWidth(RadiusData radiusData, double iconDiagonal) {
        return radiusData.getOuterRadius() // outer radius
                - radiusData.getInnerRadius()   // minus inner radius
                - PADDING // minus space between inner radius and icon
                - iconDiagonal // minus icon diagonal
                - PADDING // minus space between icon and text
                - PADDING;
    }

    private List<Rectangle2D> computeManagementIconBounds(LayoutData data) {

        List<Rectangle2D> bounds = new ArrayList<>(managementIcons.size());

        double radius = data.getRadiusData().getOuterRadius();
        for (int i = managementIcons.size(); i > 0; i--) {

            ResizableIcon icon = managementIcons.get(i - 1);
            double iconDiagonal = Utils2D.getRectangleDiagonal(new Rectangle2D.Double(0, 0, icon.getIconWidth(), icon.getIconHeight()));
            double iconCenterX = data.getCenter().getX() + radius - iconDiagonal / 2 - MANAGEMENT_ICON_SPACE;
            double iconCenterY = data.getCenter().getY() - MANAGEMENT_ICON_SPACE - iconDiagonal / 2;

            double theta = data.getAngularData().getStartAngle() + data.getAngularData().getAngleExtent();
            Point2D location = Utils2D.rotatePoint(new Point2D.Double(iconCenterX, iconCenterY), theta, layoutData.getCenter());

            Rectangle2D iconBounds = Utils2D.getUpperLeftCorner(location, icon.getIconWidth(), icon.getIconHeight());

            bounds.add(iconBounds);

            radius = radius - iconDiagonal - MANAGEMENT_ICON_SPACE;
        }

        Collections.reverse(bounds);
        return bounds;
    }

    @Override
    public LayoutData getMinimumSize(LayoutData constraint, Graphics2D g2d) {

        if (constraint == null
                || constraint.getRadiusData() == null
                || constraint.getRadiusData().getInnerRadius() == 0
                || constraint.getRadiusData().getOuterRadius() == 0) {
            // it has no point to compute anything if we dont know radius
            return constraint;
        }

        Rectangle2D iconBounds = computeIconBounds(g2d, constraint);

        // we assume that width constraint is positive and large enough!
        // it should be ensured by sufficient minimum size on editor as a whole
        Dimension2D textBounds = computeTextDimension(g2d, constraint);

        double iconDiagonal = Utils2D.getRectangleDiagonal(iconBounds);
        Double minimumAngularExtentForIcon = getMinimumAngle(iconDiagonal, constraint.getRadiusData().getInnerRadius());
        double distanceOfObject = constraint.getRadiusData().getInnerRadius() + 2 * PADDING + iconBounds.getWidth();
        Double minimumAngularExtentForText = getMinimumAngle(textBounds.getHeight(), distanceOfObject);
        LayoutData minimumSize = new LayoutData(constraint);
        minimumSize.getAngularData().setAngleExtent(Math.max(minimumAngularExtentForIcon, minimumAngularExtentForText));
        return minimumSize;
    }

    /**
     * Computes minimum angle required so that object will fit within the radius from the given distance.
     *
     * @param size             size of a perpendicular edge
     * @param distanceOfObject distance of object
     * @return minimum angle
     */
    protected Double getMinimumAngle(double size, double distanceOfObject) {
        return 2 * Math.toDegrees(Math.atan((size / 2) / distanceOfObject));
    }

    /**
     * Computes rectangle which contains icon dimension.
     *
     * @return rectangle which contains icon dimension.
     */
    protected Dimension2D computeIconDimension() {
        return new Dimension(icon.getIconWidth(), icon.getIconHeight());
    }

    /**
     * @return ListIcon of additional pairs of key value, which this view will draw into surface along with header.
     */
    protected abstract List<Pair> getAdditionalData();

    /**
     * Get font for header.
     *
     * @param g2d graphics context
     * @return Font for the header
     */
    protected Font getHeaderFont(Graphics2D g2d) {
        Font f = g2d.getFont();
        f = f.deriveFont(Font.BOLD, headerFontSize);
        return f;
    }

    /**
     * Get font for additional text.
     *
     * @param g2d graphics context
     * @return Font for the additionalText
     */
    protected Font getAdditionalTextFont(Graphics2D g2d) {
        Font f = g2d.getFont();
        f = f.deriveFont(additionalTextFontSize);
        return f;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * @return Color of the icon.
     */
    protected abstract Color getIconColor();

}
