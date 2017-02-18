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

package org.perfcake.ide.core.newmodel.component;

import java.util.Objects;

/**
 * Implementation field contains information about a field in some component implmentation.
 * @author Jakub Knetl
 */
public class ImplementationField {

    private String name;
    private String value;
    private boolean manadatory;

    /**
     * Creates description of a field.
     *
     * @param name name of the field
     * @param value value of the field
     * @param manadatory is field mandatory?
     */
    public ImplementationField(String name, String value, boolean manadatory) {
        this.name = name;
        this.value = value;
        this.manadatory = manadatory;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isManadatory() {
        return manadatory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImplementationField that = (ImplementationField) o;
        return manadatory == that.manadatory
                && Objects.equals(name, that.name)
                && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, manadatory);
    }

    @Override
    public String toString() {
        return "ImplementationField{"
                + "name='" + name + '\''
                + ", value='" + value + '\''
                + ", manadatory=" + manadatory
                + '}';
    }
}
