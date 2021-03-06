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

package org.perfcake.ide.core.components;

/**
 * Component serialization enables to get component clazz from classpath.
 *
 * @author Jakub Knetl
 */
public interface ComponentLoader {

    /**
     * Loads a PerfCake component class by its name. If name is not FQDN, then default packages for given component type are used
     * as defined in {@link org.perfcake.scenario.ScenarioFactory}.
     *
     * @param name      Name of the component.
     * @param component type of the PerfCake component.
     * @return Class of the component implementation or null if no such component can be found.
     */
    Class<?> loadComponent(String name, PerfCakeComponent component);
}
