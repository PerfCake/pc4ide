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

package org.perfcake.ide.core.model.components;

import org.perfcake.ide.core.components.PerfCakeComponent;
import org.perfcake.ide.core.docs.DocsService;
import org.perfcake.ide.core.model.AbstractModel;
import org.perfcake.ide.core.model.PropertyInfo;
import org.perfcake.ide.core.model.PropertyType;
import org.perfcake.ide.core.model.properties.SimpleValue;
import org.perfcake.scenario.Scenario;

/**
 * Represents a PerfCake scenario model.
 *
 * @author Jakub Knetl
 */
public class ScenarioModel extends AbstractModel {

    public enum PropertyNames {

        PROPERTIES("Properties"), GENERATOR("Generator"), SEQUENCES("Sequences"), SENDER("Sender"), RECEIVER("Receiver"),
        REPORTERS("Reporters"), REPORTERS_PROPERTIES("Reporters properties"), MESSAGES("Messages"), VALIDATORS("Validators"),
        VALIDATION_ENABLED("Validation enabled"), VALIDATION_FAST_FORWARD("Validation fast forward");

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
     * Creates new model of PerfCake inspector.
     *
     * @param docsService Documentation service
     */
    public ScenarioModel(DocsService docsService) {
        super(PerfCakeComponent.SCENARIO, docsService);
    }

    @Override
    protected void initializeSupportedProperties() {
        addSupportedProperties(
                new PropertyInfo(PropertyNames.PROPERTIES.toString(), this, PropertyType.KEY_VALUE.getClazz(), null, 0, -1),
                new PropertyInfo(PropertyNames.GENERATOR.toString(), this, PropertyType.MODEL.getClazz(), null, 1, 1),
                new PropertyInfo(PropertyNames.SENDER.toString(), this, PropertyType.MODEL.getClazz(), null, 1, 1),
                new PropertyInfo(PropertyNames.RECEIVER.toString(), this, PropertyType.MODEL.getClazz(), null, 0, 1),
                new PropertyInfo(PropertyNames.REPORTERS.toString(), this, PropertyType.MODEL.getClazz(), null, 0, -1),
                new PropertyInfo(PropertyNames.REPORTERS_PROPERTIES.toString(), this, PropertyType.KEY_VALUE.getClazz(),
                        null, 0, -1),
                new PropertyInfo(PropertyNames.MESSAGES.toString(), this, PropertyType.MODEL.getClazz(), null, 0, -1),
                new PropertyInfo(PropertyNames.VALIDATORS.toString(), this, PropertyType.MODEL.getClazz(), null, 0, -1),
                new PropertyInfo(PropertyNames.VALIDATION_ENABLED.toString(), this, PropertyType.VALUE.getClazz(),
                        new SimpleValue("true"), 0, 1),
                new PropertyInfo(PropertyNames.VALIDATION_FAST_FORWARD.toString(), this, PropertyType.VALUE.getClazz(),
                        new SimpleValue("false"), 0, 1)
        );

    }
}