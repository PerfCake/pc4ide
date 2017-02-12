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
import org.perfcake.ide.core.newmodel.PropertyInfo;
import org.perfcake.ide.core.newmodel.PropertyType;
import org.perfcake.ide.core.newmodel.simple.SimpleValue;
import org.perfcake.reporting.destination.Destination;

/**
 * Represent model of a Destination PerfCake component.
 *
 * @author Jakub Knetl
 */
public class DestinationModel extends AbstractModel {

    public enum PropertyNames {
        IMPLEMENTATION(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY), ENABLED("enabled"), PERIOD("Periods");

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
     * Creates new model of PerfCake Destination component.
     *
     * @param componentManager PerfCake component manager
     */
    public DestinationModel(ComponentManager componentManager) {
        super(componentManager, Destination.class);
    }

    @Override
    protected void initializeSupportedProperties() {

        addSupportedProperties(
                new PropertyInfo(PropertyNames.IMPLEMENTATION.toString(), PropertyType.VALUE.getClazz(), null, 1 , 1),
                new PropertyInfo(PropertyNames.ENABLED.toString(), PropertyType.VALUE.getClazz(), new SimpleValue("true"), 0, 1),
                new PropertyInfo(PropertyNames.PERIOD.toString(), PropertyType.KEY_VALUE.getClazz(), null, 0, -1)
        );

    }
}
