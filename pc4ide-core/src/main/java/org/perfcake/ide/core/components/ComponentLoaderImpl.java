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

import org.perfcake.ide.core.exception.ImplementationNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component loader tries to load component by its fully qualified domain name (FQDN). If only name of the component is used, then
 * it tries to load component from the default package.
 *
 * @author Jakub Knetl
 */
public class ComponentLoaderImpl implements ComponentLoader {

    static final Logger logger = LoggerFactory.getLogger(ComponentLoaderImpl.class);

    @Override
    public Class<?> loadComponent(String name, PerfCakeComponent componentType) throws ImplementationNotFoundException {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null.");
        }
        if (componentType == null) {
            throw new IllegalArgumentException("component type cannot be null.");
        }
        Class<?> component = null;
        String fqdn = name;
        if (!name.contains(".")) {
            logger.debug("Adding default package {} for the component {}", componentType.getDefaultPackage(), name);
            fqdn = String.format("%s.%s", componentType.getDefaultPackage(), name);
        }

        try {
            logger.debug("Trying to load component {}", fqdn);
            component = Class.forName(fqdn);
        } catch (ClassNotFoundException e) {
            throw new ImplementationNotFoundException(String.format("Cannot find implementation class %s.", fqdn), e);
        }

        return component;
    }
}
