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

package org.perfcake.ide.core.model.director;

import org.perfcake.ide.core.Field;

/**
 * ModelField represents a field in the model.
 *
 * @author jknetl
 */
public class ModelField extends Field {

    public ModelField(FieldType fieldType, String name, String docs, boolean isMandatory) {
        super(fieldType, name, docs, isMandatory);
    }

    public ModelField(FieldType fieldType, String name, String docs) {
        super(fieldType, name, docs, false);
    }
}
