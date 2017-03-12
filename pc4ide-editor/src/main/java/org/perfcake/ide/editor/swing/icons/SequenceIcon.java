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
public class SequenceIcon extends ComponentIcon {

    public static final Color DEFAULT_COLOR = new Color(0xFF8054);

    /**
     * The rendered image.
     */
    private BufferedImage image;

    /**
     * Creates a new transcoded SVG image.
     */
    public SequenceIcon() {
        this(DEFAULT_COLOR);
    }

    /**
     * Creates new sequence icon.
     *
     * @param color color of the icon.
     */
    public SequenceIcon(Color color) {
        this(50, 20, color);
    }

    /**
     * Creates a new transcoded SVG image.
     *
     * @param width  width of the icon
     * @param height heigth of the icon
     * @param color  Color of the icon
     */
    public SequenceIcon(int width, int height, Color color) {
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
            final double coef = Math.min((double) width / (double) 50, (double) height / (double) 20);

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
        g.transform(new AffineTransform(0.03779528f, 0, 0, 0.03779528f, -487.37012f, -274.12915f));

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
        ((GeneralPath) shape).moveTo(13617.0, 7475.0);
        ((GeneralPath) shape).curveTo(13581.0, 7449.0, 13574.0, 7443.0, 13554.0, 7404.0);
        ((GeneralPath) shape).curveTo(13468.0, 7403.0, 13137.0, 7404.0, 13137.0, 7404.0);
        ((GeneralPath) shape).lineTo(13137.0, 7737.0);
        ((GeneralPath) shape).lineTo(13680.0, 7737.0);
        ((GeneralPath) shape).curveTo(13680.0, 7737.0, 13776.0, 7546.0, 14014.0, 7402.0);
        ((GeneralPath) shape).lineTo(14155.0, 7332.0);
        ((GeneralPath) shape).curveTo(14155.0, 7332.0, 14033.0, 7375.0, 13991.0, 7387.0);
        ((GeneralPath) shape).curveTo(13963.0, 7398.0, 13810.0, 7457.0, 13777.0, 7471.0);
        ((GeneralPath) shape).curveTo(13741.0, 7490.0, 13650.0, 7489.0, 13617.0, 7475.0);
        ((GeneralPath) shape).closePath();

        g.setPaint(color);
        g.setStroke(new BasicStroke(42, 0, 0, 4));
        g.draw(shape);

        // _0_0_0_0_0_1

        // _0_0_0_0_0_1_0

        // _0_0_0_0_0_1_0_0

        // _0_0_0_0_0_1_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13118.0, 7417.0);
        ((GeneralPath) shape).curveTo(13113.0, 7429.0, 13105.0, 7437.0, 13092.0, 7443.0);
        ((GeneralPath) shape).curveTo(13079.0, 7450.0, 13065.0, 7452.0, 13047.0, 7452.0);
        ((GeneralPath) shape).curveTo(13028.0, 7452.0, 13012.0, 7449.0, 12993.0, 7442.0);
        ((GeneralPath) shape).curveTo(12975.0, 7435.0, 12961.0, 7426.0, 12947.0, 7415.0);
        ((GeneralPath) shape).curveTo(12934.0, 7403.0, 12925.0, 7392.0, 12920.0, 7379.0);
        ((GeneralPath) shape).curveTo(12915.0, 7365.0, 12915.0, 7354.0, 12919.0, 7343.0);
        ((GeneralPath) shape).curveTo(12924.0, 7331.0, 12932.0, 7323.0, 12945.0, 7317.0);
        ((GeneralPath) shape).curveTo(12958.0, 7311.0, 12973.0, 7308.0, 12991.0, 7308.0);
        ((GeneralPath) shape).curveTo(13009.0, 7308.0, 13026.0, 7312.0, 13044.0, 7318.0);
        ((GeneralPath) shape).curveTo(13062.0, 7325.0, 13077.0, 7334.0, 13090.0, 7345.0);
        ((GeneralPath) shape).curveTo(13104.0, 7357.0, 13112.0, 7368.0, 13117.0, 7382.0);
        ((GeneralPath) shape).curveTo(13123.0, 7395.0, 13123.0, 7406.0, 13118.0, 7417.0);
        ((GeneralPath) shape).closePath();

        g.draw(shape);

        // _0_0_0_0_0_2

        // _0_0_0_0_0_2_0

        // _0_0_0_0_0_2_0_0

        // _0_0_0_0_0_2_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13253.0, 7274.0);
        ((GeneralPath) shape).lineTo(13433.0, 7274.0);

        g.draw(shape);

        // _0_0_0_0_0_3

        // _0_0_0_0_0_3_0

        // _0_0_0_0_0_3_0_0

        // _0_0_0_0_0_3_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13343.0, 7277.0);
        ((GeneralPath) shape).lineTo(13343.0, 7396.0);

        g.draw(shape);

        // _0_0_0_0_0_4

        // _0_0_0_0_0_4_0

        // _0_0_0_0_0_4_0_0

        // _0_0_0_0_0_4_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(14159.0, 7480.0);
        ((GeneralPath) shape).lineTo(14125.0, 7563.0);

        g.draw(shape);

        // _0_0_0_0_0_5

        // _0_0_0_0_0_5_0

        // _0_0_0_0_0_5_0_0

        // _0_0_0_0_0_5_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(14184.0, 7573.0);
        ((GeneralPath) shape).curveTo(14184.0, 7578.0, 14182.0, 7582.0, 14180.0, 7586.0);
        ((GeneralPath) shape).curveTo(14178.0, 7590.0, 14175.0, 7593.0, 14171.0, 7595.0);
        ((GeneralPath) shape).curveTo(14167.0, 7597.0, 14163.0, 7599.0, 14158.0, 7599.0);
        ((GeneralPath) shape).curveTo(14153.0, 7599.0, 14149.0, 7597.0, 14145.0, 7595.0);
        ((GeneralPath) shape).curveTo(14141.0, 7593.0, 14138.0, 7590.0, 14135.0, 7586.0);
        ((GeneralPath) shape).curveTo(14133.0, 7582.0, 14132.0, 7578.0, 14132.0, 7573.0);
        ((GeneralPath) shape).curveTo(14132.0, 7568.0, 14133.0, 7564.0, 14135.0, 7560.0);
        ((GeneralPath) shape).curveTo(14138.0, 7556.0, 14141.0, 7553.0, 14145.0, 7550.0);
        ((GeneralPath) shape).curveTo(14149.0, 7548.0, 14153.0, 7547.0, 14158.0, 7547.0);
        ((GeneralPath) shape).curveTo(14163.0, 7547.0, 14167.0, 7548.0, 14171.0, 7550.0);
        ((GeneralPath) shape).curveTo(14175.0, 7553.0, 14178.0, 7556.0, 14180.0, 7560.0);
        ((GeneralPath) shape).curveTo(14182.0, 7564.0, 14184.0, 7568.0, 14184.0, 7573.0);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0xDD4814));
        g.fill(shape);

        // _0_0_0_0_0_5_0_2
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(14184.0, 7573.0);
        ((GeneralPath) shape).curveTo(14184.0, 7578.0, 14182.0, 7582.0, 14180.0, 7586.0);
        ((GeneralPath) shape).curveTo(14178.0, 7590.0, 14175.0, 7593.0, 14171.0, 7595.0);
        ((GeneralPath) shape).curveTo(14167.0, 7597.0, 14163.0, 7599.0, 14158.0, 7599.0);
        ((GeneralPath) shape).curveTo(14153.0, 7599.0, 14149.0, 7597.0, 14145.0, 7595.0);
        ((GeneralPath) shape).curveTo(14141.0, 7593.0, 14138.0, 7590.0, 14135.0, 7586.0);
        ((GeneralPath) shape).curveTo(14133.0, 7582.0, 14132.0, 7578.0, 14132.0, 7573.0);
        ((GeneralPath) shape).curveTo(14132.0, 7568.0, 14133.0, 7564.0, 14135.0, 7560.0);
        ((GeneralPath) shape).curveTo(14138.0, 7556.0, 14141.0, 7553.0, 14145.0, 7550.0);
        ((GeneralPath) shape).curveTo(14149.0, 7548.0, 14153.0, 7547.0, 14158.0, 7547.0);
        ((GeneralPath) shape).curveTo(14163.0, 7547.0, 14167.0, 7548.0, 14171.0, 7550.0);
        ((GeneralPath) shape).curveTo(14175.0, 7553.0, 14178.0, 7556.0, 14180.0, 7560.0);
        ((GeneralPath) shape).curveTo(14182.0, 7564.0, 14184.0, 7568.0, 14184.0, 7573.0);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0xFF8054));
        g.setStroke(new BasicStroke(59, 0, 0, 4));
        g.draw(shape);

        // _0_0_0_0_0_6

        // _0_0_0_0_0_6_0

        // _0_0_0_0_0_6_0_0

        // _0_0_0_0_0_6_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(14157.0, 7480.0);
        ((GeneralPath) shape).lineTo(14190.0, 7563.0);

        g.setStroke(new BasicStroke(42, 0, 0, 4));
        g.draw(shape);

        g.setTransform(transformations.pop()); // _0

    }
}

