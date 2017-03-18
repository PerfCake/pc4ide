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

package org.perfcake.ide.core.model.factory;

import org.perfcake.ide.core.components.PerfCakeComponent;
import org.perfcake.ide.core.model.Model;

/**
 * Factory for model objects.
 *
 * @author Jakub Knetl
 */
public interface ModelFactory {

    /**
     * Creates model for particular PerfCake component
     * @param component type of PerfCake component.
     * @return Model representing given component.
     */
    Model createModel(PerfCakeComponent component);
}
