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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.perfcake.ide.editor.colors.NamedColor;
import org.perfcake.ide.editor.swing.icons.components.MessageIcon;
import org.perfcake.ide.editor.swing.icons.control.MinusIcon;
import org.perfcake.ide.editor.swing.icons.control.PlusIcon;
import org.perfcake.ide.editor.view.Pair;

/**
 * Represents a message view.
 *
 * @author Jakub Knetl
 */
public class MessageView extends SimpleSectorView {

    private Set<String> validatorRefs;
    private String uri;
    private String content;
    private String multiplicity;

    /**
     * Creates new sector view.
     */
    public MessageView() {
        super(new MessageIcon(ICON_SIDE, ICON_SIDE));
        validatorRefs = new HashSet<>();
    }

    @Override
    protected List<Pair> getAdditionalData() {
        List<Pair> pairs = new ArrayList<>();
        if (uri != null && !uri.isEmpty()) {
            pairs.add(new Pair("uri", uri));
        }
        if (content != null && !content.isEmpty()) {
            pairs.add(new Pair("content", content));
        }
        if (multiplicity != null && !multiplicity.isEmpty()) {
            pairs.add(new Pair("multiplicity", multiplicity));
        }

        for (String validatorRef : validatorRefs) {
            pairs.add(new Pair("Validator", validatorRef));
        }

        return pairs;
    }

    @Override
    protected Color getIconColor() {
        return colorScheme.getColor(NamedColor.COMPONENT_MESSAGE);
    }

    @Override
    protected void initManagementIcons() {
        managementIcons.add(new MinusIcon(colorScheme.getColor(NamedColor.ACCENT_1)));
        managementIcons.add(new PlusIcon(colorScheme.getColor(NamedColor.ACCENT_4)));
    }

    public Set<String> getValidatorRefs() {
        return validatorRefs;
    }

    public void setValidatorRefs(Set<String> validatorRefs) {
        this.validatorRefs = validatorRefs;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(String multiplicity) {
        this.multiplicity = multiplicity;
    }
}
