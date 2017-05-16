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
 * Radius data represents a radius of editor. Editor consists of inner radius and outer radius. Content of an editor
 * is placed between inner radius and outer radius. Outer radius must be larger than inner radius.
 */
public class RadiusData {
    private double innerRadius;
    private double outerRadius;

    public RadiusData() {
    }

    /**
     * Creates new radius data.
     * @param innerRadius inner radius
     * @param outerRadius outer radius
     */
    public RadiusData(double innerRadius, double outerRadius) {
        super();
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
    }

    /**
     * Creates new radius data by deep copying another radius data instance.
     * @param data another radius data instance to be copied
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RadiusData that = (RadiusData) o;
        return Double.compare(that.innerRadius, innerRadius) == 0
                && Double.compare(that.outerRadius, outerRadius) == 0;
    }

    @Override
    public String toString() {
        return "RadiusData{"
                + "innerRadius=" + innerRadius
                + ", outerRadius=" + outerRadius
                + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(innerRadius, outerRadius);
    }
}