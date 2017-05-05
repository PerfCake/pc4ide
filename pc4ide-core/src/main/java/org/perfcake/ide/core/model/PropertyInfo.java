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
import org.perfcake.ide.core.components.ComponentLoaderImpl;
import org.perfcake.ide.core.components.PerfCakeComponent;
import org.perfcake.ide.core.docs.DocsService;
import org.perfcake.ide.core.exception.ModelException;
import org.perfcake.ide.core.model.properties.DataType;
import org.perfcake.ide.core.model.properties.KeyValue;
import org.perfcake.ide.core.model.properties.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Contains metadata about property of a model class.</p>
 *
 * <p>Use on of the static factory methods in order to instantiate this class.</p>
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
     * Data type of a key (applicable only if this {@link PropertyType} is KEY_VALUE).
     */
    private DataType keyDataType;

    /**
     * Data type of a value (applicable for all {@link PropertyType}s).
     */
    private DataType valueDataType;

    /**
     * Which component is represented by this property (applicable only in case that type == PropertyType.Model)
     */
    private PerfCakeComponent perfCakeComponent;

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

    private PropertyInfo(String name, Model model, PropertyType type, int minOccurs, int maxOccurs, DataType valueDataType) {

        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null.");
        }

        if (minOccurs < 0) {
            throw new IllegalArgumentException("Minimum number of occurences must be positive.");
        }

        if (maxOccurs >= 0 && minOccurs > maxOccurs) {
            throw new IllegalArgumentException("If maximum occurs is not unlimeted, then it must not be lower than minimum"
                    + " number of occurences.");
        }

        if (valueDataType == null) {
            throw new IllegalArgumentException("Value data type cannot be null.");
        }
        this.name = name;
        this.model = model;
        this.type = type;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
        this.valueDataType = valueDataType;
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
     * @return PerfCake component represented by this metadata, or null if {@link #getType()} is not a model.
     */
    public PerfCakeComponent getPerfCakeComponent() {
        return perfCakeComponent;
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

            Class<?> clazz = null;
            // try to parse information from implementation
            if (model.getSupportedProperty(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY) != null
                    && !model.getProperties(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY).isEmpty()) {
                Value implName = model.getSingleProperty(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY, Value.class);
                clazz = new ComponentLoaderImpl().loadComponent(implName.getValue(), model.getComponent());
            }

            if (clazz == null) {
                // use component API as a base
                clazz = model.getComponent().getApi();
            }

            docs = model.getDocsService().getFieldDocs(clazz, name);
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

    public DataType getKeyDataType() {
        return keyDataType;
    }

    public DataType getValueDataType() {
        return valueDataType;
    }

    /**
     * Creates info about {@link Value} property.
     *
     * @param name      name
     * @param model     model which owns this property
     * @param minOccurs minimum number of occurrences of this property.
     * @param maxOccurs maximum number of occurrences of this property. Use -1 for unlimited.
     * @return property info
     */
    public static PropertyInfo createValueInfo(String name, Model model, int minOccurs, int maxOccurs) {
        PropertyInfo info = new PropertyInfo(name, model, PropertyType.VALUE, minOccurs, maxOccurs, DataType.STRING);
        return info;
    }

    /**
     * Creates info about {@link Value} property.
     *
     * @param name          name
     * @param displayName   display name (optional)
     * @param model         model which owns this property
     * @param minOccurs     minimum number of occurrences of this property.
     * @param maxOccurs     maximum number of occurrences of this property. Use -1 for unlimited.
     * @param valueDataType data type of value (optional)
     * @param defaultValue  default value (optional)
     * @return property info
     */
    public static PropertyInfo createValueInfo(String name, String displayName, Model model, int minOccurs, int maxOccurs,
                                               DataType valueDataType, Value defaultValue) {
        PropertyInfo info = createValueInfo(name, model, minOccurs, maxOccurs);
        if (valueDataType != null) {
            info.valueDataType = valueDataType;
        }
        info.defaultValue = defaultValue;
        info.displayName = displayName;
        return info;
    }

    /**
     * Creates info about {@link KeyValue} property.
     *
     * @param name      name
     * @param model     model which owns this property
     * @param minOccurs minimum number of occurrences of this property.
     * @param maxOccurs maximum number of occurrences of this property. Use -1 for unlimited.
     * @return property info
     */
    public static PropertyInfo createKeyValueInfo(String name, Model model, int minOccurs, int maxOccurs) {
        PropertyInfo info = new PropertyInfo(name, model, PropertyType.KEY_VALUE, minOccurs, maxOccurs, DataType.STRING);
        info.valueDataType = DataType.STRING;
        info.keyDataType = DataType.STRING;
        return info;
    }

    /**
     * Creates info about {@link KeyValue} property.
     *
     * @param name          name
     * @param displayName   display name (optional)
     * @param model         model which owns this property
     * @param minOccurs     minimum number of occurrences of this property.
     * @param maxOccurs     maximum number of occurrences of this property. Use -1 for unlimited.
     * @param keyDataType   data type of key (optional)
     * @param valueDataType data type of value (optional)
     * @param defaultValue  default value (optional)
     * @return property info
     */
    public static PropertyInfo createKeyValueInfo(String name, String displayName, Model model, int minOccurs, int maxOccurs,
                                                  DataType keyDataType, DataType valueDataType, KeyValue defaultValue) {
        PropertyInfo info = createKeyValueInfo(name, model, minOccurs, maxOccurs);
        if (valueDataType != null) {
            info.valueDataType = valueDataType;
        }
        if (keyDataType != null) {
            info.keyDataType = keyDataType;
        }

        info.displayName = displayName;
        info.defaultValue = defaultValue;
        return info;
    }

    /**
     * Creates info about {@link Model} property.
     *
     * @param name      name
     * @param model     model which owns this property
     * @param minOccurs minimum number of occurrences of this property.
     * @param maxOccurs maximum number of occurrences of this property. Use -1 for unlimited.
     * @param component perfcake component
     * @return property info
     */
    public static PropertyInfo createModelInfo(String name, Model model, PerfCakeComponent component, int minOccurs, int maxOccurs) {
        PropertyInfo info = new PropertyInfo(name, model, PropertyType.MODEL, minOccurs, maxOccurs, DataType.MODEL);
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null.");
        }

        info.perfCakeComponent = component;

        return info;
    }


    /**
     * Creates info about {@link Model} property.
     *
     * @param name        name
     * @param displayName display name (optional)
     * @param model       model which owns this property
     * @param minOccurs   minimum number of occurrences of this property.
     * @param maxOccurs   maximum number of occurrences of this property. Use -1 for unlimited.
     * @param component   perfcake component
     * @return property info
     */
    public static PropertyInfo createModelInfo(String name, String displayName, Model model, PerfCakeComponent component,
                                               int minOccurs, int maxOccurs) {
        PropertyInfo info = createModelInfo(name, model, component, minOccurs, maxOccurs);

        info.displayName = displayName;

        return info;
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
                && keyDataType == that.keyDataType
                && valueDataType == that.valueDataType
                && Objects.equals(defaultValue, that.defaultValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, model, type, keyDataType, valueDataType, defaultValue, minOccurs, maxOccurs);
    }

    @Override
    public String toString() {
        return "PropertyInfo{"
                + "name='" + name + '\''
                + ", displayName='" + displayName + '\''
                + ", model=" + model
                + ", type=" + type
                + ", keyDataType=" + keyDataType
                + ", valueDataType=" + valueDataType
                + ", defaultValue=" + defaultValue
                + ", minOccurs=" + minOccurs
                + ", maxOccurs=" + maxOccurs
                + '}';
    }
}
