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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.perfcake.ide.editor.colors.NamedColor;
import org.perfcake.ide.editor.swing.icons.components.GeneratorIcon;
import org.perfcake.ide.editor.view.Pair;

/**
 * Represents view of a generator.
 *
 * @author Jakub Knetl
 */
public class GeneratorView extends SimpleSectorView {

    private String runType;
    private String runValue;
    private String threads;

    /**
     * creates new sector view.
     */
    public GeneratorView() {
        super(new GeneratorIcon(ICON_SIDE, ICON_SIDE));
    }

    public String getRunType() {
        return runType;
    }

    public void setRunType(String runType) {
        this.runType = runType;
    }

    public String getRunValue() {
        return runValue;
    }

    public void setRunValue(String runValue) {
        this.runValue = runValue;
    }

    public String getThreads() {
        return threads;
    }

    public void setThreads(String threads) {
        this.threads = threads;
    }

    @Override
    protected List<Pair> getAdditionalData() {
        List<Pair> list = new ArrayList<>();

        list.add(new Pair(runType, runValue));
        list.add(new Pair("threads", threads));

        return list;
    }

    @Override
    protected Color getIconColor() {
        return colorScheme.getColor(NamedColor.COMPONENT_GENERATOR);
    }

    @Override
    protected void initManagementIcons() {
         // no icons
    }
}
