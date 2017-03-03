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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.PropertyInfo;
import org.perfcake.ide.core.model.validation.error.CompoundError;
import org.perfcake.ide.core.model.validation.error.ErrorType;
import org.perfcake.ide.core.model.validation.error.SingleError;
import org.perfcake.ide.core.model.validation.error.ValidationError;

/**
 * ModelValidator validates model object. Model is valid in case that:
 * <ol>
 * <li>All properties which are contained in model are valid</li>
 * <li>Model contains all required properties</li>
 * <li>Model does not have superfluos properties</li>
 * </ol>
 *
 * @author Jakub Knetl
 */
public class ModelValidator implements Validator<Model> {

    @Override
    public ValidationError validate(Property property, Model model) {
        Set<PropertyInfo> supportedProperties = model.getSupportedProperties();

        List<ValidationError> errors = new ArrayList<>();

        for (PropertyInfo info : supportedProperties) {
            int propertiesCount = model.getProperties(info).size();

            // validate number of properties for each propertyInfo
            if (propertiesCount < info.getMinOccurs()) {
                String description = String.format("Property '%s' has '%d' occurences, but minimum is '%s'",
                        info.getDisplayName(), propertiesCount, info.getMinOccurs());
                errors.add(new SingleError(property, ErrorType.MISSING_PROPERTY, description));
            }
            if (info.getMaxOccurs() > 0 && propertiesCount > info.getMaxOccurs()) { // max occurs can be negativ => unlimited bound
                String description = String.format("Property '%s' has '%d' occurences, but maximum is '%s'",
                        info.getDisplayName(), propertiesCount, info.getMaxOccurs());
                errors.add(new SingleError(property, ErrorType.SUPERFLUOS_PROPERTY, description));
            }

            for (Property p : model.getProperties(info)) {
                ValidationError propertyError = p.getValidationError();
                if (propertyError != null) {
                    errors.add(propertyError);
                }
            }
        }


        ValidationError result = null;

        if (!errors.isEmpty()) {
            result = new CompoundError(property, errors);
        }

        return result;
    }
}
