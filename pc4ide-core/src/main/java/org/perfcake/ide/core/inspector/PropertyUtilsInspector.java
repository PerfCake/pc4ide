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

package org.perfcake.ide.core.inspector;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.FluentPropertyBeanIntrospector;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.SuppressPropertiesBeanIntrospector;
import org.perfcake.ide.core.components.PerfCakeComponent;
import org.perfcake.ide.core.model.properties.DataType;
import org.perfcake.util.properties.MandatoryProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * PropertyUtilsInspector uses Commons Beanutils to parse implementation fields from the class.
 *
 * @author Jakub Knetl
 */
public class PropertyUtilsInspector implements PropertyInspector {

    static final Logger logger = LoggerFactory.getLogger(PropertyUtilsInspector.class);

    @Override
    public List<ImplementationField> getProperties(Class<?> implementation) {

        PerfCakeComponent component = PerfCakeComponent.detectComponentType(implementation);

        if (component == null) {
            throw new IllegalArgumentException("implementation class must be implementation of some Perfcake component.");
        }

        PropertyUtilsBean propertyUtils = new PropertyUtilsBean();
        propertyUtils.addBeanIntrospector(new FluentPropertyBeanIntrospector()); // also parse beans with fluent setters
        propertyUtils.addBeanIntrospector(SuppressPropertiesBeanIntrospector.SUPPRESS_CLASS); // do not parse class field
        List<String> apiProperties = getApiProperties(propertyUtils.getPropertyDescriptors(component.getApi()));
        propertyUtils.addBeanIntrospector(new SuppressPropertiesBeanIntrospector(apiProperties)); // do not parse fields from api

        PropertyDescriptor[] implementationDescriptors = propertyUtils.getPropertyDescriptors(implementation);


        List<ImplementationField> fields = new ArrayList<>();
        Object instance = null;
        try {
            instance = implementation.newInstance();
        } catch (InstantiationException e) {
            logger.warn(String.format("Cannot instantiate an implementation class %s, default values won't be set", implementation), e);
        } catch (IllegalAccessException e) {
            logger.warn(String.format("Cannot instantiate an implementation class %s, default values won't be set", implementation), e);
        }
        for (PropertyDescriptor descriptor : implementationDescriptors) {

            // skip property which has no setter, since it is useless to tweak its value
            if (descriptor.getWriteMethod() != null) {
                String value = getDefaultValue(implementation, propertyUtils, instance, descriptor);
                boolean isMandatory = hasMandatoryAnnotation(descriptor, implementation, component.getApi());
                DataType dataType = DataType.detectFromJavaType(descriptor.getPropertyType());
                ImplementationField field = new ImplementationField(descriptor.getName(), value, isMandatory, dataType);
                fields.add(field);
            }

        }
        return fields;
    }

    /**
     * Detects if the PropertyDescriptor has associated @mandatory annotation.
     *
     * @param descriptor     descriptor of the property to be checked
     * @param implementation Class for which we check annotation
     * @param api            Class which represents api class.
     * @return true if the property description contains mandatory annotation.
     */
    private boolean hasMandatoryAnnotation(PropertyDescriptor descriptor, Class<?> implementation, Class<?> api) {

        boolean isMandatory = false;
        boolean fieldDefinitionFound = false;
        Class<?> checkedClazz = implementation;

        // search the field definition in the type hierarchy starting from implementaiton
        while (!fieldDefinitionFound && !api.equals(checkedClazz) && checkedClazz != null) {
            try {
                Field f = checkedClazz.getDeclaredField(descriptor.getName());
                fieldDefinitionFound = true;
                isMandatory = (f.getAnnotationsByType(MandatoryProperty.class).length > 0);
                break;
            } catch (NoSuchFieldException e) {
                // try to find field in superclass in the next iteration
                checkedClazz = checkedClazz.getSuperclass();
                if (!api.isAssignableFrom(checkedClazz)) {
                    checkedClazz = null;
                }
            }
        }

        if (!fieldDefinitionFound) {
            logger.info("Definition of the field {}, in inspector {} was not found. Assuming that property is not mandatory.",
                    descriptor.getName(), implementation.getCanonicalName());
        }

        return isMandatory;
    }

    /**
     * Determines default value of an property.
     *
     * @param implementation type of the bean
     * @param propertyUtils  propertyUtils instance
     * @param instance       instance of a bean
     * @param descriptor     descriptor of the property
     * @return default value of a bean or null, if it is not possible to get default value.
     */
    private String getDefaultValue(Class<?> implementation,
                                   PropertyUtilsBean propertyUtils, Object instance, PropertyDescriptor descriptor) {
        String value = null;
        if (instance != null) {
            try {
                Object v = propertyUtils.getSimpleProperty(instance, descriptor.getName());
                value = (v == null) ? null : String.valueOf(v);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException  e) {
                logger.warn(String.format("Cannot obtain default value of the property %s,"
                        + " in the implementation class %s, default value won't be set", descriptor.getName(), implementation));
            }

        }
        return value;
    }

    private List<String> getApiProperties(PropertyDescriptor[] apiDescriptors) {
        List<String> apiProperties = new ArrayList<>();
        for (PropertyDescriptor d : apiDescriptors) {
            apiProperties.add(d.getName());
        }
        return apiProperties;
    }
}

