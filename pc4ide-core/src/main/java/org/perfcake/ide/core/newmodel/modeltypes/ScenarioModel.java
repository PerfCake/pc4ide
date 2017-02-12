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
import org.perfcake.scenario.Scenario;

/**
 * Represents a PerfCake scenario model.
 * @author Jakub Knetl
 */
public class ScenarioModel extends AbstractModel {

    public enum PropertyNames {

        PROPERTIES("Properties"), GENERATOR("Generator"), SEQUENCES("Sequences"), SENDER("Sender"), RECEIVER("Receiver"),
        REPORTERS("Reporters"), MESSAGES("Messages"), VALIDATORS("Validators"), VALIDATION_ENABLED("Validation enabled"),
        VALIDATION_FAST_FORWARD("Validation fast forward");

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
     */
    public ScenarioModel(ComponentManager componentManager) {
        super(componentManager, Scenario.class);
    }

    @Override
    protected void initializeSupportedProperties() {
        addSupportedProperties(
                new PropertyType<>(PropertyNames.PROPERTIES.toString(), ModelType.KEY_VALUE, null, 0, -1),
                new PropertyType<>(PropertyNames.GENERATOR.toString(), ModelType.MODEL, null, 1, 1),
                new PropertyType<>(PropertyNames.SENDER.toString(), ModelType.MODEL, null, 1, 1),
                new PropertyType<>(PropertyNames.RECEIVER.toString(), ModelType.MODEL, null, 0, 1),
                new PropertyType<>(PropertyNames.REPORTERS.toString(), ModelType.MODEL, null, 0, -1),
                new PropertyType<>(PropertyNames.MESSAGES.toString(), ModelType.MODEL, null, 0, -1),
                new PropertyType<>(PropertyNames.VALIDATORS.toString(), ModelType.MODEL, null, 0, -1),
                new PropertyType<>(PropertyNames.VALIDATION_ENABLED.toString(), ModelType.VALUE, "true", 0, 1),
                new PropertyType<>(PropertyNames.VALIDATION_FAST_FORWARD.toString(), ModelType.VALUE, "false", 0, 1)
        );

    }
}
