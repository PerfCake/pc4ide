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

/**
 * Non instantiable class with static factory methods for creating scenario managers.
 *
 * @author Jakub Knetl
 */
public class ScenarioManagers {

    private ScenarioManagers() {
    }

    /**
     * Creates new scenario manager which manages file in XML format.
     *
     * @return scenario manager of XML file
     * @throws NullPointerException if path is null.
     */
    public static ScenarioManager createXmlManager(Path path) throws NullPointerException {
        if (path == null) {
            throw new NullPointerException("Path cannot be empty.");
        }
        return new XmlScenarioManager(path);

    }

    /**
     * Creates new scenario manager which manages file in DSL format.
     *
     * @return scenario manager of DSL file
     * @throws NullPointerException if path is null.
     */
    public static ScenarioManager createDslManager(Path path) throws NullPointerException {
        if (path == null) {
            throw new NullPointerException("Path cannot be empty.");
        }
        //TODO(jknetl): implement this
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
