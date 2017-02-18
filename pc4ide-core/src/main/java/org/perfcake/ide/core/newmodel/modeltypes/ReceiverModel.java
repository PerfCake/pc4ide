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
import org.perfcake.ide.core.docs.DocsService;
import org.perfcake.ide.core.newmodel.AbstractModel;
import org.perfcake.ide.core.newmodel.PropertyInfo;
import org.perfcake.ide.core.newmodel.PropertyType;
import org.perfcake.message.receiver.Receiver;

/**
 * Represents model of a Receiver.
 *
 * @author Jakub Knetl
 */
public class ReceiverModel extends AbstractModel {

    public enum PropertyNames {
        IMPLEMENTATION(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY), THREADS("Threads"), SOURCE("Source"), CORRELATOR("Correlator");

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
     * Creates new model of PerfCake Receiver component.
     *
     * @param componentManager PerfCake component manager
     * @param docsService Documentation service
     */
    public ReceiverModel(ComponentManager componentManager, DocsService docsService) {
        super(componentManager, Receiver.class, docsService);
    }

    @Override
    protected void initializeSupportedProperties() {
        addSupportedProperties(
                new PropertyInfo(PropertyNames.IMPLEMENTATION.toString(), this, PropertyType.VALUE.getClazz(), null, 1, 1),
                new PropertyInfo(PropertyNames.THREADS.toString(), this, PropertyType.VALUE.getClazz(), null, 0, 1),
                new PropertyInfo(PropertyNames.SOURCE.toString(), this, PropertyType.VALUE.getClazz(), null, 0, 1),
                new PropertyInfo(PropertyNames.CORRELATOR.toString(), this, PropertyType.MODEL.getClazz(), null, 1, 1)
        );

    }
}
