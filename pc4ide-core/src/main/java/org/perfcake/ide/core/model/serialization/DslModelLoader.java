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
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.perfcake.ide.core.Pc4ideConstants;
import org.perfcake.ide.core.docs.DocsServiceImpl;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.exception.ModelSerializationException;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.converter.dsl.DslScenarioUtil;
import org.perfcake.ide.core.model.converter.xml.XmlConverter;
import org.perfcake.model.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dsl model loader is able to load pc4ide model from PerfCake dsl scenario definition.
 *
 * @author Jakub Knetl
 */
public class DslModelLoader implements ModelLoader {

    static final Logger logger = LoggerFactory.getLogger(DslModelLoader.class);

    @Override
    public ScenarioModel loadModel(InputStream inputStream) throws ModelSerializationException, ModelConversionException {

        ScenarioModel model = null;
        StringWriter stringWriter = new StringWriter();
        try {
            IOUtils.copy(inputStream, stringWriter, StandardCharsets.UTF_8);
            String dslScenario = stringWriter.toString();
            Scenario xmlModel = DslScenarioUtil.getModelFrom(dslScenario);
            Properties javadocProperties = new Properties();

            try {
                javadocProperties.load(this.getClass().getResourceAsStream(Pc4ideConstants.PERFCAKE_COMMENT_PROPERTIES));
            } catch (IOException e) {
                throw new ModelConversionException("Cannot load javadoc.", e);
            }
            XmlConverter converter = new XmlConverter(new DocsServiceImpl(javadocProperties));
            model = converter.convertToPc4ideModel(xmlModel);
        } catch (IOException e) {
            throw new ModelSerializationException(e);
        }
        return model;
    }
}
