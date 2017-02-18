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

package org.perfcake.ide.core.docs;

/**
 * <p>DocsService allows to obtain documentation for PerfCake components and other PerfCake objects.
 * It allows you to parse:</p>
 * <ol>
 *     <li>documentation of any PerfCake class or interface</li>
 *     <li>documentation of any field of PerfCake class or interface</li>
 * </ol>
 *
 * <p>Fields may be detected by their corresponding set method in addition to actuall fields of a class. In other words, if class contains
 * method setX(), but it has no field X, this service will return documentation of x getter or setter method.</p>
 *
 * @author Jakub Knetl
 */
public interface DocsService {

    /**
     * Finds a documentation of PerfCake type.
     * @param type class representing perfcake type
     * @return Documentation of the type, or null, if no documentation of this type can be found.
     */
    String getDocs(Class<?> type);

    /**
     * Finds a documentation of PerfCake type's field. If no field with this name exists in the type, but there is setter or getter
     * for that field, then this method can return documentation of the getter or setter.
     *
     * @param type class representing perfcake type
     * @param fieldName name of the field
     * @return Documentation of the type's field, or null, if no documentation of this type's field can be found.
     */
    String getFieldDocs(Class<?> type, String fieldName);
}
