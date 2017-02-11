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

package org.perfcake.ide.editor.view.icons;

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

/**
 * This class has been automatically generated using
 * <a href="http://ebourg.github.io/flamingo-svg-transcoder/">Flamingo SVG transcoder</a>.
 */
public class MessageIcon implements ResizableIcon {

    /**
     * The width of this icon.
     */
    private int width;

    /**
     * The height of this icon.
     */
    private int height;

    /**
     * The rendered image.
     */
    private BufferedImage image;

    /**
     * Creates a new transcoded SVG image.
     */
    public MessageIcon() {
        this(41, 23);
    }

    /**
     * Creates a new transcoded SVG image.
     * @param width width of the icon
     * @param height heigth of the icon
     */
    public MessageIcon(int width, int height) {
        this.width = width;
        this.height = height;
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
    public void setIconWidth(int width) {
        this.width = width;
    }

    @Override
    public void setIconHeight(int height) {
        this.height = height;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (image == null) {
            image = new BufferedImage(getIconWidth(), getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            final double coef = Math.min((double) width / (double) 41, (double) height / (double) 23);

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
    private static void paint(Graphics2D g) {
        Shape shape = null;

        final float origAlpha = 1.0f;

        final java.util.LinkedList<AffineTransform> transformations = new java.util.LinkedList<AffineTransform>();


        //
        transformations.push(g.getTransform());
        g.transform(new AffineTransform(0.03779528f, 0, 0, 0.03779528f, -482.19217f, -482.41895f));

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
        ((GeneralPath) shape).moveTo(12779.0, 12785.0);
        ((GeneralPath) shape).lineTo(12779.0, 13350.0);
        ((GeneralPath) shape).curveTo(12779.0, 13350.0, 12922.0, 13138.0, 13204.0, 13079.0);
        ((GeneralPath) shape).curveTo(13265.0, 13079.0, 13767.0, 13080.0, 13767.0, 13080.0);
        ((GeneralPath) shape).curveTo(13794.0, 13097.0, 13796.0, 13153.0, 13796.0, 13153.0);
        ((GeneralPath) shape).lineTo(13796.0, 12983.0);
        ((GeneralPath) shape).curveTo(13796.0, 12983.0, 13794.0, 13040.0, 13766.0, 13056.0);
        ((GeneralPath) shape).curveTo(13766.0, 13056.0, 13259.0, 13055.0, 13204.0, 13055.0);
        ((GeneralPath) shape).curveTo(12921.0, 13027.0, 12779.0, 12785.0, 12779.0, 12785.0);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0x6AD299));
        g.fill(shape);

        // _0_0_0_0_0_0_0_2
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(12779.0, 12785.0);
        ((GeneralPath) shape).lineTo(12779.0, 13350.0);
        ((GeneralPath) shape).curveTo(12779.0, 13350.0, 12922.0, 13138.0, 13204.0, 13079.0);
        ((GeneralPath) shape).curveTo(13265.0, 13079.0, 13767.0, 13080.0, 13767.0, 13080.0);
        ((GeneralPath) shape).curveTo(13794.0, 13097.0, 13796.0, 13153.0, 13796.0, 13153.0);
        ((GeneralPath) shape).lineTo(13796.0, 12983.0);
        ((GeneralPath) shape).curveTo(13796.0, 12983.0, 13794.0, 13040.0, 13766.0, 13056.0);
        ((GeneralPath) shape).curveTo(13766.0, 13056.0, 13259.0, 13055.0, 13204.0, 13055.0);
        ((GeneralPath) shape).curveTo(12921.0, 13027.0, 12779.0, 12785.0, 12779.0, 12785.0);
        ((GeneralPath) shape).closePath();

        g.setStroke(new BasicStroke(42, 0, 1, 4));
        g.draw(shape);

        // _0_0_0_0_0_1

        // _0_0_0_0_0_1_0

        // _0_0_0_0_0_1_0_0

        // _0_0_0_0_0_1_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13676.0, 13171.0);
        ((GeneralPath) shape).curveTo(13676.0, 13190.0, 13665.0, 13205.0, 13640.0, 13221.0);
        ((GeneralPath) shape).curveTo(13616.0, 13237.0, 13585.0, 13248.0, 13542.0, 13257.0);
        ((GeneralPath) shape).curveTo(13499.0, 13266.0, 13457.0, 13271.0, 13408.0, 13271.0);
        ((GeneralPath) shape).curveTo(13358.0, 13271.0, 13316.0, 13266.0, 13273.0, 13257.0);
        ((GeneralPath) shape).curveTo(13230.0, 13248.0, 13200.0, 13237.0, 13175.0, 13221.0);
        ((GeneralPath) shape).curveTo(13150.0, 13205.0, 13139.0, 13190.0, 13139.0, 13171.0);
        ((GeneralPath) shape).curveTo(13139.0, 13153.0, 13150.0, 13137.0, 13175.0, 13122.0);
        ((GeneralPath) shape).curveTo(13200.0, 13106.0, 13230.0, 13094.0, 13273.0, 13085.0);
        ((GeneralPath) shape).curveTo(13316.0, 13076.0, 13358.0, 13072.0, 13408.0, 13072.0);
        ((GeneralPath) shape).curveTo(13457.0, 13072.0, 13499.0, 13076.0, 13542.0, 13085.0);
        ((GeneralPath) shape).curveTo(13585.0, 13094.0, 13616.0, 13106.0, 13640.0, 13122.0);
        ((GeneralPath) shape).curveTo(13665.0, 13137.0, 13676.0, 13153.0, 13676.0, 13171.0);
        ((GeneralPath) shape).closePath();

        g.draw(shape);

        g.setTransform(transformations.pop()); // _0

    }
}

