/**
 *
 */
package org.perfcake.ide.editor.view.impl;

import org.perfcake.ide.editor.view.ComponentView;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
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
public class SectorView  implements ComponentView {

	private String componentName;
	private Point2D center;
	private int outerRadius;
	private int innerRadius;
	private double startAngle;
	private double angleExtent;

	private Shape bounds;

	public SectorView(String componentName, Point2D center, int outerRadius, int innerRadius, double startAngle, double angleExtent) {
		this.componentName = componentName;
		this.center = center;
		this.outerRadius = outerRadius;
		this.innerRadius = innerRadius;
		this.startAngle = startAngle;
		this.angleExtent = angleExtent;
	}


	/* (non-Javadoc)
	 * @see org.perfcake.ide.editor.view.ComponentView#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		//antialiasing
		Map<Object, Object> hints = new HashMap<>();
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.addRenderingHints(hints);


		Arc2D outerArc = new Arc2D.Double();
		outerArc.setArcByCenter(center.getX(), center.getY(), outerRadius, startAngle, angleExtent, Arc2D.PIE);

		//angular overlap needed since we need to make sure that innerArc overlaps completely over innerArc
		//if we use same angles than it could happen that innerArc is little bit less because of double conversion
		double angularOverlap = 5;
		Arc2D innerArc = new Arc2D.Double();
		innerArc.setArcByCenter(center.getX(), center.getY(), innerRadius, startAngle - angularOverlap, angleExtent + 2*angularOverlap, Arc2D.PIE);


		Point2D startOuterArcPoint = new Point2D.Double(center.getX() + outerRadius * Math.cos(Math.toRadians(-startAngle)),
				center.getY() + outerRadius * Math.sin(Math.toRadians(-startAngle)));

		Point2D endOuterArcPoint = new Point2D.Double(center.getX() + outerRadius * Math.cos(Math.toRadians(-(startAngle + angleExtent))),
				center.getY() + outerRadius * Math.sin(Math.toRadians(-(startAngle+angleExtent))));

//		Point2D startInnerArcPoint = new Point2D.Double(center.getX() + innerRadius * Math.cos(Math.toRadians(-startAngle)),
//				center.getY() + innerRadius * Math.sin(Math.toRadians(-startAngle)));
//
//		Point2D endInnerArcPoint = new Point2D.Double(center.getX() + innerRadius * Math.cos(Math.toRadians(-(startAngle + angleExtent))),
//				center.getY() + innerRadius * Math.sin(Math.toRadians(-(startAngle+angleExtent))));
//
//		Line2D line1 = new Line2D.Double(center.getX() + innerRadius * Math.cos(Math.toRadians(-startAngle)),
//				center.getY() + innerRadius * Math.sin(Math.toRadians(-startAngle)),
//				center.getX() + outerRadius * Math.cos(Math.toRadians(-startAngle)),
//				center.getY() + outerRadius * Math.sin(Math.toRadians(-startAngle)));
//
//		Line2D line2 = new Line2D.Double(center.getX() + innerRadius * Math.cos(Math.toRadians(-(startAngle + angleExtent))),
//				center.getY() + innerRadius * Math.sin(Math.toRadians(-(startAngle + angleExtent))),
//				center.getX() + outerRadius * Math.cos(Math.toRadians(-(startAngle + angleExtent))),
//				center.getY() + outerRadius * Math.sin(Math.toRadians(-(startAngle + angleExtent))));
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

		Area boundArea = new Area(outerArc);
		boundArea.subtract(new Area(innerArc));

		bounds = boundArea;

		Point2D chordCenter = new Point2D.Double(
				(startOuterArcPoint.getX() + endOuterArcPoint.getX())/2,
				(startOuterArcPoint.getY() + endOuterArcPoint.getY())/2);



		AffineTransform defaultTransform = g2d.getTransform();
		FontRenderContext frc = g2d.getFontRenderContext();
		Font font = g2d.getFont();
		Rectangle2D fontBounds = font.getStringBounds(componentName, frc);

		Double theta = 90 - (startAngle + angleExtent/2);

		g2d.rotate(Math.toRadians(theta), chordCenter.getX(), chordCenter.getY());
		g2d.drawString(componentName, (float) (chordCenter.getX() - fontBounds.getWidth()/2) , (float) (chordCenter.getY() - fontBounds.getHeight()/2));
		g2d.setTransform(defaultTransform);



		g2d.draw(boundArea);

//		drawSectionName(g2d, endOuterArcPoint);


	}

//
//	private void drawSectionName(Graphics2D g2d, Point2D startingPoint) {
//		//font along the line:
//		Font font = new Font("Serif", Font.PLAIN, 12);
//		FontRenderContext frc = g2d.getFontRenderContext();
//		AffineTransform defaultTransform = g2d.getTransform();
//		g2d.translate(startingPoint.getX(), startingPoint.getY());
//		g2d.rotate(Math.toRadians(-(startAngle - 55)));
//
//		GlyphVector gv = font.createGlyphVector(frc, componentName);
//		int length = gv.getNumGlyphs();
//		for (int i = 0; i < length; i++) {
//			Point2D p = gv.getGlyphPosition(i);
//			double theta = (double) i/ (double) (length ) * Math.toRadians(angleExtent);
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

}