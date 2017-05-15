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

import java.nio.file.Path;
import java.util.List;
import org.perfcake.PerfCakeException;

/**
 * ComponentCatalogue enables to obtain a list of PerfCake component implementations.
 *
 * @author Jakub Knetl
 */
public interface ComponentCatalogue {

    /**
     * List of the default packages, that are scanned by default for PerfCake components. These packages will be always scanned.
     */
    String[] DEFAULT_PACKAGES = new String[] {"org.perfcake", "."};


    /**
     * Updates a catalogue in order to find new implementations. This method may take a while to finish.
     */
    void update();

    /**
     * Adds packages which should be scanned for components.
     * This method only modifies collection of packages, but it does not trigger actual scanning. In order
     * to discover new components you need to call {@link #update()} method.
     *
     * @param packages additional packages.
     */
    void addPackage(String... packages);

    /**
     * Removes packages which should not be scanned for components anymore.
     * This method only modifies collection of packages, but it does not trigger actual scanning. In order
     * to discover new components you need to call {@link #update()} method.
     *
     * @param packages packages which should not be scanned anymore.
     */
    void removePackage(String... packages);

    /**
     * Lists a PerfCake component implementations for a given inspector type.
     *
     * @param component PerfCake comonent type whose implementations should be returned.
     * @return Unmodifiable List of implementation classes. If no implementation is found, then empty list is returned.
     */
    List<String> list(PerfCakeComponent component);

    /**
     * Adds an external jar to the library.
     *
     * @param jar path to a jar package with.
     * @throws PerfCakeException when it cannot add the jar.
     */
    void addSoftwareLibrary(Path jar) throws PerfCakeException;
}
