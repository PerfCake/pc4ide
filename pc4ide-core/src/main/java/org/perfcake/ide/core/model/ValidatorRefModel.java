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

import java.util.Collections;
import java.util.List;

import org.perfcake.model.Scenario.Messages.Message.ValidatorRef;

/**
 * Model of validator reference.
 */
public class ValidatorRefModel extends AbstractModel {

    public static final String PROPERTY_ID = "validatorRef-id";

    private ValidatorRef validatorRef;

    ValidatorRefModel(ValidatorRef validatorRef) {
        super();
        if (validatorRef == null) {
            throw new IllegalArgumentException("ValidatorRef must not be null.");
        }
    }

    /**
     * creates new validator reference model.
     */
    public ValidatorRefModel() {
        super();
        validatorRef = new ValidatorRef();
    }

    /**
     * This method should not be used for modifying validatorRef (in a way getValidatorRef().setId()))
     * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
     *
     * @return PerfCake model of validatorRef
     */
    ValidatorRef getValidatorRef() {
        return validatorRef;
    }

    public String getId() {
        return validatorRef.getId();
    }

    /**
     * Sets id of the reference.
     * @param id id of the reference
     */
    public void setId(String id) {
        final String oldId = getValidatorRef().getId();
        validatorRef.setId(id);
        getPropertyChangeSupport().firePropertyChange(PROPERTY_ID, oldId, id);
    }

    @Override
    public List<AbstractModel> getModelChildren() {
        return Collections.emptyList();
    }

}
