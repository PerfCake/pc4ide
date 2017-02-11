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
     * Class representing a value of the property.
     */
    private Class<T> clazz;

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
     * @param clazz        Type which represents property value.
     * @param defaultValue default value of the property
     * @param minOccurs    minimum number of occurrences of this property.
     * @param maxOccurs    maximum number of occurrences of this property. Use -1 for unlimited.
     */
    public PropertyType(String name, Class<T> clazz, T defaultValue, int minOccurs, int maxOccurs) {

        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null.");
        }
        if (minOccurs < 0) {
            throw new IllegalArgumentException("Minimum number of occurences must be positive.");
        }
        if (maxOccurs >= 0 && minOccurs > maxOccurs) {
            throw new IllegalArgumentException("If maximum occurs is not unlimeted, then it must not be lower than minimum"
                                               + " number of occurences.");
        }
        this.name = name;
        this.clazz = clazz;
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

    /**
     * @return Class representing property value.
     */
    public Class<T> getClazz() {
        return clazz;
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
               && Objects.equals(clazz, that.clazz)
               && Objects.equals(defaultValue, that.defaultValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, clazz, defaultValue, minOccurs, maxOccurs);
    }

    @Override
    public String toString() {
        return "PropertyType{"
               + "name='" + name + '\''
               + ", clazz=" + clazz
               + ", defaultValue=" + defaultValue
               + ", minOccurs=" + minOccurs
               + ", maxOccurs=" + maxOccurs
               + '}';
    }
}
