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

import org.apache.commons.lang3.math.NumberUtils;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.validation.error.ErrorType;
import org.perfcake.ide.core.model.validation.error.SingleError;
import org.perfcake.ide.core.model.validation.error.ValidationError;

/**
 * Validates whether string value is a number.
 * @author Jakub Knetl
 */
public class IntegerValidator implements Validator<String> {

    @Override
    public ValidationError validate(Property property, String value) {
        ValidationError error = null;
        if (!NumberUtils.isNumber(value)) {
            error = new SingleError(property, ErrorType.INVALID_VALUE, String.format("'%s' is not a number", value));
        }

        return error;
    }
}
