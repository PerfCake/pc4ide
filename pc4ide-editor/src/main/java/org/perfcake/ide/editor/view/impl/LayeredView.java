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

package org.perfcake.ide.editor.view.impl;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.view.AbstractView;
import org.perfcake.ide.editor.view.View;

/**
 * LayeredView is composite view. It contains multiple views.
 *
 * @author Jakub Knetl
 */
public class LayeredView extends AbstractView {
    private List<View> innerViews;
    private List<View> outerViews;

    private Class<? extends View> innerViewsType;
    private Class<? extends View> outerViewsType;

    /**
     * Creates new layered view.
     */
    public LayeredView() {
        innerViews = new ArrayList<>();
        outerViews = new ArrayList<>();
    }

    @Override
    public void draw(Graphics2D g2d) {

        for (View v : getChildren()) {
            v.draw(g2d);
        }
    }

    @Override
    public Shape getViewBounds() {
        Area bounds = new Area();

        for (View v : getChildren()) {
            bounds.add(new Area(v.getViewBounds()));
        }

        return bounds;
    }

    @Override
    public LayoutData getMinimumSize(LayoutData constraint, Graphics2D g2d) {
        return layoutManager.getMinimumSize(constraint, g2d);
    }

    @Override
    public List<View> getChildren() {
        List<View> children = new ArrayList<>();
        children.addAll(innerViews);
        children.addAll(outerViews);

        return children;
    }

    @Override
    public void addChild(View view) {
        if (view == null) {
            throw new IllegalArgumentException("view cannot ben null.");
        }

        if (innerViewsType.isAssignableFrom(view.getClass())) {
            view.setParent(this);
            innerViews.add(view);
            layoutManager.add(this);
        } else if (outerViewsType.isAssignableFrom(view.getClass())) {
            view.setParent(this);
            outerViews.add(view);
            layoutManager.add(this);
        } else {
            throw new IllegalArgumentException(String.format("View expected of either type '%s' or '%s' but was '%s'",
                    innerViewsType.getCanonicalName(), outerViewsType.getCanonicalName(), view.getClass().getCanonicalName()));
        }
    }

    @Override
    public boolean removeChild(View view) {
        boolean removed = false;

        if (view == null) {
            return removed;
        }

        if (innerViewsType.isAssignableFrom(view.getClass())) {
            removed = innerViews.remove(view);
        } else if (outerViewsType.isAssignableFrom(view.getClass())) {
            removed = outerViews.remove(view);
        }
        return removed;
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public void setSelected(boolean selected) {
        // do nothing
    }

    @Override
    protected void initManagementIcons() {
        // no icons required
    }
}
