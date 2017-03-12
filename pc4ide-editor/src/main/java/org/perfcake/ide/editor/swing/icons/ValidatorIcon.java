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

package org.perfcake.ide.editor.swing.icons;

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
public class ValidatorIcon extends ComponentIcon {

    public static final Color DEFAULT_COLOR = new Color(0x7BDDD9);

    /**
     * The rendered image.
     */
    private BufferedImage image;

    /**
     * Creates a new transcoded SVG image.
     */
    public ValidatorIcon() {
        this(DEFAULT_COLOR);
    }

    /**
     * Creates new validator icon.
     *
     * @param color color of the icon
     */
    public ValidatorIcon(Color color) {
        this(40, 35, color);
    }

    /**
     * Creates a new transcoded SVG image.
     *
     * @param width  width of the icon
     * @param height height of the icon
     * @param color  color of the icon
     */
    public ValidatorIcon(int width, int height, Color color) {
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
            final double coef = Math.min((double) width / (double) 40, (double) height / (double) 35);

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
        g.transform(new AffineTransform(0.037795275f, 0, 0, 0.037795275f, -481.32285f, -313.43622f));

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
        ((GeneralPath) shape).moveTo(12756.0, 8952.0);
        ((GeneralPath) shape).curveTo(12826.0, 8912.0, 12836.0, 8913.0, 12905.0, 8949.0);
        ((GeneralPath) shape).curveTo(12973.0, 8984.0, 12985.0, 8985.0, 13053.0, 8949.0);

        g.setPaint(color);
        g.setStroke(new BasicStroke(42, 0, 0, 4));
        g.draw(shape);

        // _0_0_0_0_0_1

        // _0_0_0_0_0_1_0

        // _0_0_0_0_0_1_0_0

        // _0_0_0_0_0_1_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13768.0, 8971.0);
        ((GeneralPath) shape).curveTo(13699.0, 8931.0, 13688.0, 8932.0, 13619.0, 8968.0);
        ((GeneralPath) shape).curveTo(13551.0, 9003.0, 13539.0, 9004.0, 13471.0, 8968.0);

        g.draw(shape);

        // _0_0_0_0_0_2

        // _0_0_0_0_0_2_0

        // _0_0_0_0_0_2_0_0

        // _0_0_0_0_0_2_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(12745.0, 9155.0);
        ((GeneralPath) shape).curveTo(12815.0, 9115.0, 12825.0, 9116.0, 12894.0, 9152.0);
        ((GeneralPath) shape).curveTo(12962.0, 9187.0, 12974.0, 9188.0, 13042.0, 9152.0);

        g.draw(shape);

        // _0_0_0_0_0_3

        // _0_0_0_0_0_3_0

        // _0_0_0_0_0_3_0_0

        // _0_0_0_0_0_3_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13036.0, 9155.0);
        ((GeneralPath) shape).curveTo(13106.0, 9115.0, 13116.0, 9116.0, 13185.0, 9152.0);
        ((GeneralPath) shape).curveTo(13253.0, 9187.0, 13265.0, 9188.0, 13333.0, 9152.0);

        g.draw(shape);

        // _0_0_0_0_0_4

        // _0_0_0_0_0_4_0

        // _0_0_0_0_0_4_0_0

        // _0_0_0_0_0_4_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13324.0, 9155.0);
        ((GeneralPath) shape).curveTo(13394.0, 9115.0, 13404.0, 9116.0, 13473.0, 9152.0);
        ((GeneralPath) shape).curveTo(13541.0, 9187.0, 13553.0, 9188.0, 13621.0, 9152.0);

        g.draw(shape);

        // _0_0_0_0_0_5

        // _0_0_0_0_0_5_0

        // _0_0_0_0_0_5_0_0

        // _0_0_0_0_0_5_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13770.0, 9155.0);
        ((GeneralPath) shape).curveTo(13701.0, 9115.0, 13690.0, 9116.0, 13621.0, 9152.0);
        ((GeneralPath) shape).curveTo(13553.0, 9187.0, 13541.0, 9188.0, 13473.0, 9152.0);

        g.draw(shape);

        // _0_0_0_0_0_6

        // _0_0_0_0_0_6_0

        // _0_0_0_0_0_6_0_0

        // _0_0_0_0_0_6_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13264.0, 8965.0);
        ((GeneralPath) shape).lineTo(13264.0, 8293.0);

        g.setStroke(new BasicStroke(56, 0, 0, 4));
        g.draw(shape);

        // _0_0_0_0_0_7

        // _0_0_0_0_0_7_0

        // _0_0_0_0_0_7_0_0

        // _0_0_0_0_0_7_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13528.0, 8413.0);
        ((GeneralPath) shape).lineTo(13282.0, 8413.0);

        g.setStroke(new BasicStroke(42, 0, 0, 4));
        g.draw(shape);

        // _0_0_0_0_0_8

        // _0_0_0_0_0_8_0

        // _0_0_0_0_0_8_0_0

        // _0_0_0_0_0_8_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13528.0, 8413.0);
        ((GeneralPath) shape).lineTo(13282.0, 8413.0);

        g.draw(shape);

        // _0_0_0_0_0_9

        // _0_0_0_0_0_9_0

        // _0_0_0_0_0_9_0_0

        // _0_0_0_0_0_9_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13528.0, 8578.0);
        ((GeneralPath) shape).lineTo(13282.0, 8578.0);

        g.draw(shape);

        // _0_0_0_0_0_10

        // _0_0_0_0_0_10_0

        // _0_0_0_0_0_10_0_0

        // _0_0_0_0_0_10_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13528.0, 8744.0);
        ((GeneralPath) shape).lineTo(13282.0, 8744.0);

        g.draw(shape);

        // _0_0_0_0_0_11

        // _0_0_0_0_0_11_0

        // _0_0_0_0_0_11_0_0

        // _0_0_0_0_0_11_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13326.0, 8950.0);
        ((GeneralPath) shape).curveTo(13326.0, 8961.0, 13323.0, 8970.0, 13318.0, 8980.0);
        ((GeneralPath) shape).curveTo(13312.0, 8990.0, 13305.0, 8997.0, 13295.0, 9003.0);
        ((GeneralPath) shape).curveTo(13285.0, 9008.0, 13276.0, 9011.0, 13265.0, 9011.0);
        ((GeneralPath) shape).curveTo(13253.0, 9011.0, 13244.0, 9008.0, 13234.0, 9003.0);
        ((GeneralPath) shape).curveTo(13224.0, 8997.0, 13217.0, 8990.0, 13211.0, 8980.0);
        ((GeneralPath) shape).curveTo(13206.0, 8970.0, 13203.0, 8961.0, 13203.0, 8950.0);
        ((GeneralPath) shape).curveTo(13203.0, 8938.0, 13206.0, 8929.0, 13211.0, 8919.0);
        ((GeneralPath) shape).curveTo(13217.0, 8909.0, 13224.0, 8902.0, 13234.0, 8896.0);
        ((GeneralPath) shape).curveTo(13244.0, 8891.0, 13253.0, 8888.0, 13265.0, 8888.0);
        ((GeneralPath) shape).curveTo(13276.0, 8888.0, 13285.0, 8891.0, 13295.0, 8896.0);
        ((GeneralPath) shape).curveTo(13305.0, 8902.0, 13312.0, 8909.0, 13318.0, 8919.0);
        ((GeneralPath) shape).curveTo(13323.0, 8929.0, 13326.0, 8938.0, 13326.0, 8950.0);
        ((GeneralPath) shape).closePath();

        g.fill(shape);

        // _0_0_0_0_0_11_0_2
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13326.0, 8950.0);
        ((GeneralPath) shape).curveTo(13326.0, 8961.0, 13323.0, 8970.0, 13318.0, 8980.0);
        ((GeneralPath) shape).curveTo(13312.0, 8990.0, 13305.0, 8997.0, 13295.0, 9003.0);
        ((GeneralPath) shape).curveTo(13285.0, 9008.0, 13276.0, 9011.0, 13265.0, 9011.0);
        ((GeneralPath) shape).curveTo(13253.0, 9011.0, 13244.0, 9008.0, 13234.0, 9003.0);
        ((GeneralPath) shape).curveTo(13224.0, 8997.0, 13217.0, 8990.0, 13211.0, 8980.0);
        ((GeneralPath) shape).curveTo(13206.0, 8970.0, 13203.0, 8961.0, 13203.0, 8950.0);
        ((GeneralPath) shape).curveTo(13203.0, 8938.0, 13206.0, 8929.0, 13211.0, 8919.0);
        ((GeneralPath) shape).curveTo(13217.0, 8909.0, 13224.0, 8902.0, 13234.0, 8896.0);
        ((GeneralPath) shape).curveTo(13244.0, 8891.0, 13253.0, 8888.0, 13265.0, 8888.0);
        ((GeneralPath) shape).curveTo(13276.0, 8888.0, 13285.0, 8891.0, 13295.0, 8896.0);
        ((GeneralPath) shape).curveTo(13305.0, 8902.0, 13312.0, 8909.0, 13318.0, 8919.0);
        ((GeneralPath) shape).curveTo(13323.0, 8929.0, 13326.0, 8938.0, 13326.0, 8950.0);
        ((GeneralPath) shape).closePath();

        g.draw(shape);

        g.setTransform(transformations.pop()); // _0

    }
}

