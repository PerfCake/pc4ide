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

package org.perfcake.ide.core.org.perfcake.ide.core.model.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import org.perfcake.ide.core.docs.DocServiceImpl;
import org.perfcake.ide.core.docs.DocsService;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.PropertyInfo;
import org.perfcake.ide.core.model.components.CorrelatorModel;
import org.perfcake.ide.core.model.components.DestinationModel;
import org.perfcake.ide.core.model.components.GeneratorModel;
import org.perfcake.ide.core.model.components.MessageModel;
import org.perfcake.ide.core.model.components.ReceiverModel;
import org.perfcake.ide.core.model.components.ReporterModel;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.components.SenderModel;
import org.perfcake.ide.core.model.components.SequenceModel;
import org.perfcake.ide.core.model.components.ValidatorModel;
import org.perfcake.ide.core.model.converter.XmlConverter;
import org.perfcake.ide.core.model.properties.KeyValue;
import org.perfcake.ide.core.model.properties.Value;
import org.perfcake.model.HeaderType;
import org.perfcake.model.ObjectFactory;
import org.perfcake.model.PropertyType;
import org.perfcake.model.Scenario;

/**
 * Tests for {@link org.perfcake.ide.core.model.converter.XmlConverter}.
 *
 * @author Jakub Knetl
 */
public class XmlConverterTest {
    private DocsService docsService;
    private XmlConverter converter;
    private static ObjectFactory objectFactory;
    private Scenario xmlModel;
    private ScenarioModel scenarioModel;


    /**
     * Sets up a converter.
     *
     * @throws IOException              if javadoc cannot be loaded.
     * @throws ModelConversionException if model cannot be converted
     */
    @Before
    public void setUp() throws IOException, ModelConversionException {
        Properties javadoc = new Properties();
        Path javadocPath = Paths.get("src/main/resources/perfcake-comment.properties");
        assertTrue("File with javadoc does not exists.", Files.exists(javadocPath));
        InputStream inStream = Files.newInputStream(javadocPath);
        javadoc.load(inStream);
        objectFactory = new ObjectFactory();
        docsService = new DocServiceImpl(javadoc);
        converter = new XmlConverter(docsService);

        // do the conversion
        xmlModel = createScenario();
        scenarioModel = converter.convertToPc4ideModel(xmlModel);

    }

    @Test
    public void testScenarioProperties() {
        KeyValue property = scenarioModel.getProperties(ScenarioModel.PropertyNames.PROPERTIES.toString()).get(0).cast(KeyValue.class);
        PropertyType xmlProperty = xmlModel.getProperties().getProperty().get(0);

        assertThat(property.getKey(), equalTo(xmlProperty.getName()));
        assertThat(property.getValue(), equalTo(xmlProperty.getValue()));
    }

    @Test
    public void testGenerator() throws ModelConversionException {
        Model generator = scenarioModel.getProperties(ScenarioModel.PropertyNames.GENERATOR.toString()).get(0).cast(Model.class);
        assertThat(generator.getProperties(GeneratorModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlModel.getGenerator().getClazz()));

        //check run
        assertThat(generator.getProperties(GeneratorModel.PropertyNames.THREADS.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlModel.getGenerator().getThreads()));
        KeyValue run = generator.getProperties(GeneratorModel.PropertyNames.RUN.toString()).get(0).cast(KeyValue.class);
        assertThat(run.getKey(), equalTo(xmlModel.getRun().getType()));
        assertThat(run.getValue(), equalTo(xmlModel.getRun().getValue()));

        //check implementation property
        assertImplementationProperties(generator, xmlModel.getGenerator().getProperty());
    }

    @Test
    public void testSequences() throws ModelConversionException {
        Model sequence = scenarioModel.getProperties(ScenarioModel.PropertyNames.SEQUENCES.toString()).get(0).cast(Model.class);
        Scenario.Sequences.Sequence xmlSequence = xmlModel.getSequences().getSequence().get(0);

        assertThat(sequence.getProperties(SequenceModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlSequence.getClazz()));
        assertThat(sequence.getProperties(SequenceModel.PropertyNames.ID.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlSequence.getId()));

        assertImplementationProperties(sequence, xmlSequence.getProperty());
    }

    @Test
    public void testSender() {
        Model sender = scenarioModel.getProperties(ScenarioModel.PropertyNames.SENDER.toString()).get(0).cast(Model.class);
        Scenario.Sender xmlSender = xmlModel.getSender();

        assertThat(sender.getProperties(SenderModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlSender.getClazz()));

        assertThat(sender.getProperties(SenderModel.PropertyNames.TARGET.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlSender.getTarget()));

        PropertyInfo propertyInfo = sender.getSupportedProperty(xmlSender.getProperty().get(0).getName());
        assertImplementationProperties(sender, xmlSender.getProperty());
    }

    @Test
    public void testReceiver() {
        Model receiver = scenarioModel.getProperties(ScenarioModel.PropertyNames.RECEIVER.toString()).get(0).cast(Model.class);
        Scenario.Receiver xmlReceiver = xmlModel.getReceiver();

        assertThat(receiver.getProperties(ReceiverModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlReceiver.getClazz()));
        assertThat(receiver.getProperties(ReceiverModel.PropertyNames.SOURCE.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlReceiver.getSource()));
        assertThat(receiver.getProperties(ReceiverModel.PropertyNames.THREADS.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlReceiver.getThreads()));
        assertImplementationProperties(receiver, xmlReceiver.getProperty());

        // test also correlator which is nested in the receiver

        Model correlator = receiver.getProperties(ReceiverModel.PropertyNames.CORRELATOR.toString()).get(0).cast(Model.class);
        Scenario.Receiver.Correlator xmlCorrelator = xmlReceiver.getCorrelator();

        assertThat(correlator.getProperties(CorrelatorModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlCorrelator.getClazz()));
        assertImplementationProperties(correlator, xmlCorrelator.getProperty());
    }

    @Test
    public void testReporting() {

        Property reportingProperty = scenarioModel.getProperties(ScenarioModel.PropertyNames.REPORTERS_PROPERTIES.toString()).get(0);
        assertThat(reportingProperty.cast(KeyValue.class).getKey(), equalTo(xmlModel.getReporting().getProperty().get(0).getName()));
        assertThat(reportingProperty.cast(KeyValue.class).getValue(), equalTo(xmlModel.getReporting().getProperty().get(0).getValue()));


        Model reporter1 = scenarioModel.getProperties(ScenarioModel.PropertyNames.REPORTERS.toString()).get(0).cast(Model.class);
        Model reporter2 = scenarioModel.getProperties(ScenarioModel.PropertyNames.REPORTERS.toString()).get(1).cast(Model.class);
        Scenario.Reporting.Reporter xmlReporter1 = xmlModel.getReporting().getReporter().get(0);
        Scenario.Reporting.Reporter xmlReporter2 = xmlModel.getReporting().getReporter().get(1);

        assertThat(reporter1.getProperties(ReporterModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlReporter1.getClazz()));
        assertThat(reporter2.getProperties(ReporterModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlReporter2.getClazz()));

        assertThat(reporter1.getProperties(ReporterModel.PropertyNames.ENABLED.toString()).get(0).cast(Value.class).getValue(),
                equalTo(String.valueOf(xmlReporter1.isEnabled())));
        assertThat(reporter2.getProperties(ReporterModel.PropertyNames.ENABLED.toString()), empty());

        // check destinations
        Model destination1 = reporter1.getProperties(ReporterModel.PropertyNames.DESTINATION.toString()).get(0).cast(Model.class);
        Model destination2 = reporter1.getProperties(ReporterModel.PropertyNames.DESTINATION.toString()).get(1).cast(Model.class);

        Scenario.Reporting.Reporter.Destination xmlDestination1 = xmlReporter1.getDestination().get(0);
        Scenario.Reporting.Reporter.Destination xmlDestination2 = xmlReporter1.getDestination().get(1);

        assertThat(destination1.getProperties(DestinationModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlDestination1.getClazz()));
        assertThat(destination2.getProperties(DestinationModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlDestination2.getClazz()));
        assertThat(destination1.getProperties(DestinationModel.PropertyNames.ENABLED.toString()).get(0).cast(Value.class).getValue(),
                equalTo(String.valueOf(xmlDestination1.isEnabled())));
        assertThat(destination2.getProperties(DestinationModel.PropertyNames.ENABLED.toString()).get(0).cast(Value.class).getValue(),
                equalTo(String.valueOf(xmlDestination2.isEnabled())));

        KeyValue period = destination1.getProperties(DestinationModel.PropertyNames.PERIOD.toString()).get(0).cast(KeyValue.class);
        assertThat(period.getKey(), equalTo(xmlDestination1.getPeriod().get(0).getType()));
        assertThat(period.getValue(), equalTo(xmlDestination1.getPeriod().get(0).getValue()));
        assertThat(destination2.getProperties(DestinationModel.PropertyNames.PERIOD.toString()), empty()); // destination2 has no period

        assertImplementationProperties(destination1, xmlDestination1.getProperty());
        assertImplementationProperties(destination2, xmlDestination2.getProperty());

        assertThat(reporter2.getProperties(ReporterModel.PropertyNames.DESTINATION.toString()), empty()); // reporter 2 has no destination

        assertImplementationProperties(reporter1, xmlReporter1.getProperty());
        assertImplementationProperties(reporter2, xmlReporter2.getProperty());
    }

    @Test
    public void testMessages() {
        final Model message1 = scenarioModel.getProperties(ScenarioModel.PropertyNames.MESSAGES.toString()).get(0).cast(Model.class);
        final Model message2 = scenarioModel.getProperties(ScenarioModel.PropertyNames.MESSAGES.toString()).get(1).cast(Model.class);

        final Scenario.Messages.Message xmlMessage1 = xmlModel.getMessages().getMessage().get(0);
        final Scenario.Messages.Message xmlMessage2 = xmlModel.getMessages().getMessage().get(1);

        assertThat(message1.getProperties(MessageModel.PropertyNames.CONTENT.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlMessage1.getContent()));
        assertThat(message1.getProperties(MessageModel.PropertyNames.URI.toString()), empty());
        assertThat(message1.getProperties(MessageModel.PropertyNames.MULTIPLICITY.toString()), empty());

        assertThat(message2.getProperties(MessageModel.PropertyNames.CONTENT.toString()), empty());
        assertThat(message2.getProperties(MessageModel.PropertyNames.URI.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlMessage2.getUri()));
        assertThat(message2.getProperties(MessageModel.PropertyNames.MULTIPLICITY.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlMessage2.getMultiplicity()));

        // test properties, validatorRefs and headers
        KeyValue property = message1.getProperties(MessageModel.PropertyNames.PROPERTIES.toString()).get(0).cast(KeyValue.class);
        assertThat(property.getKey(), equalTo(xmlMessage1.getProperty().get(0).getName()));
        assertThat(property.getValue(), equalTo(xmlMessage1.getProperty().get(0).getValue()));

        Value validatorRef =
                message1.getProperties(MessageModel.PropertyNames.VALIDATOR_REFS.toString()).get(0).cast(Value.class);

        Scenario.Messages.Message.ValidatorRef xmlValidatorRef = xmlMessage1.getValidatorRef().get(0);
        assertThat(validatorRef.getValue(), equalTo(xmlValidatorRef.getId()));

        KeyValue header =
                message1.getProperties(MessageModel.PropertyNames.HEADERS.toString()).get(0).cast(KeyValue.class);
        HeaderType xmlHeader = xmlMessage1.getHeader().get(0);
        assertThat(header.getKey(), equalTo(xmlHeader.getName()));
        assertThat(header.getValue(), equalTo(xmlHeader.getValue()));
    }

    @Test
    public void testValidation() {

        Value validationEnabled =
                scenarioModel.getProperties(ScenarioModel.PropertyNames.VALIDATION_ENABLED.toString()).get(0).cast(Value.class);
        assertThat(validationEnabled.getValue(), equalTo(String.valueOf(xmlModel.getValidation().isEnabled())));
        List<Property> fastForward = scenarioModel.getProperties(ScenarioModel.PropertyNames.VALIDATION_FAST_FORWARD.toString());
        assertThat(fastForward.get(0).cast(Value.class).getValue(), equalTo(String.valueOf(xmlModel.getValidation().isFastForward())));

        final Model validator1 = scenarioModel.getProperties(ScenarioModel.PropertyNames.VALIDATORS.toString()).get(0).cast(Model.class);
        final Model validator2 = scenarioModel.getProperties(ScenarioModel.PropertyNames.VALIDATORS.toString()).get(1).cast(Model.class);

        Scenario.Validation.Validator xmlValidator1 = xmlModel.getValidation().getValidator().get(0);
        final Scenario.Validation.Validator xmlValidator2 = xmlModel.getValidation().getValidator().get(1);

        // check first validator
        assertThat(validator1.getProperties(ValidatorModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlValidator1.getClazz()));
        assertThat(validator1.getProperties(ValidatorModel.PropertyNames.ID.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlValidator1.getId()));
        assertImplementationProperties(validator1, xmlValidator1.getProperty());

        // check second validator
        assertThat(validator2.getProperties(ValidatorModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlValidator2.getClazz()));
        assertThat(validator2.getProperties(ValidatorModel.PropertyNames.ID.toString()).get(0).cast(Value.class).getValue(),
                equalTo(xmlValidator2.getId()));
        assertImplementationProperties(validator2, xmlValidator2.getProperty());
    }

    private Scenario createScenario() {
        //create scenario
        Scenario scenario = objectFactory.createScenario();

        Scenario.Properties scenarioProperties = objectFactory.createScenarioProperties();
        scenario.setProperties(scenarioProperties);
        scenario.getProperties().getProperty().add(createProperty("my-property", "my-property-value"));
        scenario.getProperties().getProperty().add(createProperty("my-property", "my-property-value"));

        // create run
        Scenario.Run run = objectFactory.createScenarioRun();
        run.setType("time");
        run.setValue("123456");

        // create generator
        Scenario.Generator generator = objectFactory.createScenarioGenerator();
        generator.setClazz("CustomProfileGenerator");
        generator.setThreads("15");
        generator.getProperty().add(createProperty("autoReplay", "false"));

        scenario.setGenerator(generator);
        scenario.setRun(run);

        // create sequence
        final Scenario.Sequences sequences = objectFactory.createScenarioSequences();
        Scenario.Sequences.Sequence sequence = objectFactory.createScenarioSequencesSequence();

        sequence.setClazz("FileLinesSequence");
        sequence.setId("seq-id-123");
        sequence.getProperty().add(createProperty("fileUrl", "file:///my-file/location"));

        sequences.getSequence().add(sequence);
        scenario.setSequences(sequences);

        //create Sender
        Scenario.Sender sender = objectFactory.createScenarioSender();
        sender.setTarget("sender-target-123");
        sender.setClazz("HttpSender");
        sender.getProperty().add(createProperty("method", "GET"));

        scenario.setSender(sender);

        //create Receiver

        Scenario.Receiver receiver = objectFactory.createScenarioReceiver();

        receiver.setClazz("HttpReceiver");
        receiver.setSource("Source-of-the-receiver");
        receiver.setThreads("11");
        receiver.getProperty().add(createProperty("httpStatusCode", "500"));

        Scenario.Receiver.Correlator correlator = objectFactory.createScenarioReceiverCorrelator();
        correlator.setClazz("GenerateHeaderCorrelator");
        receiver.setCorrelator(correlator);

        scenario.setReceiver(receiver);

        // create reporting
        Scenario.Reporting reporting = objectFactory.createScenarioReporting();
        reporting.getProperty().add(createProperty("rep-property", "prop"));
        Scenario.Reporting.Reporter reporter1 = objectFactory.createScenarioReportingReporter();

        reporter1.setClazz("MemoryUsageReporter");
        reporter1.setEnabled(true);

        Scenario.Reporting.Reporter.Destination d1 = objectFactory.createScenarioReportingReporterDestination();
        d1.setClazz("ConsoleDestination");
        d1.getProperty().add(createProperty("prefix", "d1:"));

        Scenario.Reporting.Reporter.Destination.Period period = objectFactory.createScenarioReportingReporterDestinationPeriod();
        period.setType("iterations");
        period.setValue("55555");
        d1.getPeriod().add(period);

        Scenario.Reporting.Reporter.Destination d2 = objectFactory.createScenarioReportingReporterDestination();
        d2.setClazz("Log4jDestination");
        d2.getProperty().add(createProperty("level", "INFO"));

        reporter1.getDestination().add(d1);
        reporter1.getDestination().add(d2);

        Scenario.Reporting.Reporter reporter2 = objectFactory.createScenarioReportingReporter();

        reporter2.setClazz("WarmUpReporter");
        reporter2.setEnabled(false);

        reporting.getReporter().add(reporter1);
        reporting.getReporter().add(reporter2);

        scenario.setReporting(reporting);

        // create messages
        final Scenario.Messages messages = objectFactory.createScenarioMessages();
        final Scenario.Messages.Message m1 = objectFactory.createScenarioMessagesMessage();
        final Scenario.Messages.Message m2 = objectFactory.createScenarioMessagesMessage();

        m1.setContent("hello world!");
        HeaderType header = objectFactory.createHeaderType();
        header.setName("my-header-name");
        header.setValue("my-header-value");
        m1.getHeader().add(header);
        m1.getProperty().add(createProperty("my-message-property", "my-message-property-value"));
        Scenario.Messages.Message.ValidatorRef ref = objectFactory.createScenarioMessagesMessageValidatorRef();
        ref.setId("v2-regexp");
        m1.getValidatorRef().add(ref);
        m2.setUri("file:///my-message");
        m2.setMultiplicity("5");

        messages.getMessage().add(m1);
        messages.getMessage().add(m2);

        scenario.setMessages(messages);

        //create validation
        Scenario.Validation validation = objectFactory.createScenarioValidation();

        validation.setEnabled(false);
        validation.setFastForward(true);

        Scenario.Validation.Validator v1 = objectFactory.createScenarioValidationValidator();
        final Scenario.Validation.Validator v2 = objectFactory.createScenarioValidationValidator();

        v1.setClazz("DictionaryValidator");
        v1.setId("dic-val-1");
        v1.getProperty().add(createProperty("record", "true"));

        v2.setClazz("RegExpValidator");
        v2.setId("v2-regexp");
        v2.getProperty().add(createProperty("pattern", "my [Pp].*"));

        validation.getValidator().add(v1);
        validation.getValidator().add(v2);

        scenario.setValidation(validation);


        return scenario;
    }

    /**
     * Checks whether model contains implementation specific properties with proper value.
     *
     * @param model         model object
     * @param xmlProperties properties of xml model
     */
    private void assertImplementationProperties(Model model, List<PropertyType> xmlProperties) {
        if (xmlProperties == null) {
            return;
        }
        for (PropertyType p : xmlProperties) {
            PropertyInfo propertyInfo = model.getSupportedProperty(p.getName());
            assertThat(propertyInfo, notNullValue());
            Value value = model.getProperties(propertyInfo).get(0).cast(Value.class);
            assertThat(value.getValue(), equalTo(p.getValue()));
        }
    }

    private static PropertyType createProperty(String name, String value) {
        PropertyType property = objectFactory.createPropertyType();
        property.setName(name);
        property.setValue(value);

        return property;
    }
}
