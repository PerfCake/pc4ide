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

import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.validation.error.ValidationError;

/**
 * Validator is able to check if an object is valid content of a property and create
 * {@link org.perfcake.ide.core.model.validation.error.ValidationError}
 *
 * @author Jakub Knetl
 */
public interface Validator<T> {

    /**
     * Validates value and returns error (if any).
     * @param property property which is being validated
     * @param value value to be validated
     * @return ValidationError which represents error, or null if value is valid.
     */
    ValidationError validate(Property property, T value);
}
