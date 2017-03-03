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

package org.perfcake.ide.core.model.validation.error;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.perfcake.ide.core.model.Property;

/**
 * Compound error is {@link ValidationError} which consists of multiple errors.
 *
 * @author Jakub Knetl
 */
public class CompoundError implements ValidationError {

    private List<ValidationError> errors;
    private Property property;
    private String description;

    /**
     * Creates new compound error.
     * @param property    property which is invalid because of multiple errors
     * @param description description of error
     * @param errors      non empty list of errors.
     */
    public CompoundError(Property property, String description, List<ValidationError> errors) {
        if (property == null) {
            throw new IllegalArgumentException("property cannot be null.");
        }
        if (errors == null) {
            throw new IllegalArgumentException("errors cannot be null.");
        }
        if (errors.isEmpty()) {
            throw new IllegalArgumentException("errors cannot be empty.");
        }

        if (description == null) {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < errors.size(); i++) {
                if (i > 0) {
                    builder.append(". ");
                }
                builder.append(errors.get(i).getDescription());
            }
            description = builder.toString();
        }

        this.errors = errors;
        this.property = property;
        this.description = description;
    }

    /**
     * Creates new compound error.
     * @param property    property which is invalid because of multiple errors
     * @param description description of error
     * @param errors      non empty array of errors
     */
    public CompoundError(Property property, String description, ValidationError... errors) {
        this (property, description, Arrays.asList(errors));
    }


    /**
     * Creates new compound error.
     *
     * @param property property which is invalid because of multiple errors
     * @param errors   non empty list of errors.
     */
    public CompoundError(Property property, List<ValidationError> errors) {
        this(property, null, errors);
    }

    /**
     * @return Unmodifiable list of {@link ValidationError} which forms this compound error.
     */
    public List<ValidationError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.COMPOUND_ERROR;
    }

    @Override
    public Property getProperty() {
        return property;
    }
}
