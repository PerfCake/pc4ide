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

package org.perfcake.ide.editor.comparator;

import java.util.Comparator;
import java.util.Map;

/**
 * Implementation priority comparator is able to compare objects based on priorities of its implementing classes. It uses internal priority
 * map. The object with lower priority is less than a object with higher priority. Two objects with the same priority are same.
 * If a view has not defined a priority, than  it has largest priority.
 *
 * @author Jakub Knetl
 */
public abstract class ImplementationPriorityComparator<T> implements Comparator<T> {

    private Map<Class<? extends T>, Integer> priorityMap;

    public ImplementationPriorityComparator() {
        priorityMap = initializePriorities();
    }

    @Override
    public int compare(T o1, T o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }

        if (o1 == null) {
            return 1;
        }

        if (o2 == null) {
            return -1;
        }

        int priority1 = getPriority((Class<? extends T>) o1.getClass());
        int priority2 = getPriority((Class<? extends T>) o1.getClass());

        if (priority1 == priority2) {
            return 0;
        }

        if (priority1 < priority2) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * This method creates map of priorities.
     * @return Map of priorities.
     */
    protected abstract Map<Class<? extends T>, Integer> initializePriorities();

    protected Map<Class<? extends T>, Integer> getPriorities() {
        return priorityMap;
    }

    /**
     * Gets priority of an type. If instance has no priority set, then Integer.MAX_VALUE is returned.
     *
     * @param clazz clazz which represents model type
     * @return a priority of type which represents model.
     */
    public int getPriority(Class<? extends T> clazz) {
        return (priorityMap.get(clazz) == null ? Integer.MAX_VALUE : priorityMap.get(clazz));
    }

}
