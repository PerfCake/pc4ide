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

package org.perfcake.ide.core.model;

import java.util.Objects;
import org.perfcake.ide.core.docs.DocsService;
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
     * Name of the property to be displayed in UI. If this value is null, then name field is used.
     */
    private String displayName;

    /**
     * Model to which property described by this propertyInfo instance belongs.
     */
    private Model model;

    /**
     * Represent which model is used in order to represent this property.
     */
    private PropertyType type;

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
     * @param <T>               Type of property value.
     * @param name              Name of the property
     * @param displayName       Name to be displayed in the UI
     * @param model             Model to which this property belongs
     * @param defaultValueClazz class of a default value
     * @param defaultValue      default value of the property
     * @param minOccurs         minimum number of occurrences of this property.
     * @param maxOccurs         maximum number of occurrences of this property. Use -1 for unlimited.
     */
    public <T extends Property> PropertyInfo(String name, String displayName, Model model, Class<? extends T> defaultValueClazz,
                                             T defaultValue, int minOccurs, int maxOccurs) {

        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null.");
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
        type = PropertyType.detectPropertyType(defaultValueClazz);

        this.name = name;
        this.defaultValue = defaultValue;
        this.displayName = displayName;
        this.model = model;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
    }

    /**
     * Creates new propertyInfo
     *
     * @param <T>               Type of property value.
     * @param name              Name of the property
     * @param model             Model to which this property belongs
     * @param defaultValueClazz class of a default value
     * @param defaultValue      default value of the property
     * @param minOccurs         minimum number of occurrences of this property.
     * @param maxOccurs         maximum number of occurrences of this property. Use -1 for unlimited.
     */
    public <T extends Property> PropertyInfo(String name, Model model, Class<? extends T> defaultValueClazz,
                                             T defaultValue, int minOccurs, int maxOccurs) {
        this(name, null, model, defaultValueClazz, defaultValue, minOccurs, maxOccurs);
    }


    /**
     * @return Property name.
     */
    public String getName() {
        return name;
    }

    public PropertyType getType() {
        return type;
    }

    /**
     * @param <T>  Type of property value.
     * @param type expected type of default value
     * @return Property default value or null, if property has no default value.
     * @throws ModelException when the default value cannot be cast to expected type
     */
    public <T extends Property> T getDefaultValue(Class<T> type) throws ModelException {
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
     * @return returns name to be displayed in the UI.
     */
    public String getDisplayName() {
        if (displayName == null || displayName.isEmpty()) {
            return name;
        } else {
            return displayName;
        }
    }

    public Model getModel() {
        return model;
    }

    /**
     * @return Documentation of this property.
     */
    public String getDocs() {
        DocsService docsService = model.getDocsService();
        String docs;
        if (type == PropertyType.MODEL) {
            docs = docsService.getDocs(model.getComponent().getApi());
        } else {
            docs = model.getDocsService().getFieldDocs(model.getComponent().getApi(), name);
        }
        return docs;
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
                && Objects.equals(model, that.model)
                && type == that.type
                && Objects.equals(defaultValue, that.defaultValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, model, type, defaultValue, minOccurs, maxOccurs);
    }

    @Override
    public String toString() {
        return "PropertyInfo{"
                + "name='" + name + '\''
                + ", displayName='" + displayName + '\''
                + ", model=" + model
                + ", type=" + type
                + ", defaultValue=" + defaultValue
                + ", minOccurs=" + minOccurs
                + ", maxOccurs=" + maxOccurs
                + '}';
    }
}
