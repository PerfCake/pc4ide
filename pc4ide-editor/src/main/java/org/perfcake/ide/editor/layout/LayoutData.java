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
/**
 *
 */

package org.perfcake.ide.editor.layout;

import java.awt.geom.Point2D;

/**
 * This class holds information about the plane (canvas, inspector, drawing surface) which can be used for editor.
 *
 * @author jknetl
 */
public class LayoutData {

    private double width;
    private double height;
    private RadiusData radiusData = new RadiusData();
    private AngularData angularData = new AngularData();

    // if set, explicit center defines alternative center of the screen. If explicitCenter is null, then center is computed
    // from width and height.
    private Point2D explicitCenter;

    public LayoutData() {
    }

    /**
     * Creates new layout data by specifying dimensions.
     *
     * @param width widht
     * @param height height
     * @param data radius data
     * @param angularData angular data
     */
    public LayoutData(double width, double height, RadiusData data, AngularData angularData) {
        super();
        this.width = width;
        this.height = height;
        this.radiusData = data;
        this.angularData = angularData;
    }

    /**
     * Creates new layout data by deep copying another data object.
     *
     * @param other another data object to be copied
     */
    public LayoutData(LayoutData other) {
        this.width = other.width;
        this.height = other.height;
        this.radiusData = new RadiusData(other.radiusData);
        this.angularData = new AngularData(other.angularData);
        this.explicitCenter = other.getExplicitCenter();
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

    /**
     * Center of the layout data. If explicit center is set, then this call returns value in explicit center. Otherwise, center of layout
     * data is computed dynamically.
     *
     * @return center
     */
    public Point2D getCenter() {
        if (explicitCenter != null) {
            return explicitCenter;
        } else {
            return new Point2D.Double(width / 2, height / 2);
        }
    }

    public Point2D getExplicitCenter() {
        return explicitCenter;
    }

    public void setExplicitCenter(Point2D explicitCenter) {
        this.explicitCenter = explicitCenter;
    }
}
