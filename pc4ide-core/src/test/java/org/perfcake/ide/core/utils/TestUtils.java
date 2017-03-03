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

package org.perfcake.ide.core.utils;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.junit.Test;
import org.perfcake.ide.core.docs.DocsServiceTest;

/**
 * Contains useful methods for the tests.
 * @author Jakub Knetl
 */
public class TestUtils {

    private TestUtils() {}

    /**
     * Loads Properties with PerfCake javadocs.
     * @return Properties with perfcake javadoc.
     * @throws IOException when IO error occurs while loading properties from file.
     */
    public static Properties loadJavadocProperties() throws IOException {
        Properties javadoc = new Properties();
        Path javadocPath = Paths.get(DocsServiceTest.PERFCAKE_COMMENT_PROPERTIES);
        assertTrue("File with javadoc does not exists.", Files.exists(javadocPath));
        InputStream inStream = Files.newInputStream(javadocPath);
        javadoc.load(inStream);
        return javadoc;
    }
}
