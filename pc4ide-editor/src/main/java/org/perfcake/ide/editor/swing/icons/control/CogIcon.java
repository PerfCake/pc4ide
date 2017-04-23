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

package org.perfcake.ide.editor.swing.icons.control;

import static java.awt.Color.BLACK;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

/**
 * This class has been automatically generated using
 * <a href="http://ebourg.github.io/flamingo-svg-transcoder/">Flamingo SVG transcoder</a>.
 */
public class CogIcon extends AbstractControlIcon {

    public static final int DEFAULT_WIDTH = 8;
    private static final int DEFAULT_HEIGHT = 8;

    /**
     * Creates a new transcoded SVG image.
     */
    public CogIcon() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, BLACK);
    }

    /**
     * Creates a new transcoded SVG image.
     *
     * @param width  width of icon
     * @param height height of icon
     */
    public CogIcon(int width, int height) {
        this(width, height, BLACK);
    }

    /**
     * Creates new configure icon.
     *
     * @param width  width
     * @param height height
     * @param color  color
     */
    public CogIcon(int width, int height, Color color) {
        super(width, height, color);
    }

    @Override
    public int getIconHeight() {
        return height;
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (isRenderNeeded()) {
            image = new BufferedImage(getIconWidth(), getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            double coef = Math.min((double) width / (double) DEFAULT_WIDTH, (double) height / (double) DEFAULT_HEIGHT);

            Graphics2D g2d = image.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.scale(coef, coef);
            paint(g2d);
            g2d.dispose();
        }

        g.drawImage(image, x, y, null);
    }

    /**
     * Paints the transcoded SVG image on the specified graphics context.
     *
     * @param g Graphics context.
     */
    private void paint(Graphics2D g) {
        Shape shape = null;

        float origAlpha = 1.0f;

        java.util.LinkedList<AffineTransform> transformations = new java.util.LinkedList<AffineTransform>();


        // 

        // _0

        // _0_0
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(3.5, 0.0);
        ((GeneralPath) shape).lineTo(3.0, 1.19);
        ((GeneralPath) shape).curveTo(2.9, 1.22, 2.81, 1.2700001, 2.72, 1.32);
        ((GeneralPath) shape).lineTo(1.53, 0.82000005);
        ((GeneralPath) shape).lineTo(0.80999994, 1.5400001);
        ((GeneralPath) shape).lineTo(1.31, 2.73);
        ((GeneralPath) shape).curveTo(1.26, 2.83, 1.2199999, 2.91, 1.18, 3.01);
        ((GeneralPath) shape).lineTo(-0.01000011, 3.51);
        ((GeneralPath) shape).lineTo(-0.01000011, 4.51);
        ((GeneralPath) shape).lineTo(1.18, 5.01);
        ((GeneralPath) shape).curveTo(1.2199999, 5.11, 1.26, 5.19, 1.31, 5.2900004);
        ((GeneralPath) shape).lineTo(0.80999994, 6.4800005);
        ((GeneralPath) shape).lineTo(1.53, 7.200001);
        ((GeneralPath) shape).lineTo(2.72, 6.700001);
        ((GeneralPath) shape).curveTo(2.81, 6.7400007, 2.9, 6.790001, 3.0, 6.830001);
        ((GeneralPath) shape).lineTo(3.5, 8.02);
        ((GeneralPath) shape).lineTo(4.5, 8.02);
        ((GeneralPath) shape).lineTo(5.0, 6.8300004);
        ((GeneralPath) shape).curveTo(5.09, 6.7900004, 5.19, 6.7500005, 5.28, 6.7000003);
        ((GeneralPath) shape).lineTo(6.4700003, 7.2000003);
        ((GeneralPath) shape).lineTo(7.1900005, 6.4800005);
        ((GeneralPath) shape).lineTo(6.6900005, 5.2900004);
        ((GeneralPath) shape).curveTo(6.7300005, 5.2000003, 6.7800007, 5.1000004, 6.8200006, 5.01);
        ((GeneralPath) shape).lineTo(8.01, 4.51);
        ((GeneralPath) shape).lineTo(8.01, 3.5100002);
        ((GeneralPath) shape).lineTo(6.82, 3.0100002);
        ((GeneralPath) shape).curveTo(6.79, 2.9200003, 6.7400002, 2.8200002, 6.69, 2.7300003);
        ((GeneralPath) shape).lineTo(7.19, 1.5400002);
        ((GeneralPath) shape).lineTo(6.4700003, 0.8200002);
        ((GeneralPath) shape).lineTo(5.28, 1.3200002);
        ((GeneralPath) shape).curveTo(5.19, 1.2800002, 5.09, 1.2300001, 5.0, 1.1900002);
        ((GeneralPath) shape).lineTo(4.5, 1.1920929E-7);
        ((GeneralPath) shape).lineTo(3.5, 1.1920929E-7);
        ((GeneralPath) shape).closePath();
        ((GeneralPath) shape).moveTo(4.0, 2.5);
        ((GeneralPath) shape).curveTo(4.83, 2.5, 5.5, 3.17, 5.5, 4.0);
        ((GeneralPath) shape).curveTo(5.5, 4.83, 4.83, 5.5, 4.0, 5.5);
        ((GeneralPath) shape).curveTo(3.17, 5.5, 2.5, 4.83, 2.5, 4.0);
        ((GeneralPath) shape).curveTo(2.5, 3.17, 3.17, 2.5, 4.0, 2.5);
        ((GeneralPath) shape).closePath();

        g.setPaint(color);
        g.fill(shape);

    }
}

