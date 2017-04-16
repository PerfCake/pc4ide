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

package org.perfcake.ide.core.org.perfcake.ide.core.model.loader;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import org.perfcake.PerfCakeException;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.exception.ModelSerializationException;
import org.perfcake.ide.core.model.serialization.ModelLoader;
import org.perfcake.model.Scenario;

/**
 * Tests for {@link org.perfcake.ide.core.model.serialization.ModelLoader}.
 *
 * @author Jakub Knetl
 */
public class ModelLoaderTest {

    private ModelLoader loader = new ModelLoader();

    @Test
    public void testParsing() throws ModelConversionException, PerfCakeException, IOException, ModelSerializationException {

        String[] scenarios = new String[] {
                "bob.xml",
                "john.xml",
                "maria.xml"
        };
        for (String scenario : scenarios) {
            Path scenarioPath = Paths.get("src/test/resources/users/scenarios/" + scenario);
            try (InputStream inputStream = Files.newInputStream(scenarioPath)) {
                Scenario xmlScenario = loader.parse(inputStream);
                assertThat(xmlScenario, not(nullValue()));
            }
        }
    }
}
