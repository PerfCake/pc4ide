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

import org.perfcake.ide.editor.view.ComponentView;

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
     * Sets {@link LayoutData} constraint for the layoutManager. This indicates what part of drawing surface
     * may be used by this LayoutManager. If some dimension of constraint is zero then it means there is
     * no constraint on that dimension.
     *
     * @param constraint constraint to be set
     */
    void setConstraint(LayoutData constraint);

    /**
     * Adds a graphical component to the layout so that this layout manager can manage the component.
     *
     * @param component component to be managed by this layout manager
     */
    void add(ComponentView component);

    /**
     * Removes a graphical component from the layout so that this layout manager won't manager component
     * anymore.
     *
     * @param component component to be removed from this layout manager
     * @return true if the component was removed, or false if component couldn't be found.
     */
    boolean remove(ComponentView component);

    /**
     * @return <b>Unmodifiable List</b> of all managed component views.
     */
    List<ComponentView> getChildren();

}
