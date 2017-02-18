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
 * Represents a store of key value pair. Additionally, key value pair may also
 * contain any String representation of another attribute, because some PerfCake key-value
 * tags such as property may have any embedded data.
 *
 * @author Jakub Knetl
 */
public interface KeyValue extends Property {
    String getKey();

    void setKey(String key);

    String getValue();

    void setValue(String value);

    String getAny();

    void setAny(String any);
}
