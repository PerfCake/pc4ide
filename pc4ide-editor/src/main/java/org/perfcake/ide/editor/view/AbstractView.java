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

package org.perfcake.ide.editor.view;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.LayoutManager;

/**
 * {@link AbstractView} implements some of the methods of {@link ComponentView} interface.
 *
 * @author jknetl
 */
public abstract class AbstractView implements ComponentView {

    private boolean isSelected = false;
    private List<ComponentView> children = new ArrayList<>();
    private ComponentView parent;
    protected boolean isValid;

    protected LayoutData layoutData;
    protected LayoutManager layoutManager;

    /**
     * Creates new abstract view.
     * @param parent parent view
     */
    public AbstractView(ComponentView parent) {
        super();
        this.parent = parent;
        isValid = false;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        if (this.isSelected != selected) {
            this.isSelected = selected;
            invalidate();
        }
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void invalidate() {
        isValid = false;
        if (parent != null) {
            // indicate to parent that this controller needs validation
            parent.invalidate();
        }
    }

    @Override
    public void validate(Graphics2D g2d) {
        if (layoutManager != null) {
            layoutManager.layout(g2d);
        }
        for (final ComponentView view : children) {
            view.validate(g2d);
        }
        isValid = true;
    }

    @Override
    public LayoutData getLayoutData() {
        return layoutData;
    }

    @Override
    public void setLayoutData(LayoutData layoutData) {
        this.layoutData = layoutData;
    }

    @Override
    public List<ComponentView> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public ComponentView getParent() {
        return parent;
    }

    @Override
    public void setParent(ComponentView parent) {
        this.parent = parent;
    }

    @Override
    public void addChild(ComponentView view) {
        children.add(view);
        if (layoutManager != null) {
            layoutManager.add(view);
        }
    }

    @Override
    public boolean removeChild(ComponentView view) {
        if (layoutManager != null) {
            layoutManager.remove(view);
        }
        return children.remove(view);
    }
}
