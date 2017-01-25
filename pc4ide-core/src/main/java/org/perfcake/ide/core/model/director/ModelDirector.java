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

import java.util.List;

import org.perfcake.ide.core.Field;
import org.perfcake.ide.core.components.PropertyField;
import org.perfcake.ide.core.exception.ModelDirectorException;


/**
 * ModelDirector is used to manipulate with arbitrary model class in a generic and uniform way.
 *
 * <p>There are two types of fields in the model:</p>
 * <ol>
 * <li>model fields</li>
 * <li>custom property fields</li>
 * </ol>
 * <p>Model fields are fields which are defined statically in the perfcake XML model. The model fields
 * are dependent just on the specific model.</p>
 * <p>Custom property fields are the fields which are specific for some model implementation. So they are dependent
 * on model class and the particular implementation (usually specified by the class field in the model class)</p>
 *
 * @author jknetl
 */
public interface ModelDirector {

    /**
     * Gets model object.
     * @return Model object which is controled by the director.
     */
    Object getModel();

    /**
     * Gets documentation or description of the model object.
     * @return Documentation of the model
     */
    String getDocs();

    /**
     * Gets list of all fields of the model object.
     * @return List of all fields applicable to model.
     */
    List<Field> getAllFields();

    /**
     * Return list of fields which are defined by the model. Custom property fields are ignored
     * since they are handled by {@link #getCustomPropertyFields()}.
     *
     * @return list of model fields (without custom property fields)
     */
    List<ModelField> getModelFields();

    /**
     * Finds a field by name. It searches through all fields (both model fields and custom property fields).
     *
     * @param name name of the field
     * @return a field with given name or null if no such field can be found.
     */
    Field getFieldByName(String name);

    /**
     * Gets current value of the field.
     *
     * @param field field whose value should be obtained
     * @return current value of the field
     * @throws ModelDirectorException if no such field can be found
     */
    Object getFieldValue(Field field) throws ModelDirectorException;

    /**
     * Sets field to given value.
     *
     * @param field field to set
     * @param value new value of the field
     * @throws ModelDirectorException when field does not correspond to model or setMethod cannot be found.
     */
    void setField(Field field, Object value) throws ModelDirectorException;


    /**
     * Gets custom property fields.
     * @return list of custom property fields corresponding with specific component implementation.
     */
    List<PropertyField> getCustomPropertyFields();
}
