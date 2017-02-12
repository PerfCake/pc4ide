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

package org.perfcake.ide.core.newmodel;

import org.perfcake.ide.core.newmodel.simple.KeyValue;
import org.perfcake.ide.core.newmodel.simple.Value;

/**
 * Type which represent a value of a property.
 *
 * @author Jakub Knetl
 */
public enum PropertyType {

    /*
     * Class which represent property type must implement PropertyRepresentaiton!
     */
    VALUE(Value.class), KEY_VALUE(KeyValue.class), MODEL(Model.class);

    private final Class<? extends PropertyRepresentation> clazz;

    PropertyType(Class<? extends PropertyRepresentation> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends PropertyRepresentation> getClazz() {
        return clazz;
    }
}
