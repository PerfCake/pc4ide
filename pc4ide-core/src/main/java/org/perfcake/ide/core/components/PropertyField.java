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

package org.perfcake.ide.core.components;

import org.perfcake.ide.core.Field;
import org.perfcake.ide.core.model.director.FieldType;

/**
 * Represents property field of the perfcake component.
 *
 * @author jknetl
 */
public class PropertyField extends Field {

    public PropertyField(String name, String docs, boolean isMandatory) {
        super(FieldType.PROPERTY, name, docs, isMandatory);
    }
}
