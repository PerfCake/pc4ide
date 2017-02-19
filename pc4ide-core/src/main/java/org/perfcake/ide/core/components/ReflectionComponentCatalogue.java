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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.perfcake.message.generator.MessageGenerator;
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

    public static final String[] PACKAGES_WITH_COMPONENTS = new String[] {"org.perfcake"};

    /**
     * Map of componets type and their implementations.
     */
    private Map<PerfCakeComponents, List<String>> components;

    /**
     * List of packages prefixes where to search for the components.
     */
    private List<String> packagesToScan;

    /**
     * Creates a inspector manager.
     *
     * @param packagesToScan packages to scan for components.
     */
    public ReflectionComponentCatalogue(List<String> packagesToScan) {
        this.packagesToScan = packagesToScan;
        components = new HashMap<>();
        update();
    }

    @Override
    public void update() {
        for (final PerfCakeComponents componentApi : PerfCakeComponents.values()) {
            final Reflections reflections = createReflections();
            Set<Class<? extends MessageGenerator>> subTypesOf = reflections.getSubTypesOf(MessageGenerator.class);

            List<String> list = new ArrayList<>();
            for (Class<?> subType : reflections.getSubTypesOf(componentApi.getApi())) {
                if (!subType.isInterface() && !Modifier.isAbstract(subType.getModifiers())) {
                    list.add(subType.getCanonicalName());
                }
            }

            components.put(componentApi, list);
        }
    }

    @Override
    public List<String> list(PerfCakeComponents component) {
        return Collections.unmodifiableList(components.get(component));

    }

    /**
     * Creates and configure instance of {@link Reflections} which will be used for scanning.
     *
     * @return Reflections instance
     */
    protected Reflections createReflections() {
        return new Reflections(packagesToScan);
    }

}
