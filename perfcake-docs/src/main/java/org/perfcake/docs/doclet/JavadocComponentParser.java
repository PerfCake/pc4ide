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

package org.perfcake.docs.doclet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.perfcake.docs.utils.StringUtils;

/**
 * JavadocComponentParser scans sources of standard PerfCake components and parses their documentation from
 * javadoc sources.
 */
public class JavadocComponentParser {

    /**
     * Properties file.
     */
    public static final String JAVADOC_PROPERTIES_FILE = "perfcake-javadoc.properties";

    private static final String JAVADOC_PROPERTIES_COMMENT = "Javadoc for the perfcake components fields.";

    /**
     * Parses perfcake components javadoc.
     *
     * @param root Root of the program structure.
     * @return true if parsing ended successfully
     */
    public static boolean start(RootDoc root) {

        boolean processedSuccessfully = false;
        final ClassDoc[] classes = root.classes();

        final Properties properties = new Properties();

        for (final ClassDoc classDoc : classes) {
            if (isPerfcakeComponent(classDoc)) {

                if (classDoc.commentText() != null && !classDoc.commentText().isEmpty()) {
                    properties.setProperty(classDoc.qualifiedName(), classDoc.commentText());
                }
                for (final FieldDoc field : classDoc.fields()) {
                    if (field.commentText() != null && !field.commentText().isEmpty()) {
                        properties.setProperty(field.qualifiedName(), field.commentText());
                    }
                }

                /* if the class is abstract then we want to derive field documentation using set methods
                 * defined in the interface */
                if (classDoc.isInterface()) {
                    for (final MethodDoc method : classDoc.methods()) {
                        if (method.name().startsWith("set")) {
                            final String fieldName = StringUtils.firstToLowerCase(method.name().substring(3));
                            final String documentation = method.commentText();
                            properties.setProperty(classDoc.qualifiedName() + "." + fieldName, documentation);
                        }
                    }
                }
            }
        }

        processedSuccessfully = true;

        try {
            System.out.println("Storing properties to file: " + JAVADOC_PROPERTIES_FILE);
            properties.store(new FileOutputStream(JAVADOC_PROPERTIES_FILE), JAVADOC_PROPERTIES_COMMENT);
        } catch (final FileNotFoundException e) {
            logException(e);

        } catch (final IOException e) {
            logException(e);
        }
        return processedSuccessfully;

    }

    private static boolean isPerfcakeComponent(ClassDoc classDoc) {

        boolean isComponent = false;
        /* it could be useful to fine grane this filtering so that only required javadoc are parsed,
         * but it would require to create a list of types that should be parsed. The list will need to
         * be updated with perfcake changes so until this is a performance or javadoc size issue such coarse
         * grined filtering is good enough.
         */
        if (classDoc != null && classDoc.qualifiedName().startsWith("org.perfcake")) {
            isComponent = true;
        }

        return isComponent;
    }

    private static void logException(Throwable e) {
        System.out.println("Exception: " + e.getClass().getName());
        System.out.println("Message: " + e.getMessage());
        e.printStackTrace();
    }
}
