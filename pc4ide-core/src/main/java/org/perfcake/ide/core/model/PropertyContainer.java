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

package org.perfcake.ide.core.model;

import java.util.Iterator;
import java.util.List;
import org.perfcake.ide.core.exception.PropertyLimitException;
import org.perfcake.ide.core.exception.UnsupportedPropertyException;

/**
 * Property container maintains collection of properties.
 *
 * @author Jakub Knetl
 */
public interface PropertyContainer extends Iterable<Property> {

    @Override
    Iterator<Property> iterator();

    /**
     * Adds  new property into the container.
     *
     * @param property property to be added
     * @throws PropertyLimitException       if you try to add a property which has maximum number of occurrences used already.
     * @throws UnsupportedPropertyException If the property is not supported by this container.
     */
    void addProperty(Property property) throws PropertyLimitException, UnsupportedPropertyException;

    /**
     * Removes a property from the container.
     *
     * @param property property To be added
     * @return true if the property was removed.
     * @throws PropertyLimitException if you try to remove property, which cannot be removed due to required minimum occurrences.
     */
    boolean removeProperty(Property property)
            throws PropertyLimitException;

    /**
     * Lists properties which are contained in this container.
     *
     * @return <em>Unmodifiable list</em> of the properties in this container.
     */
    List<Property> getProperties();

    /**
     * Determines whether this container is empty.
     *
     * @return True if there is no property in this container.
     */
    boolean isEmpty();

    /**
     * Determines number of properties in this container.
     *
     * @return number of properties of in this container.
     */
    int size();
}

