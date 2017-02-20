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
import java.util.Map;

import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.view.AbstractView;
import org.perfcake.ide.editor.view.ComponentView;
import org.perfcake.ide.editor.view.icons.ResizableIcon;

/**
 * Represents a view of the sector.
 *
 * @author jknetl
 */
public class SectorView extends AbstractView {

    public String getComponentName() {
        return componentName;
    }

    private String componentName;
    private ResizableIcon icon;

    private Shape bounds;

    /**
     * creates new sector view.
     * @param parent parent view
     * @param componentName name of the inspector in the sector
     * @param icon icon of the inspector in the sector
     */
    public SectorView(ComponentView parent, String componentName, ResizableIcon icon) {
        super(parent);
        this.componentName = componentName;
        this.icon = icon;
    }

    /* (non-Javadoc)
     * @see org.perfcake.ide.editor.view.ComponentView#draw(java.awt.Graphics)
     */
    @Override
    public void draw(Graphics2D g2d) {
        //antialiasing
        final Map<Object, Object> hints = new HashMap<>();
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.addRenderingHints(hints);

        final Arc2D outerArc = new Arc2D.Double();
        outerArc.setArcByCenter(layoutData.getCenter().getX(), layoutData.getCenter().getY(), layoutData.getRadiusData().getOuterRadius(),
                layoutData.getAngularData().getStartAngle(), layoutData.getAngularData().getAngleExtent(),
                Arc2D.PIE);

        //angular overlap needed since we need to make sure that innerArc overlaps completely over innerArc
        //if we use same angles than it could happen that innerArc is little bit less because of double conversion
        final double angularOverlap = 5;
        final Arc2D innerArc = new Arc2D.Double();
        innerArc.setArcByCenter(layoutData.getCenter().getX(), layoutData.getCenter().getY(), layoutData.getRadiusData().getInnerRadius(),
                layoutData.getAngularData().getStartAngle() - angularOverlap,
                layoutData.getAngularData().getAngleExtent() + 2 * angularOverlap, Arc2D.PIE);

        drawIcon(g2d);

        final Area boundArea = new Area(outerArc);
        boundArea.subtract(new Area(innerArc));

        bounds = boundArea;

        final Stroke defaultStorke = g2d.getStroke();
        if (isSelected()) {
            final Stroke selectedStroke = new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            g2d.setStroke(selectedStroke);
        }

        g2d.draw(boundArea);
        g2d.setStroke(defaultStorke);

        drawSectorName(g2d);

    }

    private void drawIcon(Graphics2D g2d) {
        if (icon != null) {
            Rectangle2D iconBounds = getIconBounds(layoutData);

            // we may pass null as inspector since our icon implementation completely ignores this argument
            // icon.paintIcon(null, g2d, (int) iconX, (int) iconY);
            icon.paintIcon(null, g2d, (int) iconBounds.getX(), (int) iconBounds.getY());
        }
    }

    protected Rectangle2D getIconBounds(LayoutData layoutData) {
        final double iconX = (layoutData.getCenter().getX() - icon.getIconWidth() / 2)
                + (1.5 * layoutData.getRadiusData().getInnerRadius() + icon.getIconWidth() / 2)
                * Math.cos(Math.toRadians(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent() / 2));
        final double iconY = (layoutData.getCenter().getY() - icon.getIconHeight() / 2)
                - (1.5 * layoutData.getRadiusData().getInnerRadius() + icon.getIconHeight() / 2)
                * Math.sin(Math.toRadians(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent() / 2));
        return new Rectangle2D.Double(iconX, iconY, icon.getIconWidth(), icon.getIconHeight());
    }

    protected void drawSectorName(Graphics2D g2d) {
        final Point2D startOuterArcPoint = new Point2D.Double(layoutData.getCenter().getX() + layoutData.getRadiusData().getOuterRadius()
                * Math.cos(Math.toRadians(-layoutData.getAngularData().getStartAngle())),
                layoutData.getCenter().getY() + layoutData.getRadiusData().getOuterRadius()
                        * Math.sin(Math.toRadians(-layoutData.getAngularData().getStartAngle())));

        final Point2D endOuterArcPoint = new Point2D.Double(
                layoutData.getCenter().getX() + layoutData.getRadiusData().getOuterRadius()
                        * Math.cos(Math.toRadians(
                                -(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent()))),
                layoutData.getCenter().getY() + layoutData.getRadiusData().getOuterRadius()
                        * Math.sin(Math.toRadians(
                                -(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent()))));

        final Point2D chordCenter = new Point2D.Double(
                (startOuterArcPoint.getX() + endOuterArcPoint.getX()) / 2,
                (startOuterArcPoint.getY() + endOuterArcPoint.getY()) / 2);

        final AffineTransform defaultTransform = g2d.getTransform();
        final FontRenderContext frc = g2d.getFontRenderContext();
        final Font font = g2d.getFont();
        final Rectangle2D fontBounds = font.getStringBounds(componentName, frc);

        final Point2D textCenter = new Point2D.Double(
                chordCenter.getX() - fontBounds.getHeight() * Math.cos(Math.toRadians(
                        -(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent() / 2))),
                chordCenter.getY() - fontBounds.getHeight() * Math.sin(Math.toRadians(
                        -(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent() / 2))));

        final Double theta = 180 - (layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent() / 2);

        g2d.rotate(Math.toRadians(theta), textCenter.getX(), textCenter.getY());
        // g2d.drawString(componentName, (float) (textCenter.getX() - fontBounds.getWidth() / 2),
        // (float) (textCenter.getY() - fontBounds.getHeight() / 2));
        g2d.drawString(componentName, (float) (textCenter.getX()), (float) (textCenter.getY()));
        g2d.setTransform(defaultTransform);
    }

    @Override
    public Shape getViewBounds() {
        return bounds;
    }

    @Override
    public LayoutData getMinimumSize(LayoutData constraint, Graphics2D g2d) {
        // TODO: Compute size from the content (+ child)

        Rectangle2D iconBounds = getIconBounds(constraint);

        Double minAngularExtent = Math.toDegrees(2 * Math.atan((iconBounds.getHeight() / 2) / constraint.getRadiusData().getInnerRadius()));
        LayoutData minimumSize = new LayoutData(constraint);
        minimumSize.getAngularData().setAngleExtent(minAngularExtent);
        return minimumSize;
    }

}
