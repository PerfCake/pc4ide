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

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import org.perfcake.ide.editor.colors.NamedColor;
import org.perfcake.ide.editor.layout.AngularData;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.RadiusData;
import org.perfcake.ide.editor.layout.impl.CircularSectorLayoutManager;
import org.perfcake.ide.editor.swing.icons.ControlIcon;
import org.perfcake.ide.editor.swing.icons.control.DebugIcon;
import org.perfcake.ide.editor.swing.icons.control.PlayIcon;
import org.perfcake.ide.editor.swing.icons.control.StopIcon;
import org.perfcake.ide.editor.view.AbstractView;
import org.perfcake.ide.editor.view.View;

/**
 * Represents view of the whole scenario, which represents whole editor.
 *
 * @author jknetl
 */
public class ScenarioView extends AbstractView {

    private static final int MAXIMUM_ANGLE_EXTENT = 340;
    private static final int MAXIMUM_INNER_RADIUS = 200;
    public static final int START_ANGLE = 180;
    public static final int ICON_WIDTH = 28;
    public static final int ICON_HEIGTH = 28;
    private static final int PADDING_BEETWEN_ICONS = 35;

    private JComponent jComponent;
    private boolean isRunning = false;
    private PlayIcon playIcon;
    private StopIcon stopIcon;
    private DebugIcon debugIcon;

    /**
     * Creates new editor view inside of swing container.
     */
    public ScenarioView() {
        super();
        layoutManager = new CircularSectorLayoutManager(false, true);
    }

    /* (non-Javadoc)
     * @see org.perfcake.ide.editor.view.View#draw(java.awt.Graphics)
     */
    @Override
    public void draw(Graphics2D g2d) {
        layoutData = getConstraints();
        for (View child : getChildren()) {
            child.draw(g2d);
        }

        // draw management icons
        drawManagementIcons(g2d, layoutData);
    }

    protected void drawManagementIcons(Graphics2D g2d, LayoutData layoutData) {

        double iconsWidth = 0;

        for (ControlIcon icon : managementIcons) {
            iconsWidth += icon.getIconWidth();
        }

        iconsWidth += ((managementIcons.size() - 1) * PADDING_BEETWEN_ICONS);

        Point2D center = layoutData.getCenter();
        if ((layoutManager instanceof CircularSectorLayoutManager)) {
            Point2D adjustedCenter = ((CircularSectorLayoutManager) layoutManager).getAdjustedCenter();
            if (adjustedCenter != null) {
                center = adjustedCenter;
            }
        }
        int iconX = (int) (center.getX() - iconsWidth / 2);
        int iconY = (int) (center.getY() - ICON_HEIGTH / 2);

        for (ControlIcon icon : managementIcons) {
            icon.paintIcon(null, g2d, iconX, iconY);
            iconX += PADDING_BEETWEN_ICONS + icon.getIconWidth();
        }

    }

    /* (non-Javadoc)
     * @see org.perfcake.ide.editor.view.View#getViewBounds()
     */
    @Override
    public Shape getViewBounds() {
        return jComponent.getBounds();
    }

    @Override
    public double getMinimumAngularExtent(LayoutData constraint, Graphics2D g2d) {
        return layoutManager.getMinimumAngularExtent(constraint, g2d);
    }

    @Override
    public double getPreferredAngularExtent(LayoutData constraint, Graphics2D g2d) {
        return layoutManager.getPreferredAngularExtent(constraint, g2d);
    }

    /**
     * Invalidates the view. Since this is a root view, it also triggers
     * a validation process.
     */
    public void invalidate() {
        if (jComponent != null) {
            //ScenarioView is a root so it may trigger validation
            Graphics2D g2d = (Graphics2D) jComponent.getGraphics();
            if (g2d != null) {
                validate(g2d);
            }
        }
    }

    @Override
    public void validate(Graphics2D g2d) {
        // get drawing surface constraints
        final LayoutData data = getConstraints();
        layoutManager.setConstraint(data);

        super.validate(g2d);

        jComponent.repaint();
    }

    @Override
    protected void initManagementIcons() {
        playIcon = new PlayIcon(ICON_WIDTH, ICON_HEIGTH, colorScheme.getColor(NamedColor.ACCENT_4));
        stopIcon = new StopIcon(ICON_WIDTH, ICON_HEIGTH, colorScheme.getColor(NamedColor.ACCENT_1));
        debugIcon = new DebugIcon(ICON_WIDTH, ICON_HEIGTH, colorScheme.getColor(NamedColor.ACCENT_4));
        managementIcons.add(playIcon);
        managementIcons.add(debugIcon);
    }

    @Override
    protected String getToolTipText() {
        return null;
    }

    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Is scenario running.
     *
     * @param running running
     */
    public void setRunning(boolean running) {
        if (isRunning != running) {
            isRunning = running;
            managementIcons.clear();
            if (isRunning) {
                managementIcons.add(stopIcon);
            } else {
                managementIcons.add(playIcon);
                managementIcons.add(debugIcon);
            }
            invalidate();
        }
    }

    /**
     * Sets JComponent to which the view will be drawn.
     *
     * @param jComponent pane for drawing this view.
     */
    public void setJComponent(JComponent jComponent) {
        if (jComponent == null) {
            throw new IllegalArgumentException("jcomponent cannot be null.");
        }
        this.jComponent = jComponent;
        layoutManager.setConstraint(getConstraints());
        invalidate();
    }

    private LayoutData getConstraints() {
        final double outerRadius = (0.9 * Math.min(jComponent.getWidth(), jComponent.getHeight())) / 2;
        final double innerRadius = Math.min(MAXIMUM_INNER_RADIUS, (0.25 * Math.min(jComponent.getWidth(), jComponent.getHeight())) / 2);
        final RadiusData radiusData = new RadiusData(innerRadius, outerRadius);
        final AngularData angularData = new AngularData(START_ANGLE, MAXIMUM_ANGLE_EXTENT);
        return new LayoutData(jComponent.getWidth(), jComponent.getHeight(), radiusData, angularData);
    }

    @Override
    public boolean isSelected() {
        //scenario cannot be selected
        return false;
    }

    @Override
    public void setSelected(boolean selected) {
        //does nothing, scenario cannot be selected
    }
}
