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
public class DestinationIcon implements ResizableIcon {

    /** The width of this icon. */
    private int width;

    /** The height of this icon. */
    private int height;

    /** The rendered image. */
    private BufferedImage image;

    /**
     * Creates a new transcoded SVG image.
     */
    public DestinationIcon() {
        this(40, 25);
    }

    /**
     * Creates a new transcoded SVG image.
     */
    public DestinationIcon(int width, int height) {
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
            final double coef = Math.min((double) width / (double) 40, (double) height / (double) 25);

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
        g.transform(new AffineTransform(0.037795275f, 0, 0, 0.037795275f, -481.96536f, -436.08188f));

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
        ((GeneralPath) shape).moveTo(13233.0, 12164.0);
        ((GeneralPath) shape).curveTo(13165.0, 12164.0, 13160.0, 11561.0, 13233.0, 11561.0);
        ((GeneralPath) shape).curveTo(13382.0, 11546.0, 13777.0, 11631.0, 13776.0, 11861.0);
        ((GeneralPath) shape).curveTo(13774.0, 12083.0, 13390.0, 12174.0, 13233.0, 12164.0);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0x9C81F5));
        g.setStroke(new BasicStroke(42, 0, 0, 4));
        g.draw(shape);

        // _0_0_0_0_0_1

        // _0_0_0_0_0_1_0

        // _0_0_0_0_0_1_0_0

        // _0_0_0_0_0_1_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13091.0, 11597.0);
        ((GeneralPath) shape).lineTo(12752.0, 11597.0);

        g.draw(shape);

        // _0_0_0_0_0_2

        // _0_0_0_0_0_2_0

        // _0_0_0_0_0_2_0_0

        // _0_0_0_0_0_2_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13091.0, 11729.0);
        ((GeneralPath) shape).lineTo(12752.0, 11729.0);

        g.draw(shape);

        // _0_0_0_0_0_3

        // _0_0_0_0_0_3_0

        // _0_0_0_0_0_3_0_0

        // _0_0_0_0_0_3_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13091.0, 11861.0);
        ((GeneralPath) shape).lineTo(12752.0, 11861.0);

        g.draw(shape);

        // _0_0_0_0_0_4

        // _0_0_0_0_0_4_0

        // _0_0_0_0_0_4_0_0

        // _0_0_0_0_0_4_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13091.0, 11993.0);
        ((GeneralPath) shape).lineTo(12752.0, 11993.0);

        g.draw(shape);

        // _0_0_0_0_0_5

        // _0_0_0_0_0_5_0

        // _0_0_0_0_0_5_0_0

        // _0_0_0_0_0_5_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(13091.0, 12125.0);
        ((GeneralPath) shape).lineTo(12752.0, 12125.0);

        g.draw(shape);

        g.setTransform(transformations.pop()); // _0

    }
}

