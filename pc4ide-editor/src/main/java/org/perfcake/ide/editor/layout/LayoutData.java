/**
 *
 */
package org.perfcake.ide.editor.layout;

import java.awt.geom.Point2D;

/**
 * This class holds information about the plane (canvas, component, drawing surface) which can be used for editor.
 *
 * @author jknetl
 *
 */
public class LayoutData {

	private double width;
	private double height;
	private RadiusData radiusData = new RadiusData();
	private AngularData angularData = new AngularData();

	public LayoutData() {
	}

	public LayoutData(double width, double height, RadiusData data, AngularData angularData) {
		super();
		this.width = width;
		this.height = height;
		this.radiusData = data;
		this.angularData = angularData;
	}

	public LayoutData(LayoutData other) {
		this.width = other.width;
		this.height = other.height;
		this.radiusData = new RadiusData(other.radiusData);
		this.angularData = new AngularData(other.angularData);
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public RadiusData getRadiusData() {
		return radiusData;
	}

	public void setRadiusData(RadiusData data) {
		this.radiusData = data;
	}

	public AngularData getAngularData() {
		return angularData;
	}

	public void setAngularData(AngularData angularData) {
		this.angularData = angularData;
	}

	public Point2D getCenter() {
		return new Point2D.Double(width / 2, height / 2);
	}

}
