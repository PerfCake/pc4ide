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

package org.perfcake.ide.editor.swing.icons.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import org.perfcake.ide.editor.swing.icons.AbstractIcon;

/**
 * This class has been automatically generated using
 * <a href="http://ebourg.github.io/flamingo-svg-transcoder/">Flamingo SVG transcoder</a>.
 */
public class CorrelatorIcon extends AbstractIcon {

    public static final Color DEFAULT_COLOR = new Color(0x47C30C);

    /**
     * Creates a new transcoded SVG image.
     */
    public CorrelatorIcon() {
        this(DEFAULT_COLOR);
    }

    /**
     * Creates correlator icon with given color.
     *
     * @param color color of the icon.
     */
    public CorrelatorIcon(Color color) {
        this(41, 21, color);
    }

    /**
     * Creates a new transcoded SVG image.
     *
     * @param width  width of icon
     * @param height height of icon
     */
    public CorrelatorIcon(int width, int height) {
        super(width, height, DEFAULT_COLOR);
    }

    /**
     * Creates a new transcoded SVG image.
     *
     * @param width  width of icon
     * @param height height of icon
     * @param color  color of an icon
     */
    public CorrelatorIcon(int width, int height, Color color) {
        super(width, height, color);
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (isRenderNeeded()) {
            image = new BufferedImage(getIconWidth(), getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            final double coef = Math.min((double) width / (double) 41, (double) height / (double) 21);

            final Graphics2D g2d = image.createGraphics();
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

        final float origAlpha = 1.0f;

        final java.util.LinkedList<AffineTransform> transformations = new java.util.LinkedList<AffineTransform>();


        //
        transformations.push(g.getTransform());
        g.transform(new AffineTransform(0.037795275f, 0, 0, 0.037795275f, -478.14804f, -227.86772f));

        // _0

        // _0_0

        // _0_0_0

        // _0_0_0_0

        // _0_0_0_0_0

        // _0_0_0_0_0_0

        // _0_0_0_0_0_0_0

        // _0_0_0_0_0_0_0_0

        // _0_0_0_0_0_0_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(12672.0, 6305.0);
        ((GeneralPath) shape).lineTo(12927.0, 6050.0);
        ((GeneralPath) shape).lineTo(12927.0, 6178.0);
        ((GeneralPath) shape).lineTo(13137.0, 6178.0);
        ((GeneralPath) shape).lineTo(13137.0, 6302.0);

        g.setPaint(color);
        g.setStroke(new BasicStroke(42, 0, 1, 4));
        g.draw(shape);

        // _0_0_0_0_0_1

        // _0_0_0_0_0_1_0

        // _0_0_0_0_0_1_0_0

        // _0_0_0_0_0_1_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(12672.0, 6304.0);
        ((GeneralPath) shape).lineTo(12927.0, 6559.0);
        ((GeneralPath) shape).lineTo(12927.0, 6432.0);
        ((GeneralPath) shape).lineTo(13137.0, 6432.0);
        ((GeneralPath) shape).lineTo(13137.0, 6300.0);

        g.draw(shape);

        // _0_0_0_0_0_2

        // _0_0_0_0_0_2_0

        // _0_0_0_0_0_2_0_0

        // _0_0_0_0_0_2_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13701.0, 6305.0);
        ((GeneralPath) shape).lineTo(13446.0, 6050.0);
        ((GeneralPath) shape).lineTo(13446.0, 6178.0);
        ((GeneralPath) shape).lineTo(13236.0, 6178.0);
        ((GeneralPath) shape).lineTo(13236.0, 6302.0);

        g.draw(shape);

        // _0_0_0_0_0_3

        // _0_0_0_0_0_3_0

        // _0_0_0_0_0_3_0_0

        // _0_0_0_0_0_3_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13701.0, 6304.0);
        ((GeneralPath) shape).lineTo(13446.0, 6559.0);
        ((GeneralPath) shape).lineTo(13446.0, 6432.0);
        ((GeneralPath) shape).lineTo(13236.0, 6432.0);
        ((GeneralPath) shape).lineTo(13236.0, 6300.0);

        g.draw(shape);

        g.setTransform(transformations.pop()); // _0

    }
}

