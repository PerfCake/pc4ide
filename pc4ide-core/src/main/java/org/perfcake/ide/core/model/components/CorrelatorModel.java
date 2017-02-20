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
import org.perfcake.message.correlator.Correlator;

/**
 * Represents model of a Correlator.
 * @author Jakub Knetl
 */
public class CorrelatorModel extends AbstractModel {

    public enum PropertyNames {
        IMPLEMENTATION(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY);

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
     * Creates new model of PerfCake Correlator inspector.
     *
     * @param docsService Documentation service
     */
    public CorrelatorModel(DocsService docsService) {
        super(PerfCakeComponent.CORRELATOR, docsService);
    }

    @Override
    protected void initializeSupportedProperties() {

        addSupportedProperties(
                new PropertyInfo(PropertyNames.IMPLEMENTATION.toString(), this, PropertyType.VALUE.getClazz(), null, 1, 1)
        );
    }
}
