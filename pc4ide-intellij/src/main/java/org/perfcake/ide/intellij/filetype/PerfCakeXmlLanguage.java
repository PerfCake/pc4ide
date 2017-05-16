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

import com.intellij.lang.Language;

/**
 * Represents a PerfCake XML language.
 * @author Jakub Knetl
 */
public class PerfCakeXmlLanguage extends Language {

    public static final PerfCakeXmlLanguage INSTANCE = new PerfCakeXmlLanguage();

    private PerfCakeXmlLanguage() {
        super("PerfCake XML");
    }
}
