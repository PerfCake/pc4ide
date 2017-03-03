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

package org.perfcake.ide.core.model.properties;

import java.util.Objects;
import org.perfcake.ide.core.model.AbstractProperty;
import org.perfcake.ide.core.model.PropertyType;
import org.perfcake.ide.core.model.validation.StringValidator;
import org.perfcake.ide.core.model.validation.Validator;
import org.perfcake.ide.core.model.validation.error.ValidationError;

/**
 * Represents properties value in model.
 *
 * @author Jakub Knetl
 */
public class SimpleValue extends AbstractProperty implements Value {

    private String value;
    private Validator<String> validator;

    /**
     * Creates new properties value.
     *
     * @param value value of the property
     */
    public SimpleValue(String value) {
        this(value, null);
    }

    /**
     * Creates new properties value.
     *
     * @param value     value of the property
     * @param validator value validator
     */
    public SimpleValue(String value, Validator<String> validator) {
        super(PropertyType.VALUE);
        this.value = value;
        if (validator == null) {
            this.validator = new StringValidator();
        } else {
            this.validator = validator;
        }
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        String oldValue = this.value;
        this.value = value;

        fireChangeEvent(oldValue, value);
    }

    @Override
    public boolean isValid() {
        return (validator.validate(this, value) == null);
    }

    @Override
    public ValidationError getValidationError() {
        return validator.validate(this, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleValue that = (SimpleValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "SimpleValue{"
                + "value='" + value + '\''
                + "} " + super.toString();
    }
}
