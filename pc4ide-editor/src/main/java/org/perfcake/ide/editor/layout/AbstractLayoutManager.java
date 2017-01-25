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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.perfcake.ide.editor.view.ComponentView;

/**
 * Created by jknetl on 10/15/16.
 */
public abstract class AbstractLayoutManager implements LayoutManager {
    protected LayoutData constraints;
    protected List<ComponentView> children;

    public AbstractLayoutManager() {
        children = new ArrayList<>();
    }

    @Override
    public void setConstraint(LayoutData constraint) {
        this.constraints = constraint;
    }

    @Override
    public void add(ComponentView component) {
        children.add(component);
    }

    @Override
    public boolean remove(ComponentView component) {
        return children.remove(component);
    }

    @Override
    public List<ComponentView> getChildren() {
        return Collections.unmodifiableList(children);
    }
}
