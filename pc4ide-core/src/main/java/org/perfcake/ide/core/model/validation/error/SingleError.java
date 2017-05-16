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

import org.perfcake.ide.core.model.Property;

/**
 * Represents single error in validation.
 *
 * @author Jakub Knetl
 */
public class SingleError implements ValidationError {

    private Property property;
    private ErrorType type;
    private String description;

    /**
     * Instantiates new single error.
     * @param property property which is invalid due to this error
     * @param type type of the error
     * @param description error description
     */
    public SingleError(Property property, ErrorType type, String description) {
        if (property == null) {
            throw new IllegalArgumentException("property cannot be null.");
        }
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null.");
        }
        if (description == null) {
            throw new IllegalArgumentException("description cannot be null.");
        }
        this.property = property;
        this.type = type;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ErrorType getErrorType() {
        return type;
    }

    @Override
    public Property getProperty() {
        return property;
    }
}
