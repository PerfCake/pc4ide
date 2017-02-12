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
import org.perfcake.ide.core.newmodel.PropertyContainer;
import org.perfcake.ide.core.newmodel.PropertyType;
import org.perfcake.ide.core.newmodel.simple.KeyValueImpl;

/**
 * Represents model of a generator.
 * @author Jakub Knetl
 */
public class GeneratorModel extends AbstractModel {

    public static enum PropertyNames {
        RUN("Run"), IMPLEMENTATION(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY), THREADS("Threads");

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
     * Creates new model of PerfCake component.
     *
     * @param componentManager PerfCake component manager
     * @param api              Interface or abstract class of PerfCake component
     */
    public GeneratorModel(ComponentManager componentManager, Class api) {
        super(componentManager, api);
    }

    @Override
    protected void initializeSupportedProperties() {

        PropertyType<KeyValueImpl> runType = new PropertyType<>(PropertyNames.RUN.toString(), ModelType.KEY_VALUE, null, 1, 1);
        PropertyType<String> implementation = new PropertyType<>(PropertyNames.IMPLEMENTATION.toString(), ModelType.VALUE, null, 1, 1);
        PropertyType<String> threads = new PropertyType<>(PropertyNames.THREADS.toString(), ModelType.VALUE, null, 0, 1);

        properties.put(runType, new PropertyContainer<>(this, runType));
        properties.put(implementation, new PropertyContainer<>(this, implementation));
        properties.put(threads, new PropertyContainer<>(this, implementation));
    }
}
