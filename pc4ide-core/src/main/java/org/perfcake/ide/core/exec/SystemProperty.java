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

package org.perfcake.ide.core.exec;

import java.util.Objects;

/**
 * Represents system property in perfcake execution.
 *
 * @author Jakub Knetl
 */
public class SystemProperty {
    private String key;
    private String value;

    public SystemProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public SystemProperty setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public SystemProperty setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemProperty that = (SystemProperty) o;
        return Objects.equals(key, that.key)
                && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "SystemProperty{"
                + "key='" + key + '\''
                + ", value='" + value + '\''
                + '}';
    }
}
