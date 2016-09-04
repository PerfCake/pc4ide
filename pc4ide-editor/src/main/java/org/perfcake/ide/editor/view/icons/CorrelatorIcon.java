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
public class CorrelatorIcon implements ResizableIcon {

    /** The width of this icon. */
    private int width;

    /** The height of this icon. */
    private int height;

    /** The rendered image. */
    private BufferedImage image;

    /**
     * Creates a new transcoded SVG image.
     */
    public CorrelatorIcon() {
        this(41, 21);
    }

    /**
     * Creates a new transcoded SVG image.
     */
    public CorrelatorIcon(int width, int height) {
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
    private static void paint(Graphics2D g) {
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

        g.setPaint(new Color(0x47C30C));
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

