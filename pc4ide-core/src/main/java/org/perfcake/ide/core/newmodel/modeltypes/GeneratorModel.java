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

import org.perfcake.ide.core.docs.DocsService;
import org.perfcake.ide.core.newmodel.AbstractModel;
import org.perfcake.ide.core.newmodel.PropertyInfo;
import org.perfcake.ide.core.newmodel.PropertyType;
import org.perfcake.ide.core.newmodel.simple.SimpleValue;
import org.perfcake.message.generator.MessageGenerator;

/**
 * Represents model of a generator.
 *
 * @author Jakub Knetl
 */
public class GeneratorModel extends AbstractModel {

    public enum PropertyNames {
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
     * Creates new model of PerfCake Generator component.
     *
     * @param docsService Documentation service
     */
    public GeneratorModel(DocsService docsService) {
        super(MessageGenerator.class, docsService);
    }

    @Override
    protected void initializeSupportedProperties() {

        addSupportedProperties(
                new PropertyInfo(PropertyNames.RUN.toString(), this, PropertyType.KEY_VALUE.getClazz(), null, 1, 1),
                new PropertyInfo(PropertyNames.IMPLEMENTATION.toString(), this, PropertyType.VALUE.getClazz(), null, 1, 1),
                new PropertyInfo(PropertyNames.THREADS.toString(), this, PropertyType.VALUE.getClazz(), new SimpleValue("1"), 0, 1)
        );
    }
}
