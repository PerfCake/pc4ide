package org.perfcake.ide.core.model;

import org.perfcake.PerfCakeConst;
import org.perfcake.PerfCakeException;
import org.perfcake.model.Scenario;
import org.perfcake.util.Utils;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class ModelLoader {

	/**
	* Does the parsing itself by using JAXB.
	*
	* @return Parsed JAXB scenario model.
	* @throws PerfCakeException   If XML is not valid or cannot be successfully parsed.
	*/
	Scenario parse(URL scenarioLocation) throws PerfCakeException {
		try {
			final Source scenarioXML = new StreamSource(scenarioLocation.openStream());
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
			return (org.perfcake.model.Scenario) unmarshaller.unmarshal(scenarioXML);
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

	public ScenarioModel loadModel(URL url) throws PerfCakeException {
		final Scenario scenario = parse(url);
		final ScenarioModel model = ModelConverter.getPc4ideModel(scenario);
		return model;
	}

}
