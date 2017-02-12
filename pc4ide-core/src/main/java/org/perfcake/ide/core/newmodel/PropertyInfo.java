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
import org.perfcake.ide.core.exception.ModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains metadata about property of a model class.
 *
 * @author Jakub Knetl
 */
public class PropertyInfo {

    static final Logger logger = LoggerFactory.getLogger(PropertyInfo.class);

    /**
     * Name of the property.
     */
    private String name;

    /**
     * Represent which model is used in order to represent this property.
     */
    private PropertyType propertyType;

    /**
     * Default value of the property.
     */
    private Object defaultValue;

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
     * @param <T> Type of property value.
     * @param name         Name of the property
     * @param defaultValueClazz class of a default value
     * @param defaultValue default value of the property
     * @param minOccurs    minimum number of occurrences of this property.
     * @param maxOccurs    maximum number of occurrences of this property. Use -1 for unlimited.
     */
    public <T extends PropertyRepresentation> PropertyInfo(String name, Class<? extends T> defaultValueClazz,
                                                           T defaultValue, int minOccurs, int maxOccurs) {

        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        if (propertyType == null) {
            throw new IllegalArgumentException("PropertyType cannot be null.");
        }

        if (defaultValueClazz == null) {
            throw new IllegalArgumentException("defaultValue class must not be null");
        }

        if (minOccurs < 0) {
            throw new IllegalArgumentException("Minimum number of occurences must be positive.");
        }

        if (maxOccurs >= 0 && minOccurs > maxOccurs) {
            throw new IllegalArgumentException("If maximum occurs is not unlimeted, then it must not be lower than minimum"
                                               + " number of occurences.");
        }

        // Detects type of a property based on implementation class.
        propertyType = detectPropertyType(defaultValueClazz);

        this.name = name;
        this.propertyType = propertyType;
        this.defaultValue = defaultValue;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
    }

    private <T extends PropertyRepresentation> PropertyType detectPropertyType(Class<T> defaultValueClazz) {
        PropertyType result = null;

        for (PropertyType type : PropertyType.values()) {
            if (type.getClazz().isAssignableFrom(defaultValueClazz)) {
                result = type;
                break;
            }
        }

        if (result == null) {
            throw new IllegalArgumentException(String.format("Class %s is not supported type of property.",
                    defaultValueClazz.getCanonicalName()));
        }

        return result;
    }

    /**
     * @return Property name.
     */
    public String getName() {
        return name;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    /**
     * @param <T> Type of property value.
     * @param type expected type of default value
     * @return Property default value or null, if property has no default value.
     * @throws ModelException when the default value cannot be cast to expected type
     */
    public <T extends PropertyRepresentation> T getDefaultValue(Class<T> type) throws ModelException {
        if (type == null) {
            throw new IllegalArgumentException("DefaultValueClazz cannot be null.");
        }

        T result = null;
        try {
            result = type.cast(defaultValue);
        } catch (ClassCastException e) {
            throw new ModelException(String.format("Cannot cast %s to %s.",
                    defaultValue.getClass().getCanonicalName(), type.getCanonicalName()));
        }

        return result;
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
        PropertyInfo that = (PropertyInfo) o;
        return minOccurs == that.minOccurs
               && maxOccurs == that.maxOccurs
               && Objects.equals(name, that.name)
               && Objects.equals(propertyType, that.propertyType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, propertyType, minOccurs, maxOccurs);
    }

    @Override
    public String toString() {
        return "PropertyInfo{"
               + "name='" + name + '\''
               + ", propertyType=" + propertyType
               + ", defaultValue=" + defaultValue
               + ", minOccurs=" + minOccurs
               + ", maxOccurs=" + maxOccurs
               + '}';
    }
}
