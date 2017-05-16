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

package org.perfcake.ide.editor.awt.geom;

import java.awt.geom.Dimension2D;

/**
 * This class implements {@link Dimension2D} with double precision.
 *
 * @author Jakub Knetl
 */
public class DimensionDouble extends Dimension2D {

    private double width;
    private double height;

    /**
     * Creates new dimension with zero widht and height.
     */
    public DimensionDouble() {
        this(0, 0);
    }

    /**
     * Creates new dimension with given width and height.
     *
     * @param width  width
     * @param height height
     */
    public DimensionDouble(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setSize(double width, double height) {

    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
