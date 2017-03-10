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

import java.util.ArrayList;
import java.util.List;
import org.perfcake.ide.editor.swing.icons.SenderIcon;
import org.perfcake.ide.editor.view.Pair;

/**
 * Represents view of a sender.
 *
 * @author Jakub Knetl
 */
public class SenderView extends SimpleSectorView {

    private String target;

    /**
     * creates new sector view.
     */
    public SenderView() {
        super(new SenderIcon());
    }

    @Override
    protected List<Pair> getAdditionalData() {
        ArrayList<Pair> list = new ArrayList<>(1);
        list.add(new Pair("target", target));

        return list;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
