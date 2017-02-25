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

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an catalogue of PerfCake components on the classpath.
 *
 * @author jknetl
 */
public class ReflectionComponentCatalogue implements ComponentCatalogue {

    static final Logger logger = LoggerFactory.getLogger(ReflectionComponentCatalogue.class);

    /**
     * Map of components type and their implementations.
     */
    private Map<PerfCakeComponent, List<String>> components;

    /**
     * List of additional packages which will be scanned.
     */
    private Set<String> additionalPackages;

    /**
     * Creates a new catalogue. It also triggers scanning for components in given packages. Therefore, creating a new
     * {@link ReflectionComponentCatalogue} is costly operation and should not be called often.
     *
     * @param additionalPackages additional packages where catalogue will look for components.
     */
    public ReflectionComponentCatalogue(String... additionalPackages) {
        this.additionalPackages = new HashSet<String>(Arrays.asList(additionalPackages));
        components = new HashMap<>();
        update();
    }

    @Override
    public void update() {
        final Reflections reflections = createReflections();
        for (final PerfCakeComponent componentApi : PerfCakeComponent.values()) {
            logger.debug("Scanning for perfcake components in packages {}.", String.join(",", additionalPackages));
            List<String> list = new ArrayList<>();
            for (Class<?> subType : reflections.getSubTypesOf(componentApi.getApi())) {
                if (!subType.isInterface() && !Modifier.isAbstract(subType.getModifiers())) {
                    String name;
                    if (subType.getCanonicalName().startsWith(componentApi.getDefaultPackage())) {
                        name = subType.getSimpleName();
                    } else {
                        name = subType.getCanonicalName();
                    }

                    logger.trace("Component found. Type: {}, Name: {}", componentApi.name(), name);
                    list.add(name);
                }
            }

            components.put(componentApi, list);
        }
    }

    @Override
    public List<String> list(PerfCakeComponent component) {
        return Collections.unmodifiableList(components.get(component));

    }

    @Override
    public void addPackage(String... packages) {
        if (packages != null) {
            for (String p : packages) {
                additionalPackages.add(p);
            }
        }
    }

    @Override
    public void removePackage(String... packages) {
        if (packages != null) {
            for (String p : packages) {
                additionalPackages.remove(p);
            }
        }
    }

    /**
     * Creates and configure instance of {@link Reflections} which will be used for scanning.
     *
     * @return Reflections instance
     */
    protected Reflections createReflections() {

        Set<String> allPackages = new HashSet<>(Arrays.asList(DEFAULT_PACKAGES));
        allPackages.addAll(additionalPackages);
        Reflections reflections = new Reflections(allPackages);

        return reflections;
    }

}
