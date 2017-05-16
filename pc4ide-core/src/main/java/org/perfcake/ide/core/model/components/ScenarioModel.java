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
import org.perfcake.ide.core.model.properties.DataType;
import org.perfcake.ide.core.model.properties.SimpleValue;

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
        this.setPropertyInfo(PropertyInfo.createModelInfo("Scenario", this, PerfCakeComponent.SCENARIO, 1, 1));
    }

    @Override
    protected void initializeSupportedProperties() {
        addSupportedProperties(
                PropertyInfo.createKeyValueInfo(PropertyNames.PROPERTIES.toString(), this, 0, -1),
                PropertyInfo.createModelInfo(PropertyNames.GENERATOR.toString(), this, PerfCakeComponent.GENERATOR, 1, 1),
                PropertyInfo.createModelInfo(PropertyNames.SENDER.toString(), this, PerfCakeComponent.SENDER, 1, 1),
                PropertyInfo.createModelInfo(PropertyNames.RECEIVER.toString(), this, PerfCakeComponent.RECEIVER, 0, 1),
                PropertyInfo.createModelInfo(PropertyNames.SEQUENCES.toString(), this, PerfCakeComponent.SEQUENCE, 0, -1),
                PropertyInfo.createModelInfo(PropertyNames.REPORTERS.toString(), this, PerfCakeComponent.REPORTER, 0, -1),
                PropertyInfo.createKeyValueInfo(PropertyNames.REPORTERS_PROPERTIES.toString(), this, 0, -1),
                PropertyInfo.createModelInfo(PropertyNames.MESSAGES.toString(), this, PerfCakeComponent.MESSAGE, 0, -1),
                PropertyInfo.createModelInfo(PropertyNames.VALIDATORS.toString(), this, PerfCakeComponent.VALIDATOR, 0, -1),
                PropertyInfo.createValueInfo(PropertyNames.VALIDATION_ENABLED.toString(), null, this, 0, 1,
                        DataType.BOOLEAN, new SimpleValue("true")),
                PropertyInfo.createValueInfo(PropertyNames.VALIDATION_FAST_FORWARD.toString(), null, this, 0, 1,
                        DataType.BOOLEAN, new SimpleValue("false"))
        );

    }
}
