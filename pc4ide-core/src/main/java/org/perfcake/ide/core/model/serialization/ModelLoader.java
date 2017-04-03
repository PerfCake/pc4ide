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
import java.io.UnsupportedEncodingException;
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
import org.perfcake.ide.core.docs.DocsServiceImpl;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.converter.XmlConverter;
import org.perfcake.model.Scenario;
import org.perfcake.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class ModelLoader {

    static final Logger logger = LoggerFactory.getLogger(ModelLoader.class);
    public static final String PERFCAKE_COMMENT_PROPERTIES = "/perfcake-comment.properties";

    /**
     * Does the parsing itself by using JAXB.
     *
     * @param scenarioLocation url to scenario file.
     * @return Parsed JAXB scenario model.
     * @throws PerfCakeException If XML is not valid or cannot be successfully parsed.
     */
    public Scenario parse(URL scenarioLocation) throws PerfCakeException {
        try {
            final Source scenarioXml = new StreamSource(scenarioLocation.openStream());
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
            throw new PerfCakeException("Cannot validate scenario configuration. PerfCake installation seems broken. ", e);
        } catch (final JAXBException e) {
            throw new PerfCakeException("Cannot parse scenario configuration: ", e);
        } catch (final MalformedURLException e) {
            throw new PerfCakeException("Cannot read scenario schema to validate it: ", e);
        } catch (final UnsupportedEncodingException e) {
            throw new PerfCakeException("set encoding is not supported: ", e);
        } catch (final IOException e) {
            throw new PerfCakeException("Wrong scenario url.", e);
        }
    }

    /**
     * Loads a model of given scenario.
     *
     * @param url URL of scenario
     * @return Scenario model
     * @throws PerfCakeException        when model cannot be loaded
     * @throws ModelConversionException when PerfCake model can't be converted to pc4ide model.
     */
    public ScenarioModel loadModel(URL url) throws PerfCakeException, ModelConversionException {
        final Scenario scenario = parse(url);
        Properties javadocProperties = new Properties();
        try {
            javadocProperties.load(this.getClass().getResourceAsStream(PERFCAKE_COMMENT_PROPERTIES));
        } catch (IOException e) {
            throw new ModelConversionException("Cannot load javadoc.", e);
        }
        XmlConverter converter = new XmlConverter(new DocsServiceImpl(javadocProperties));
        final ScenarioModel model = converter.convertToPc4ideModel(scenario);
        return model;
    }

}
