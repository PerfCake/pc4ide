package org.perfcake.ide.core.model;
/**
 *
 */

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.perfcake.PerfCakeException;
import org.perfcake.model.Scenario;
import org.perfcake.model.Scenario.Reporting.Reporter;

import java.io.File;
import java.net.MalformedURLException;


/**
 * @author jknetl
 *
 */
public class LoaderTest {

	ModelLoader parser;


	@Before
	public void before() {
		parser = new ModelLoader();
	}

	@Test
	public void testScenarioParsing() throws MalformedURLException, PerfCakeException {
		final File scenarioFile = new File("src/test/resources/scenario/http.xml");

		final Scenario scenario = parser.parse(scenarioFile.toURI().toURL());
		//		final Scenario scenario = parser.parse(Utils.getResourceAsUrl("/scenarios/http.xml"));

		//Generator
		Assert.assertEquals("DefaultMessageGenerator", scenario.getGenerator().getClazz());
		Assert.assertEquals("${perfcake.thread.count:100}", scenario.getGenerator().getThreads());

		//Sender
		Assert.assertEquals("HttpSender", scenario.getSender().getClazz());
		Assert.assertEquals("http://${server.host}/post", scenario.getSender().getTarget());
		Assert.assertEquals(1, scenario.getSender().getProperty().size());
		Assert.assertEquals("method", scenario.getSender().getProperty().get(0).getName());
		Assert.assertEquals("POST", scenario.getSender().getProperty().get(0).getValue());

		// reporting
		Assert.assertEquals(1, scenario.getReporting().getReporter().size());
		final Reporter reporter = scenario.getReporting().getReporter().get(0);
		Assert.assertEquals("IterationsPerSecondReporter", reporter.getClazz());
		Assert.assertEquals(1, reporter.getDestination().size());
		Assert.assertEquals(1, reporter.getDestination().get(0).getPeriod().size());
		Assert.assertEquals("time", reporter.getDestination().get(0).getPeriod().get(0).getType());
		Assert.assertEquals("1000", reporter.getDestination().get(0).getPeriod().get(0).getValue());

		// messages
		Assert.assertEquals(1, scenario.getMessages().getMessage().size());
		Assert.assertEquals("plain.txt", scenario.getMessages().getMessage().get(0).getUri());

	}

	@Test
	public void testScenarioConverting() throws MalformedURLException, PerfCakeException {

		final File scenarioFile = new File("src/test/resources/scenario/http.xml");

		final ScenarioModel scenario = parser.loadModel(scenarioFile.toURI().toURL());

		//Generator
		Assert.assertEquals("DefaultMessageGenerator", scenario.getGenerator().getClazz());
		Assert.assertEquals("${perfcake.thread.count:100}", scenario.getGenerator().getThreads());

		//Sender
		Assert.assertEquals("HttpSender", scenario.getSender().getClazz());
		Assert.assertEquals("http://${server.host}/post", scenario.getSender().getTarget());
		Assert.assertEquals(1, scenario.getSender().getProperty().size());
		Assert.assertEquals("method", scenario.getSender().getProperty().get(0).getName());
		Assert.assertEquals("POST", scenario.getSender().getProperty().get(0).getValue());

		// reporting
		Assert.assertEquals(1, scenario.getReporting().getReporter().size());
		final ReporterModel reporter = scenario.getReporting().getReporter().get(0);
		Assert.assertEquals("IterationsPerSecondReporter", reporter.getClazz());
		Assert.assertEquals(1, reporter.getDestination().size());
		Assert.assertEquals(1, reporter.getDestination().get(0).getPeriod().size());
		Assert.assertEquals("time", reporter.getDestination().get(0).getPeriod().get(0).getType());
		Assert.assertEquals("1000", reporter.getDestination().get(0).getPeriod().get(0).getValue());

		// messages
		Assert.assertEquals(1, scenario.getMessages().getMessage().size());
		Assert.assertEquals("plain.txt", scenario.getMessages().getMessage().get(0).getUri());
	}


}
