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

package org.perfcake.ide.editor.layout;

import java.util.Objects;

/**
 * Represents angular data. It contains an angle extent, and start angle
 */
public class AngularData {
    private double startAngle;
    private double angleExtent;

    public AngularData() {
    }

    /**
     * Creates new angular data based on start angle and angularExtent.
     * @param startAngle where the angle starts
     * @param angularExtent how large is the angle
     */
    public AngularData(double startAngle, double angularExtent) {
        super();
        this.startAngle = startAngle;
        this.angleExtent = angularExtent;
    }

    /**
     * Creates new angular data by deep copying another angular data instance.
     *
     * @param data another angular data to be copied
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AngularData that = (AngularData) o;
        return Double.compare(that.startAngle, startAngle) == 0
                && Double.compare(that.angleExtent, angleExtent) == 0;
    }

    @Override
    public String toString() {
        return "AngularData{"
                + "startAngle=" + startAngle
                + ", angleExtent=" + angleExtent
                + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(startAngle, angleExtent);
    }
}