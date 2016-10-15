/**
 *
 */
package org.perfcake.ide.editor.view.impl;

import org.perfcake.ide.editor.layout.AngularData;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.RadiusData;
import org.perfcake.ide.editor.view.AbstractView;
import org.perfcake.ide.editor.view.ComponentView;
import org.perfcake.ide.editor.view.icons.ResizableIcon;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics;
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

/**
 * @author jknetl
 *
 */
public class SectorView extends AbstractView {

	public String getComponentName() {
		return componentName;
	}

	private String componentName;
	private ResizableIcon icon;

	private Shape bounds;


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
		outerArc.setArcByCenter(layoutData.getCenter().getX(), layoutData.getCenter().getY(), layoutData.getRadiusData().getOuterRadius(), layoutData.getAngularData().getStartAngle(), layoutData.getAngularData().getAngleExtent(),
				Arc2D.PIE);

		//angular overlap needed since we need to make sure that innerArc overlaps completely over innerArc
		//if we use same angles than it could happen that innerArc is little bit less because of double conversion
		final double angularOverlap = 5;
		final Arc2D innerArc = new Arc2D.Double();
		innerArc.setArcByCenter(layoutData.getCenter().getX(), layoutData.getCenter().getY(), layoutData.getRadiusData().getInnerRadius(), layoutData.getAngularData().getStartAngle() - angularOverlap,
				layoutData.getAngularData().getAngleExtent() + 2 * angularOverlap, Arc2D.PIE);

		//				layoutData.getCenter().getY() + layoutData.getRadiusData().getInnerRadius() * Math.sin(Math.toRadians(-layoutData.getAngularData().getStartAngle())));
		//
		//		Point2D endInnerArcPoint = new Point2D.Double(layoutData.getCenter().getX() + layoutData.getRadiusData().getInnerRadius() * Math.cos(Math.toRadians(-(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent()))),
		//				layoutData.getCenter().getY() + layoutData.getRadiusData().getInnerRadius() * Math.sin(Math.toRadians(-(layoutData.getAngularData().getStartAngle()+layoutData.getAngularData().getAngleExtent()))));
		//
		//		Line2D line1 = new Line2D.Double(layoutData.getCenter().getX() + layoutData.getRadiusData().getInnerRadius() * Math.cos(Math.toRadians(-layoutData.getAngularData().getStartAngle())),
		//				layoutData.getCenter().getY() + layoutData.getRadiusData().getInnerRadius() * Math.sin(Math.toRadians(-layoutData.getAngularData().getStartAngle())),
		//				layoutData.getCenter().getX() + layoutData.getRadiusData().getOuterRadius() * Math.cos(Math.toRadians(-layoutData.getAngularData().getStartAngle())),
		//				layoutData.getCenter().getY() + layoutData.getRadiusData().getOuterRadius() * Math.sin(Math.toRadians(-layoutData.getAngularData().getStartAngle())));
		//
		//		Line2D line2 = new Line2D.Double(layoutData.getCenter().getX() + layoutData.getRadiusData().getInnerRadius() * Math.cos(Math.toRadians(-(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent()))),
		//				layoutData.getCenter().getY() + layoutData.getRadiusData().getInnerRadius() * Math.sin(Math.toRadians(-(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent()))),
		//				layoutData.getCenter().getX() + layoutData.getRadiusData().getOuterRadius() * Math.cos(Math.toRadians(-(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent()))),
		//				layoutData.getCenter().getY() + layoutData.getRadiusData().getOuterRadius() * Math.sin(Math.toRadians(-(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent()))));
		//
		//		Line2D line1 = new Line2D.Double(startOuterArcPoint, startInnerArcPoint);
		//		Line2D line2 = new Line2D.Double(endOuterArcPoint, endInnerArcPoint);
		//
		//		g2d.draw(outerArc);
		//		g2d.draw(innerArc);
		//		g2d.draw(line1);
		//		g2d.draw(line2);
		//
		// create Areas of the shapes to combine them
		//		Area outerArcArea = new Area(outerArc);
		//		Area innerArcArea = new Area(innerArc);
		//		Area line1Area = new Area(line1);
		//		Area line2Area = new Area(line2);

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
			final double iconX = (layoutData.getCenter().getX() - icon.getIconWidth() / 2)
					+ (1.5 * layoutData.getRadiusData().getInnerRadius() + icon.getIconWidth() / 2) * Math.cos(Math.toRadians(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent() / 2));
			final double iconY = (layoutData.getCenter().getY() - icon.getIconHeight() / 2)
					- (1.5 * layoutData.getRadiusData().getInnerRadius() + icon.getIconHeight() / 2) * Math.sin(Math.toRadians(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent() / 2));

			// we may pass null as component since our icon implementation completely ignores this argument
			icon.paintIcon(null, g2d, (int) iconX, (int) iconY);
		}
	}

	protected void drawSectorName(Graphics2D g2d) {
		final Point2D startOuterArcPoint = new Point2D.Double(layoutData.getCenter().getX() + layoutData.getRadiusData().getOuterRadius() * Math.cos(Math.toRadians(-layoutData.getAngularData().getStartAngle())),
				layoutData.getCenter().getY() + layoutData.getRadiusData().getOuterRadius() * Math.sin(Math.toRadians(-layoutData.getAngularData().getStartAngle())));

		final Point2D endOuterArcPoint = new Point2D.Double(
				layoutData.getCenter().getX() + layoutData.getRadiusData().getOuterRadius() * Math.cos(Math.toRadians(-(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent()))),
				layoutData.getCenter().getY() + layoutData.getRadiusData().getOuterRadius() * Math.sin(Math.toRadians(-(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent()))));

		final Point2D chordCenter = new Point2D.Double(
				(startOuterArcPoint.getX() + endOuterArcPoint.getX()) / 2,
				(startOuterArcPoint.getY() + endOuterArcPoint.getY()) / 2);

		final AffineTransform defaultTransform = g2d.getTransform();
		final FontRenderContext frc = g2d.getFontRenderContext();
		final Font font = g2d.getFont();
		final Rectangle2D fontBounds = font.getStringBounds(componentName, frc);

		final Point2D textCenter = new Point2D.Double(
				chordCenter.getX() - fontBounds.getHeight() * Math.cos(Math.toRadians(-(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent() / 2))),
				chordCenter.getY() - fontBounds.getHeight() * Math.sin(Math.toRadians(-(layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent() / 2))));

		final Double theta = 90 - (layoutData.getAngularData().getStartAngle() + layoutData.getAngularData().getAngleExtent() / 2);

		g2d.rotate(Math.toRadians(theta), textCenter.getX(), textCenter.getY());
		g2d.drawString(componentName, (float) (textCenter.getX() - fontBounds.getWidth() / 2), (float) (textCenter.getY() - fontBounds.getHeight() / 2));
		g2d.setTransform(defaultTransform);
	}

	//
	//	private void drawSectionName(Graphics2D g2d, Point2D startingPoint) {
	//		//font along the line:
	//		Font font = new Font("Serif", Font.PLAIN, 12);
	//		FontRenderContext frc = g2d.getFontRenderContext();
	//		AffineTransform defaultTransform = g2d.getTransform();
	//		g2d.translate(startingPoint.getX(), startingPoint.getY());
	//		g2d.rotate(Math.toRadians(-(layoutData.getAngularData().getStartAngle() - 55)));
	//
	//		GlyphVector gv = font.createGlyphVector(frc, componentName);
	//		int length = gv.getNumGlyphs();
	//		for (int i = 0; i < length; i++) {
	//			Point2D p = gv.getGlyphPosition(i);
	//			double theta = (double) i/ (double) (length ) * Math.toRadians(layoutData.getAngularData().getAngleExtent());
	//			AffineTransform at = AffineTransform.getTranslateInstance(p.getX(), p.getY());
	//			at.rotate(theta);
	//			Shape glyph = gv.getGlyphOutline(i);
	//			Shape transformedGlyph = at.createTransformedShape(glyph);
	//			g2d.fill(transformedGlyph);
	//		}
	//
	//		g2d.setTransform(defaultTransform);
	//	}

	@Override
	public Shape getViewBounds() {
		return bounds;
	}

	@Override
	public LayoutData getMinimumSize(LayoutData constraint, Graphics2D g2d) {
//		TODO: Compute size from the content (+ child)
		LayoutData minimumSize = new LayoutData();
		minimumSize.setAngularData(new AngularData(0,30));
		return minimumSize;
	}
}
