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
 * Model of PerfCake message.
 *
 * @author Jakub Knetl
 */
public class MessageModel extends AbstractModel {

    public enum PropertyNames {

        URI("URI"), CONTENT("Content"), MULTIPLICITY("Multiplicity"), HEADERS("Headers"),
        PROPERTIES("Properties"), VALIDATOR_REFS("Validator references");

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
    public MessageModel(DocsService docsService) {
        super(PerfCakeComponent.MESSAGE, docsService);
    }

    @Override
    protected void initializeSupportedProperties() {

        addSupportedProperties(
                PropertyInfo.createValueInfo(PropertyNames.URI.toString(), this, 0, 1),
                PropertyInfo.createValueInfo(PropertyNames.CONTENT.toString(), this, 0, 1),
                PropertyInfo.createValueInfo(PropertyNames.MULTIPLICITY.toString(), null, this, 0, 1,
                        DataType.INTEGER, new SimpleValue("1")),
                PropertyInfo.createKeyValueInfo(PropertyNames.HEADERS.toString(), null, this, 0, -1,
                        DataType.STRING, DataType.STRING, null),
                PropertyInfo.createValueInfo(PropertyNames.VALIDATOR_REFS.toString(), this, 0, -1),
                PropertyInfo.createKeyValueInfo(PropertyNames.PROPERTIES.toString(), this, 0, -1)
        );
    }

}
