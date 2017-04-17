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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.perfcake.PerfCakeConst;
import org.perfcake.PerfCakeException;
import org.perfcake.ide.core.Pc4ideConstants;
import org.perfcake.ide.core.docs.DocsServiceImpl;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.exception.ModelSerializationException;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.converter.xml.XmlConverter;
import org.perfcake.model.Scenario;
import org.perfcake.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class ModelLoader {

    static final Logger logger = LoggerFactory.getLogger(ModelLoader.class);

    /**
     * Reads scenario from input stream and parses it into PerfCake scenario model using JAXB.
     *
     * @param inputStream input stream with scenario definition
     * @return Parsed JAXB scenario model.
     * @throws ModelSerializationException if file cannot be parsed.
     */
    public Scenario parse(InputStream inputStream) throws ModelSerializationException {
        try {
            final Source scenarioXml = new StreamSource(inputStream);
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

            final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            final Schema schema = schemaFactory.newSchema(scenarioXsdUrl);

            final JAXBContext context = JAXBContext.newInstance(org.perfcake.model.Scenario.class);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setSchema(schema);
            return (org.perfcake.model.Scenario) unmarshaller.unmarshal(scenarioXml);
        } catch (final SAXException e) {
            throw new ModelSerializationException("Cannot validate scenario configuration. PerfCake installation seems broken. ", e);
        } catch (final JAXBException e) {
            throw new ModelSerializationException("Cannot parse scenario configuration: ", e);
        } catch (final MalformedURLException e) {
            throw new ModelSerializationException("Cannot read scenario schema to validate it: ", e);
        } catch (final IOException e) {
            throw new ModelSerializationException("Wrong scenario url.", e);
        } catch (PerfCakeException e) {
            throw new ModelSerializationException("Cannot find scenario XSD file.", e);
        }
    }

    /**
     * Loads a model of given scenario.
     *
     * @param inputStream input stream with scenario definition
     * @return Scenario model
     * @throws ModelSerializationException when model cannot be deserialized.
     * @throws ModelConversionException    when PerfCake model can't be converted to pc4ide model.
     */
    public ScenarioModel loadModel(InputStream inputStream) throws ModelSerializationException, ModelConversionException {
        final Scenario scenario = parse(inputStream);
        Properties javadocProperties = new Properties();
        try {
            javadocProperties.load(this.getClass().getResourceAsStream(Pc4ideConstants.PERFCAKE_COMMENT_PROPERTIES));
        } catch (IOException e) {
            throw new ModelConversionException("Cannot load javadoc.", e);
        }
        XmlConverter converter = new XmlConverter(new DocsServiceImpl(javadocProperties));
        final ScenarioModel model = converter.convertToPc4ideModel(scenario);
        return model;
    }

}
