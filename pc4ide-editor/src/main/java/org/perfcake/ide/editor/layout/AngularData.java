package org.perfcake.ide.editor.layout;

public class AngularData {
	private double startAngle;
	private double angleExtent;

	public AngularData() {
	}

	public AngularData(double startAngle, double angularExtent) {
		super();
		this.startAngle = startAngle;
		this.angleExtent = angularExtent;
	}

	public AngularData(AngularData data) {
		this.startAngle = data.startAngle;
		this.angleExtent = data.getAngleExtent();
	}

	public double getStartAngle() {
		return startAngle;
	}

	public void setStartAngle(double startAngle) {
		this.startAngle = startAngle;
	}

	public double getAngleExtent() {
		return angleExtent;
	}

	public void setAngleExtent(double angleExtent) {
		this.angleExtent = angleExtent;
	}

	public void addAngleExtent(double addExtent) {
		this.angleExtent += addExtent;
	}
}