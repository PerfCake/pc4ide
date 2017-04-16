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
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.perfcake.PerfCakeConst;
import org.perfcake.PerfCakeException;
import org.perfcake.ide.core.Pc4ideConstants;
import org.perfcake.ide.core.docs.DocsServiceImpl;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.exception.ModelSerializationException;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.converter.SerializationPostProcessor;
import org.perfcake.ide.core.model.converter.XmlConverter;
import org.perfcake.model.Scenario;
import org.perfcake.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * Model Writer writes scenario model into file.
 *
 * @author Jakub Knetl
 */
public class ModelWriter {

    static final Logger logger = LoggerFactory.getLogger(ModelWriter.class);

    private XmlConverter converter;

    /**
     * Creates new Model writer.
     */
    public ModelWriter() {
        Properties javadocProperties = new Properties();
        try {
            javadocProperties.load(this.getClass().getResourceAsStream(Pc4ideConstants.PERFCAKE_COMMENT_PROPERTIES));
        } catch (IOException e) {
            logger.warn("Cannot load javadoc for loader.", e);
        }
        converter = new XmlConverter(new DocsServiceImpl(javadocProperties));
    }

    /**
     * Writes scenario to the file.
     *
     * @param scenarioModel model of a scenario
     * @param outputStream  Output stream where scenario will be written.
     * @throws ModelConversionException    when model cannot be converted to PerfCake XML model
     * @throws ModelSerializationException when model cannot be serialized
     */
    public void writeScenario(ScenarioModel scenarioModel, OutputStream outputStream) throws ModelConversionException,
            ModelSerializationException {
        ArrayList<SerializationPostProcessor> postProcessors = new ArrayList<>();
        Scenario scenario = converter.convertToXmlModel(scenarioModel, postProcessors);

        writeScenario(scenario, outputStream, postProcessors);
    }

    private void writeScenario(Scenario scenario, OutputStream outputStream, List<SerializationPostProcessor> postProcessors)
            throws ModelSerializationException {
        try {
            JAXBContext context = JAXBContext.newInstance(org.perfcake.model.Scenario.class);
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            final String schemaFileName = "perfcake-scenario-" + PerfCakeConst.XSD_SCHEMA_VERSION + ".xsd";
            final URL backupUrl = new URL("http://schema.perfcake.org/" + schemaFileName);

            URL scenarioXsdUrl = Utils.getResourceAsUrl("/schemas/" + schemaFileName);

            try {
                final InputStream test = scenarioXsdUrl.openStream();
                //noinspection ResultOfMethodCallIgnored
                test.read(); // there always is a byte
                test.close(); // we do not need finally for this as we could not have failed
            } catch (final IOException e) {
                scenarioXsdUrl = backupUrl; // backup taken from the web
            }
            //obtain url to schema definition, which is located somewhere in PerfCakeBundle
            Schema schema = schemaFactory.newSchema(scenarioXsdUrl);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setSchema(schema);

            //add line breaks and indentation into output
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(scenario, outputStream);
        } catch (JAXBException e) {
            throw new ModelSerializationException("JAXB error when saving scenario", e);
        } catch (MalformedURLException e) {
            throw new ModelSerializationException("Malformed URL, when saving scenario", e);
        } catch (SAXException e) {
            throw new ModelSerializationException("Parsing error during scenario saving.", e);
        } catch (PerfCakeException e) {
            throw new ModelSerializationException("Cannot get schema URL resource", e);
        }

    }
}
