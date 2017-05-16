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

package org.perfcake.ide.core.model.validation;

import java.util.Arrays;
import java.util.List;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.validation.error.ErrorType;
import org.perfcake.ide.core.model.validation.error.SingleError;
import org.perfcake.ide.core.model.validation.error.ValidationError;

/**
 * This validator validates if a string value is one of allowed enumerated values.
 *
 * @author Jakub Knetl
 */
public class EnumValidator extends StringValidator {

    private List<String> allowedValues;
    private boolean caseSensitive = false;

    /**
     * Creates new enum validator.
     *
     * @param allowedValues values which are allowed.
     */
    public EnumValidator(List<String> allowedValues) {
        if (allowedValues == null) {
            throw new IllegalArgumentException("allowed values cannot be null");
        }
        if (allowedValues.isEmpty()) {
            throw new IllegalArgumentException("allowed values cannot be emtpy");
        }

        this.allowedValues = allowedValues;
    }

    /**
     * Creates new enum validator.
     *
     * @param allowedValues values which are allowed.
     */
    public EnumValidator(String... allowedValues) {
        this(Arrays.asList(allowedValues));
    }

    @Override
    public ValidationError validate(Property property, String value) {
        boolean isValid = false;
        ValidationError error = null;
        for (String s : allowedValues) {
            if (s == null && value == null) {
                isValid = true;
                break;
            }
            if (equals(value, s)) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            error = new SingleError(property, ErrorType.INVALID_VALUE, createErrorMessage(value));
        }

        return error;
    }

    /**
     * Compares two values if they are equal.
     * @param value value
     * @param s value
     * @return true if the values are equal
     */
    public boolean equals(String value, String s) {
        if (caseSensitive) {
            return s.equals(value);
        } else {
            return s.equalsIgnoreCase(value);
        }
    }

    private String createErrorMessage(String value) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("Value '%s' is not valid. Allowed values: ", value));

        for (int i = 0; i < allowedValues.size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(allowedValues.get(i));
        }

        return builder.toString();
    }
}
