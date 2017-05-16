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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.properties.DataType;
import org.perfcake.ide.core.model.validation.error.ValidationError;

/**
 * Placeholder validator first checks if a value is a placeholder. If it is then the value is expected to be valid. If the value
 * is not a placeholder then it uses another validator. It optionally also default value of the placeholder.
 *
 * @author Jakub Knetl
 */
public class PlaceholderValidator implements Validator<String> {

    private Validator validator;
    private boolean checkDefaultValue = true;

    /**
     * Creates a new placeholder validator.
     *
     * @param validator validator of a value
     */
    public PlaceholderValidator(Validator validator) {
        if (validator == null) {
            throw new IllegalArgumentException("Validator cannot be null.");
        }
        this.validator = validator;
    }

    @Override
    public ValidationError validate(Property property, String value) {
        if (DataType.isPlaceholder(value)) {
            if (checkDefaultValue) {
                return checkDefaultValue(property, value);
            } else {
                return null;
            }

        }
        return validator.validate(property, value);
    }

    private ValidationError checkDefaultValue(Property property, String value) {

        Pattern pattern = Pattern.compile("\\$\\{.*:(.*)\\}");
        Matcher matcher = pattern.matcher(value);

        if (matcher.find()) {
            String defaultValue = matcher.group(1);
            return validator.validate(property, defaultValue);
        }

        return null;
    }
}
