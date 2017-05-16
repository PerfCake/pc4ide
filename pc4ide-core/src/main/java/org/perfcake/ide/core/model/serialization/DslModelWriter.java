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

package org.perfcake.ide.core.model.serialization;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.perfcake.ide.core.Pc4ideConstants;
import org.perfcake.ide.core.docs.DocsServiceImpl;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.exception.ModelSerializationException;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.converter.dsl.DslScenarioUtil;
import org.perfcake.ide.core.model.converter.xml.SerializationPostProcessor;
import org.perfcake.ide.core.model.converter.xml.XmlConverter;
import org.perfcake.model.Scenario;

/**
 * DslModelWriter is able to write pc4ide model in PerfCake scenario DSL format to an outputStream.
 *
 * @author Jakub Knetl
 */
public class DslModelWriter implements ModelWriter {

    private String scenarioName = "";

    public DslModelWriter(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    @Override
    public void writeScenario(ScenarioModel scenarioModel, OutputStream outputStream) throws ModelConversionException,
            ModelSerializationException {

        Properties javadocProperties = new Properties();
        try {
            javadocProperties.load(this.getClass().getResourceAsStream(Pc4ideConstants.PERFCAKE_COMMENT_PROPERTIES));
        } catch (IOException e) {
            throw new ModelConversionException("Cannot load javadoc.", e);
        }
        XmlConverter converter = new XmlConverter(new DocsServiceImpl(javadocProperties));

        //TODO(jknetl): apply post processors after creating model!
        ArrayList<SerializationPostProcessor> postProcessors = new ArrayList<>();
        Scenario xmlModel = converter.convertToXmlModel(scenarioModel, postProcessors);

        String dslScenario = DslScenarioUtil.getDslScenarioFrom(xmlModel, scenarioName);

        try {
            IOUtils.write(dslScenario, outputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ModelSerializationException("Cannot write dsl scenario", e);
        }
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public DslModelWriter setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }
}
