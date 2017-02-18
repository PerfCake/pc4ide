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
    private boolean mandatory;

    /**
     * Creates description of a field.
     *
     * @param name name of the field
     * @param value value of the field
     * @param mandatory is field mandatory?
     */
    public ImplementationField(String name, String value, boolean mandatory) {
        this.name = name;
        this.value = value;
        this.mandatory = mandatory;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isMandatory() {
        return mandatory;
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
        return mandatory == that.mandatory
                && Objects.equals(name, that.name)
                && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, mandatory);
    }

    @Override
    public String toString() {
        return "ImplementationField{"
                + "name='" + name + '\''
                + ", value='" + value + '\''
                + ", mandatory=" + mandatory
                + '}';
    }
}
