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

import java.util.List;

/**
 * Represents PerfCake component.
 *
 * @author jknetl
 */
public class Component {

    /**
     * Kind of the component.
     */
    private ComponentKind kind;

    /**
     * class of the component.
     */
    private Class<?> implementation;

    /**
     * List of the fields in the component.
     */
    private List<PropertyField> propertyFields;

    /**
     * Documentation of the component.
     */
    private String documentation;

    /**
     * Creates new component.
     * @param kind kind of the component
     * @param implementation implemntation class of the component
     * @param fields fields of the component
     * @param documentation Documentation or description of the component
     */
    public Component(ComponentKind kind, Class<?> implementation, List<PropertyField> fields, String documentation) {
        super();
        this.kind = kind;
        this.implementation = implementation;
        this.propertyFields = fields;
        this.documentation = documentation;
    }

    /**
     * Gets component kind.
     * @return Kind of the component
     */
    public ComponentKind getKind() {
        return kind;
    }

    /**
     * Gets implementation class.
     * @return class which implements the component
     */
    public Class<?> getImplementation() {
        return implementation;
    }

    /**
     * Gets property fields of the component.
     * @return List of the property fields of the component
     */
    public List<PropertyField> getPropertyFields() {
        return propertyFields;
    }

    /**
     * Return documentation of the component.
     * @return Documentation of the component
     */
    public String getDocumentation() {
        return documentation;
    }

    /**
     * Gets property field by name.
     * @param fieldName simple name of the field
     * @return PropertyField with given name or null if no such field can be found.
     */
    public PropertyField getPropertyField(String fieldName) {

        if (fieldName == null) {
            return null;
        }

        PropertyField result = null;

        for (final PropertyField field : propertyFields) {
            if (fieldName.equals(field.getName())) {
                result = field;
            }
        }

        return result;
    }
}
