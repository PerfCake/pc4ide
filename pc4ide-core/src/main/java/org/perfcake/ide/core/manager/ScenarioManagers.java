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

package org.perfcake.ide.core.manager;

import java.nio.file.Path;
import org.perfcake.ide.core.model.serialization.DslModelLoader;
import org.perfcake.ide.core.model.serialization.DslModelWriter;
import org.perfcake.ide.core.model.serialization.XmlModelLoader;
import org.perfcake.ide.core.model.serialization.XmlModelWriter;

/**
 * Non instantiable class with static factory methods for creating scenario managers.
 *
 * @author Jakub Knetl
 */
public class ScenarioManagers {

    private ScenarioManagers() {
    }

    /**
     * Creates scenario manager. It decides whether dsl or xml scenario manager is loaded based on file extension.
     *
     * @param path to the managed file. File should have either ".xml" or ".dsl" suffix
     * @return Scenario manager
     */
    public static ScenarioManager createScenarioManager(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be empty.");
        }
        if (!path.toString().endsWith(".dsl") && !path.toString().endsWith(".xml")) {
            throw new IllegalArgumentException("Unrecognized file suffix: " + path.getFileName().toString());
        }
        if (path.toString().endsWith(".xml")) {
            return createXmlManager(path);
        }
        if (path.toString().endsWith(".dsl")) {
            return createDslManager(path);
        }

        return null;

    }

    /**
     * Creates new scenario manager which manages file in XML format.
     *
     * @param path path to scenario location
     * @return scenario manager of XML file
     * @throws NullPointerException if path is null.
     */
    public static ScenarioManager createXmlManager(Path path) throws NullPointerException {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be empty.");
        }
        return new ScenarioManagerImpl(path, new XmlModelWriter(), new XmlModelLoader());

    }

    /**
     * Creates new scenario manager which manages file in DSL format.
     *
     * @param path path to scenario location
     * @return scenario manager of DSL file
     * @throws NullPointerException if path is null.
     */
    public static ScenarioManager createDslManager(Path path) throws NullPointerException {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be empty.");
        }
        return new ScenarioManagerImpl(path, new DslModelWriter(path.getFileName().toString()), new DslModelLoader());
    }
}
