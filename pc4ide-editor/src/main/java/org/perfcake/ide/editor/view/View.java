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

package org.perfcake.ide.editor.view;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.List;
import org.perfcake.ide.editor.layout.LayoutData;

/**
 * Base type for a view in editor MVC.
 *
 * @author jknetl
 */
public interface View {

    /**
     * @return true if the view is currently selected.
     */
    boolean isSelected();

    /**
     * toggle the view selection.
     *
     * @param selected set as selected?
     */
    void setSelected(boolean selected);

    /**
     * draw view on the surface.
     *
     * @param g2d Graphics context
     */
    void draw(Graphics2D g2d);

    /**
     * @return Shape which completely encloses this view graphical representation.
     */
    Shape getViewBounds();

    /**
     * Computes a minimum size of the view. The constraint argument is used as a constraint for the size. So if
     * some dimension of constraint argument is N, then returned value in that dimension cannot be larger than N.
     * If some dimension of constraint argument is zero then there is no constraint on that dimension
     *
     * @param constraint constraint
     * @param g2d        Graphics context
     * @return Minimum size of the inspector according to given constraints.
     */
    LayoutData getMinimumSize(LayoutData constraint, Graphics2D g2d);

    /**
     * @return Actual layoutData of the view.
     */
    LayoutData getLayoutData();

    /**
     * Sets layout which will be used for the inspector on {@link #draw(Graphics2D)} method.
     *
     * @param data layout data to be set
     */
    void setLayoutData(LayoutData data);

    /**
     * @return <b>unmodifiable list of</b> views that acts as a child of current view (they are inside of the view).
     */
    List<View> getChildren();

    /**
     * @return view which is parent of the view. Root view will return null.
     */
    View getParent();

    /**
     * Sets a parent of this view.
     *
     * @param parent parent view
     */
    void setParent(View parent);

    /**
     * Adds child view.
     *
     * @param view view to add
     */
    void addChild(View view);

    /**
     * @return true if the view is valid (up to date).
     */
    boolean isValid();

    /**
     * Invalidates view to indicate that it needs to be redrawn.
     */
    void invalidate();

    /**
     * Validates the view and the view of the children. It means that it sets view sizes and positions so that consequent draw operation
     * will draw it on proper place with proper size.
     *
     * @param g2d Graphics context
     */
    void validate(Graphics2D g2d);


    /**
     * Remove child view.
     *
     * @param view child view to be removed
     * @return true if the view was removed or false if the view is not children of this view.
     */
    boolean removeChild(View view);

}
