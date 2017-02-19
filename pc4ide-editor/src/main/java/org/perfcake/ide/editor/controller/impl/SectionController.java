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

package org.perfcake.ide.editor.controller.impl;

import org.perfcake.ide.core.model.AbstractModel;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.view.ComponentView;
import org.perfcake.ide.editor.view.icons.ResizableIcon;
import org.perfcake.ide.editor.view.impl.SectorView;

/**
 * Controls one section of a inspector.
 *
 * @author jknetl
 */
public class SectionController extends AbstractController {

    private ComponentView view;

    /**
     * Creates new section controller.
     * @param sectionName Name of the section to be controlled
     * @param icon Icon of a section
     * @param model model class
     */
    public SectionController(String sectionName, ResizableIcon icon, Model model) {
        super(model);
        final ComponentView parentView = (getParent() == null) ? null : getParent().getView();
        view = new SectorView(parentView, sectionName, icon);
    }

    @Override
    public ComponentView getView() {
        return view;
    }

}
