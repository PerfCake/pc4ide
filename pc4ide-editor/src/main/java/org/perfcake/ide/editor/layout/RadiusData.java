package org.perfcake.ide.editor.layout;

public class RadiusData {
	private double innerRadius;
	private double outerRadius;

	public RadiusData() {
	}

	public RadiusData(double innerRadius, double outerRadius) {
		super();
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;
	}

	public RadiusData(RadiusData data) {
		this.innerRadius = data.getInnerRadius();
		this.outerRadius = data.getOuterRadius();
	}

	public double getInnerRadius() {
		return innerRadius;
	}

	public void setInnerRadius(double innerRadius) {
		this.innerRadius = innerRadius;
	}

	public double getOuterRadius() {
		return outerRadius;
	}

	public void setOuterRadius(double outerRadius) {
		this.outerRadius = outerRadius;
	}
}