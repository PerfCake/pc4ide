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

package org.perfcake.ide.intellij.filetype;

import com.intellij.openapi.fileTypes.LanguageFileType;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.perfcake.ide.intellij.PerfCakeIcons;

/**
 * Represents a PerfCake scenario file type.
 *
 * @author Jakub Knetl
 */
public class PerfCakeXmlScenarioFileType extends LanguageFileType {
    public static final PerfCakeXmlScenarioFileType INSTANCE = new PerfCakeXmlScenarioFileType();

    /**
     * Creates a language file type for the specified language.
     */
    private PerfCakeXmlScenarioFileType() {
        super(PerfCakeXmlLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "PerfCake scenario";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Contains definition of a PerfCake scenario";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "xml";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return PerfCakeIcons.SMALL_PERFCAKE_ICON;
    }
}
