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

import java.util.Objects;

/**
 * Contains metadata about property of a model class.
 *
 * @author Jakub Knetl
 */
public class PropertyType<T> {

    /**
     * Name of the property.
     */
    private String name;


    /**
     * Represent which model is used in order to represent this property.
     */
    private ModelType modelType;

    /**
     * Default value of the property.
     */
    private T defaultValue;

    /**
     * Minimum number of required occurrences of this property.
     */
    private int minOccurs;

    /**
     * Maximum number of occurrences of this property. Set to -1 for unlimited.
     */
    private int maxOccurs;

    /**
     * Creates new propertyInfo
     *
     * @param name         Name of the property
     * @param modelType    Type which represents property value.
     * @param defaultValue default value of the property
     * @param minOccurs    minimum number of occurrences of this property.
     * @param maxOccurs    maximum number of occurrences of this property. Use -1 for unlimited.
     */
    public PropertyType(String name, ModelType modelType, T defaultValue, int minOccurs, int maxOccurs) {

        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        if (modelType == null) {
            throw new IllegalArgumentException("ModelType cannot be null.");
        }
        if (minOccurs < 0) {
            throw new IllegalArgumentException("Minimum number of occurences must be positive.");
        }
        if (maxOccurs >= 0 && minOccurs > maxOccurs) {
            throw new IllegalArgumentException("If maximum occurs is not unlimeted, then it must not be lower than minimum"
                                               + " number of occurences.");
        }
        this.name = name;
        this.modelType = modelType;
        this.defaultValue = defaultValue;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
    }

    /**
     * @return Property name.
     */
    public String getName() {
        return name;
    }

    public ModelType getModelType() {
        return modelType;
    }

    /**
     * @return Property default value or null, if property has no default value.
     */
    public T getDefaultValue() {
        return defaultValue;
    }


    /**
     * @return Minimum number of occurrences of this property.
     */
    public int getMinOccurs() {
        return minOccurs;
    }

    /**
     * @return Maximum number of occurrences of this property. -1 means unlimited.
     */
    public int getMaxOccurs() {
        return maxOccurs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PropertyType<?> that = (PropertyType<?>) o;
        return minOccurs == that.minOccurs
               && maxOccurs == that.maxOccurs
               && Objects.equals(name, that.name)
               && Objects.equals(modelType, that.modelType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, modelType, minOccurs, maxOccurs);
    }

    @Override
    public String toString() {
        return "PropertyType{"
               + "name='" + name + '\''
               + ", modelType=" + modelType
               + ", defaultValue=" + defaultValue
               + ", minOccurs=" + minOccurs
               + ", maxOccurs=" + maxOccurs
               + '}';
    }
}
