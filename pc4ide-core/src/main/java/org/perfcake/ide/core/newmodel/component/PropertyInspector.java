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

package org.perfcake.ide.core.newmodel.component;

import java.util.List;
import java.util.Map;

/**
 * Property inspector enables to find properties specific to implementation class of a PerfCake component.
 *
 * @author Jakub Knetl
 */
public interface PropertyInspector {

    /**
     * Dynamically detects properties (fields) of a implementation clazz of a PerfCake component.
     * @param implementation Clazz for which properties should be detected
     * @param api Abstract API of the component
     * @return Map of property name and its default value.
     */
    List<ImplementationField> getProperties(Class<?> implementation, Class<?> api);

}
