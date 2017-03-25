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

import java.awt.Graphics2D;
import java.util.List;

import org.perfcake.ide.editor.view.View;

/**
 * Layout manger computes {@link LayoutData} for children components.
 *
 * @author jknetl
 */
public interface LayoutManager {

    /**
     * Sets layout data to all children views. It may or may not take into consideration
     * the preferred layout data of the children views.
     * @param g2d graphics context
     */
    void layout(Graphics2D g2d);

    /**
     * Computes minimum size reqeusted by this layout manager. If some dimension is zero, then there is no minimum
     * size requested.
     *
     * @param constraint constraint on some dimensions of layout data.
     * @param g2d graphics context
     * @return LayoutData which contains information about minimum size in particular dimensions.
     */
    LayoutData getMinimumSize(LayoutData constraint, Graphics2D g2d);

    /**
     * Sets {@link LayoutData} constraint for the layoutManager. This indicates what part of drawing surface
     * may be used by this LayoutManager. If some dimension of constraint is zero then it means there is
     * no constraint on that dimension.
     *
     * @param constraint constraint to be set
     */
    void setConstraint(LayoutData constraint);

    /**
     * Adds a graphical inspector to the layout so that this layout manager can manage the inspector.
     *
     * @param component inspector to be managed by this layout manager
     */
    void add(View component);

    /**
     * Removes a graphical inspector from the layout so that this layout manager won't manager inspector
     * anymore.
     *
     * @param component inspector to be removed from this layout manager
     * @return true if the inspector was removed, or false if inspector couldn't be found.
     */
    boolean remove(View component);

    /**
     * @return <b>Unmodifiable ListIcon</b> of all managed inspector views.
     */
    List<View> getChildren();

}
