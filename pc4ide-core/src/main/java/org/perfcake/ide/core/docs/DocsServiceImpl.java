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

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Docs service implementation. It uses properties files with parsed javadocs from the PerfCake to
 * get documentation.
 * @author Jakub Knetl
 */
public class DocsServiceImpl implements DocsService {

    private Properties javadoc;

    public DocsServiceImpl(Properties javadoc) {
        this.javadoc = javadoc;
    }

    @Override
    public String getDocs(Class<?> type) {
        return javadoc.getProperty(type.getCanonicalName());
    }

    @Override
    public String getFieldDocs(Class<?> type, String fieldName) {
        Class<?> tested = type;
        String fieldDocs = null;
        while (tested != null) {
            try {
                Field f = tested.getDeclaredField(fieldName);
                fieldDocs = javadoc.getProperty(String.format("%s.%s", tested.getCanonicalName(), fieldName));
                break;
            } catch (NoSuchFieldException e) {
                tested = tested.getSuperclass();
            }
        }

        return fieldDocs;
    }
}
