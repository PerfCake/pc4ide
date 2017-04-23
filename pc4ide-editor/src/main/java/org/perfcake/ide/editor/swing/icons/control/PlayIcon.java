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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import org.perfcake.ide.editor.actions.ActionType;

/**
 * This class has been automatically generated using
 * <a href="http://ebourg.github.io/flamingo-svg-transcoder/">Flamingo SVG transcoder</a>.
 */
public class PlayIcon extends AbstractControlIcon {

    public static final int DEFAULT_WIDTH = 6;
    public static final int DEFAULT_HEIGHT = 6;

    /**
     * Creates a new transcoded SVG image.
     *
     * @param color color
     */
    public PlayIcon(Color color) {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, color);
    }

    /**
     * Creates a new transcoded SVG image.
     *
     * @param width  width of the icon
     * @param height heigth of the icon
     * @param color  color
     */
    public PlayIcon(int width, int height, Color color) {
        super(width, height, color, ActionType.RUN);
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
        // store location
        this.x = x;
        this.y = y;

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
        transformations.push(g.getTransform());
        g.transform(new AffineTransform(1, 0, 0, 1, 1, 1));

        // _0_0
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(0.0, 0.0);
        ((GeneralPath) shape).lineTo(0.0, 6.0);
        ((GeneralPath) shape).lineTo(6.0, 3.0);
        ((GeneralPath) shape).lineTo(0.0, 0.0);
        ((GeneralPath) shape).closePath();

        g.setPaint(color);
        g.fill(shape);
        g.setTransform(transformations.pop()); // _0_0
    }
}

