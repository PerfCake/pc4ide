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
 * Simple validator is an base validator suitable for subclassing. This class always decides that string value is valid. But you may
 * want to extend {@link #validate(Property, String)}.
 * @author Jakub Knetl
 */
public class StringValidator implements Validator<String> {

    @Override
    public ValidationError validate(Property property, String value) {
        return null;
    }

}
