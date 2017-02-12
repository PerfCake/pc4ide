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

package org.perfcake.ide.core.newmodel.modeltypes;

import org.perfcake.ide.core.components.ComponentManager;
import org.perfcake.ide.core.newmodel.AbstractModel;
import org.perfcake.ide.core.newmodel.ModelType;
import org.perfcake.ide.core.newmodel.PropertyType;
import org.perfcake.message.sequence.Sequence;

/**
 * Model of a sequence component.
 *
 * @author Jakub Knetl
 */
public class SequenceModel extends AbstractModel {

    public enum PropertyNames {
        IMPLEMENTATION(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY), ID("id");

        private final String propertyName;

        PropertyNames(String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public String toString() {
            return this.propertyName;
        }
    }

    /**
     * Creates new model of PerfCake Sequence component.
     *
     * @param componentManager PerfCake component manager
     */
    public SequenceModel(ComponentManager componentManager) {
        super(componentManager, Sequence.class);
    }

    @Override
    protected void initializeSupportedProperties() {
        addSupportedProperties(
                new PropertyType<>(PropertyNames.IMPLEMENTATION.toString(), ModelType.VALUE, null, 1, 1),
                new PropertyType<>(PropertyNames.ID.toString() , ModelType.VALUE, null, 1, 1));
    }
}
