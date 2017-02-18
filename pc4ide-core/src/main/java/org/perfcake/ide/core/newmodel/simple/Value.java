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

package org.perfcake.ide.core.newmodel.simple;

import org.perfcake.ide.core.newmodel.Property;

/**
 * Represents a simple store of a value.
 *
 * @author Jakub Knetl
 */
public interface Value extends Property {

    /**
     * @return current value.
     */
    String getValue();

    /**
     * Sets a new value.
     *
     * @param value new value
     */
    void setValue(String value);
}
