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

import org.perfcake.ide.core.model.properties.DataType;

/**
 * Class which serves as a factory for validators.
 *
 * @author Jakub Knetl
 */
public class Validators {

    private Validators() {
    }

    /**
     * Creates a validator for a particular data type.
     *
     * @param dataType data type.
     * @return validator for given data type
     */
    public static Validator createValidator(DataType dataType) {
        if (dataType == null) {
            throw new IllegalArgumentException("data type cannot be null");
        }

        Validator validator = null;

        switch (dataType) {
            case STRING:
                validator = new StringValidator();
                break;
            case INTEGER:
                validator = new IntegerValidator();
                break;
            case FLOAT:
                validator = new FloatValidator();
                break;
            case BOOLEAN:
                validator = new EnumValidator(DataType.BOOLEAN.getValues());
                break;
            case PERIOD:
                validator = new EnumValidator(DataType.PERIOD.getValues());
                break;
            case MODEL:
                validator = new ModelValidator();
                break;
            default:
                throw new IllegalArgumentException("Unknown data type");
        }


        // add placeholder validator since every value can be a placeholder in a scenario
        return new PlaceholderValidator(validator);
    }
}
