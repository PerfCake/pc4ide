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

/**
 * Represents type of the field.
 */
public enum FieldType {
    /**
     * Simple field represents single value field (etc. int, double, String)
     */
    SIMPLE,

    /**
     * Property is a simple field which has always String value, but it is special field because it is
     * specific for particular component implementation. If you change class field of the component then
     * its PROPERTY fields will probably no longer apply.
     */
    PROPERTY,
    /**
     * PERFCAKE field is represented by enclosing class defined in perfcake/pc4ide (e.g. DestinationModel)
     */
    PERFCAKE,

    /**
     * The field is represented by the collection of elements.
     */
    COLLECTION;
}
