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

package org.perfcake.ide.editor.view.impl;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import javax.swing.JComponent;

import org.perfcake.ide.editor.layout.AngularData;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.RadiusData;
import org.perfcake.ide.editor.layout.impl.PerfCakeEditorLayoutManager;
import org.perfcake.ide.editor.view.AbstractView;
import org.perfcake.ide.editor.view.ComponentView;

/**
 * Represents view of the whole editor.
 *
 * @author jknetl
 */
public class EditorView extends AbstractView {

    private static final int MAXIMUM_ANGLE_EXTENT = 340;
    private static final int MAXIMUM_INNER_RADIUS = 150;

    private JComponent jComponent;

    /**
     * Creates new editor view inside of swing container.
     * @param jComponent swing container
     */
    public EditorView(JComponent jComponent) {
        super(null);
        this.jComponent = jComponent;
        layoutManager = new PerfCakeEditorLayoutManager();
        layoutManager.setConstraint(getConstraints());
    }

    /* (non-Javadoc)
     * @see org.perfcake.ide.editor.view.ComponentView#draw(java.awt.Graphics)
     */
    @Override
    public void draw(Graphics2D g2d) {
        for (ComponentView child : getChildren()) {
            child.draw(g2d);
        }
    }

    /* (non-Javadoc)
     * @see org.perfcake.ide.editor.view.ComponentView#getViewBounds()
     */
    @Override
    public Shape getViewBounds() {
        Area area = new Area();
        for (ComponentView v : getChildren()) {
            Area childArea = new Area(v.getViewBounds());
            area.add(childArea);
        }

        return area;
    }

    @Override
    public LayoutData getMinimumSize(LayoutData constraint, Graphics2D g2d) {
        LayoutData minimumSize = new LayoutData(constraint);
        double angularExtent = 0.0;
        for (ComponentView child : getChildren()) {
            angularExtent += child.getMinimumSize(constraint, g2d).getAngularData().getAngleExtent();
        }
        minimumSize.getAngularData().setAngleExtent(angularExtent);
        return minimumSize;
    }

    /**
     * Invalidates the view. Since this is a root view, it also triggers
     * a validation process.
     */
    public void invalidate() {
        //EditorView is a root so it may trigger validation
        Graphics2D g2d = (Graphics2D) jComponent.getGraphics();
        validate(g2d);
    }

    @Override
    public void validate(Graphics2D g2d) {
        // get drawing surface constraints
        final LayoutData data = getConstraints();
        layoutManager.setConstraint(data);

        super.validate(g2d);

        jComponent.repaint();
    }

    private LayoutData getConstraints() {
        final double outerRadius = (0.9 * Math.min(jComponent.getWidth(), jComponent.getHeight())) / 2;
        final double innerRadius = Math.min(MAXIMUM_INNER_RADIUS, (0.2 * Math.min(jComponent.getWidth(), jComponent.getHeight())) / 2);
        final RadiusData radiusData = new RadiusData(innerRadius, outerRadius);
        final AngularData angularData = new AngularData(0, MAXIMUM_ANGLE_EXTENT);
        return new LayoutData(jComponent.getWidth(), jComponent.getHeight(), radiusData, angularData);
    }
}
