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
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.perfcake.ide.editor.actions.ActionType;
import org.perfcake.ide.editor.colors.ColorScheme;
import org.perfcake.ide.editor.colors.DefaultColorScheme;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.LayoutManager;
import org.perfcake.ide.editor.swing.icons.ControlIcon;
import org.perfcake.ide.editor.utils.FontUtils;

/**
 * {@link AbstractView} implements some of the methods of {@link View} interface.
 *
 * @author jknetl
 */
public abstract class AbstractView implements View {

    /**
     * size of a side of a component icon.
     */
    public static final int ICON_SIDE = 32;

    /**
     * size of a side of a small component icon.
     */
    public static final int SMALL_ICON_SIDE = 16;

    protected List<ControlIcon> managementIcons;
    private boolean isSelected = false;
    protected List<View> children = new ArrayList<>();
    protected View parent;
    protected boolean isValid;

    protected LayoutData layoutData;
    protected LayoutManager layoutManager;

    protected ColorScheme colorScheme;

    /**
     * Creates new abstract view.
     */
    public AbstractView() {
        super();
        isValid = false;
        colorScheme = new DefaultColorScheme();
        managementIcons = new ArrayList<>();
        initManagementIcons();
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
        boolean valid = isValid;

        Iterator<View> it = getChildren().iterator();

        while (valid == true && it.hasNext()) {
            View child = it.next();
            valid = valid && child.isValid();
        }

        return valid;
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
        for (final View view : getChildren()) {
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
        if (layoutManager != null) {
            layoutManager.setConstraint(layoutData);
        }
        this.layoutData = layoutData;
    }

    @Override
    public List<View> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public View getParent() {
        return parent;
    }

    @Override
    public void setParent(View parent) {
        this.parent = parent;
    }

    @Override
    public void addChild(View view) {
        if (view != null) {
            children.add(view);
            view.setParent(this);
            if (layoutManager != null) {
                layoutManager.add(view);
            }
        }
    }

    @Override
    public boolean removeChild(View view) {
        if (layoutManager != null) {
            layoutManager.remove(view);
        }
        boolean removed = children.remove(view);
        if (removed) {
            view.setParent(null);
        }

        return removed;
    }

    @Override
    public ColorScheme getColorScheme() {
        return colorScheme;
    }

    @Override
    public void setColorScheme(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
    }

    @Override
    public ActionType getAction(Point2D location) {
        ActionType action = ActionType.NONE;

        // try to handle action by this view
        if (location != null && getViewBounds() != null || getViewBounds().contains(location)) {
            action = ActionType.SELECT;

            for (ControlIcon controlIcon : managementIcons) {
                Shape iconBounds = controlIcon.getBounds();
                if (iconBounds != null && iconBounds.contains(location) && controlIcon.getAction() != null) {
                    action = controlIcon.getAction();
                }
            }
        }

        // if there is no action try to ask children for action
        if (action == ActionType.NONE) {
            for (View child : getChildren()) {
                action = child.getAction(location);
            }
        }

        return action;
    }

    /**
     * Adds rendering hints to a graphics context.
     *
     * @param g2d graphics context
     */
    protected void addRenderingHints(Graphics2D g2d) {
        final Map<Object, Object> hints = FontUtils.getRenderingHints();
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.addRenderingHints(hints);
    }

    /**
     * This method is intended to add management icons.
     */
    protected abstract void initManagementIcons();

    @Override
    public String getToolTip(Point2D location) {
        Shape toolTipBounds = getToolTipBounds();
        if (toolTipBounds != null && toolTipBounds.contains(location)) {
            return getToolTipText();
        }

        return null;
    }

    protected Shape getToolTipBounds() {
        return getViewBounds();
    }

    /**
     * Tooltip text for this component.
     *
     * @return tooltip text
     */
    protected abstract String getToolTipText();
}
