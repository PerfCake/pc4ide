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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import org.perfcake.ide.core.docs.DocsService;
import org.perfcake.ide.core.docs.DocsServiceImpl;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.Property;
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
import org.perfcake.ide.core.model.converter.xml.SerializationPostProcessor;
import org.perfcake.ide.core.model.converter.xml.XmlConverter;
import org.perfcake.ide.core.model.properties.KeyValue;
import org.perfcake.ide.core.model.properties.KeyValueImpl;
import org.perfcake.ide.core.model.properties.SimpleValue;
import org.perfcake.ide.core.model.properties.Value;
import org.perfcake.model.HeaderType;
import org.perfcake.model.ObjectFactory;
import org.perfcake.model.PropertyType;
import org.perfcake.model.Scenario;

/**
 * Tests for converting pc4ide model to xml model using {@link XmlConverter}.
 *
 * @author Jakub Knetl
 */
public class ToXmlConverterTest {
    private DocsService docsService;
    private XmlConverter converter;
    private static ObjectFactory objectFactory;
    private Scenario xmlModel;
    private ScenarioModel pc4ideModel;


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
        docsService = new DocsServiceImpl(javadoc);
        converter = new XmlConverter(docsService);

        // do the conversion
        pc4ideModel = createScenario();
        ArrayList<SerializationPostProcessor> postProcessors = new ArrayList<>();
        xmlModel = converter.convertToXmlModel(pc4ideModel, postProcessors);
    }

    @Test
    public void testScenarioProperties() {

        List<PropertyType> xmlProperties = xmlModel.getProperties().getProperty();

        List<Property> pc4ideProperties = pc4ideModel.getProperties(ScenarioModel.PropertyNames.PROPERTIES.toString());

        assertThat(xmlProperties, hasSize(pc4ideProperties.size()));

        for (int i = 0; i < xmlProperties.size(); i++) {
            PropertyType xmlProperty = xmlProperties.get(i);
            KeyValue pc4ideProperty = pc4ideProperties.get(i).cast(KeyValue.class);

            assertThat(xmlProperty.getName(), equalTo(pc4ideProperty.getKey()));
            assertThat(xmlProperty.getValue(), equalTo(pc4ideProperty.getValue()));
        }
    }

    @Test
    public void testGenerator() throws ModelConversionException {


        Scenario.Generator xmlGenerator = xmlModel.getGenerator();
        Model pc4ideGenerator = pc4ideModel.getSingleProperty(ScenarioModel.PropertyNames.GENERATOR.toString(), Model.class);

        Value expectedImpl = pc4ideGenerator.getSingleProperty(GeneratorModel.PropertyNames.IMPLEMENTATION.toString(), Value.class);
        Value expectedThreads = pc4ideGenerator.getSingleProperty(GeneratorModel.PropertyNames.THREADS.toString(), Value.class);

        assertThat(xmlGenerator.getClazz(), equalTo(expectedImpl.getValue()));
        assertThat(xmlGenerator.getThreads(), equalTo(expectedThreads.getValue()));
        List<PropertyType> xmlGeneratorProperties = xmlGenerator.getProperty();

        assertImplementationProperties(xmlGeneratorProperties, pc4ideGenerator, "autoReplay");

        //check run
        Scenario.Run xmlRun = xmlModel.getRun();
        KeyValue expectedRun = pc4ideGenerator.getSingleProperty(GeneratorModel.PropertyNames.RUN.toString(), KeyValue.class);

        assertThat(xmlRun.getType(), equalTo(expectedRun.getKey()));
        assertThat(xmlRun.getValue(), equalTo(expectedRun.getValue()));
    }

    @Test
    public void testSequences() throws ModelConversionException {
        List<Property> pc4ideSequences = pc4ideModel.getProperties(ScenarioModel.PropertyNames.SEQUENCES.toString());
        assertThat(xmlModel.getSequences().getSequence(),
                hasSize(pc4ideSequences.size()));

        Scenario.Sequences.Sequence xmlSequence = xmlModel.getSequences().getSequence().get(0);
        Model expectedSequence = pc4ideSequences.get(0).cast(Model.class);

        Value expectedImpl = expectedSequence.getSingleProperty(SequenceModel.PropertyNames.IMPLEMENTATION.toString(), Value.class);
        Value expectedId = expectedSequence.getSingleProperty(SequenceModel.PropertyNames.ID.toString(), Value.class);

        assertThat(xmlSequence.getClazz(), equalTo(expectedImpl.getValue()));
        assertThat(xmlSequence.getId(), equalTo(expectedId.getValue()));

        assertImplementationProperties(xmlSequence.getProperty(), expectedSequence, "fileUrl");
    }

    @Test
    public void testSender() {
        Model pc4ideSender = pc4ideModel.getProperties(ScenarioModel.PropertyNames.SENDER.toString()).get(0).cast(Model.class);
        Scenario.Sender xmlSender = xmlModel.getSender();


        Value expectedImpl = pc4ideSender.getProperties(SenderModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class);
        Value expectedTarget = pc4ideSender.getProperties(SenderModel.PropertyNames.TARGET.toString()).get(0).cast(Value.class);

        assertThat(xmlSender.getClazz(), equalTo(expectedImpl.getValue()));
        assertThat(xmlSender.getTarget(), equalTo(expectedTarget.getValue()));

        assertImplementationProperties(xmlSender.getProperty(), pc4ideSender, "method");
    }

    @Test
    public void testReceiver() {
        Model pc4ideReceiver = pc4ideModel.getProperties(ScenarioModel.PropertyNames.RECEIVER.toString()).get(0).cast(Model.class);
        Scenario.Receiver xmlReceiver = xmlModel.getReceiver();

        Value expectedImpl = pc4ideReceiver.getProperties(ReceiverModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class);
        Value expectedSource = pc4ideReceiver.getProperties(ReceiverModel.PropertyNames.SOURCE.toString()).get(0).cast(Value.class);
        Value expectedThreads = pc4ideReceiver.getProperties(ReceiverModel.PropertyNames.THREADS.toString()).get(0).cast(Value.class);

        assertThat(xmlReceiver.getClazz(), equalTo(expectedImpl.getValue()));
        assertThat(xmlReceiver.getSource(), equalTo(expectedSource.getValue()));
        assertThat(xmlReceiver.getThreads(), equalTo(expectedThreads.getValue()));

        assertImplementationProperties(xmlReceiver.getProperty(), pc4ideReceiver, "httpStatusCode");

        // test also correlator which is nested in the receiver
        Model pc4ideCorrelator = pc4ideReceiver.getProperties(ReceiverModel.PropertyNames.CORRELATOR.toString()).get(0).cast(Model.class);
        Scenario.Receiver.Correlator xmlCorrelator = xmlReceiver.getCorrelator();


        Value expectedCorrelatorImpl = pc4ideCorrelator.getProperties(CorrelatorModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class);
        assertThat(xmlCorrelator.getClazz(), equalTo(expectedCorrelatorImpl.getValue()));
        assertThat(filterProperties(xmlCorrelator.getProperty()), empty());
    }

    @Test
    public void testReporting() {

        // test reporting properties
        KeyValue pc4ideReportingProperty = pc4ideModel.getProperties(ScenarioModel.PropertyNames.REPORTERS_PROPERTIES.toString()).get(0).cast(KeyValue.class);
        List<PropertyType> xmlReportingProperties = xmlModel.getReporting().getProperty();

        assertThat(xmlReportingProperties, hasSize(1));
        PropertyType xmlReportingProperty = xmlReportingProperties.get(0);

        assertThat(xmlReportingProperty.getName(), equalTo(pc4ideReportingProperty.getKey()));
        assertThat(xmlReportingProperty.getValue(), equalTo(pc4ideReportingProperty.getValue()));


        // test reporters
        Model pc4ideReporter1 = pc4ideModel.getProperties(ScenarioModel.PropertyNames.REPORTERS.toString()).get(0).cast(Model.class);
        Model pc4ideReporter2 = pc4ideModel.getProperties(ScenarioModel.PropertyNames.REPORTERS.toString()).get(1).cast(Model.class);

        Scenario.Reporting.Reporter xmlReporter1 = xmlModel.getReporting().getReporter().get(0);
        Scenario.Reporting.Reporter xmlReporter2 = xmlModel.getReporting().getReporter().get(1);

        Value expectedImpl1 = pc4ideReporter1.getProperties(ReporterModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class);
        Value expectedImpl2 = pc4ideReporter2.getProperties(ReporterModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class);
        Value expectedEnabled1 = pc4ideReporter1.getProperties(ReporterModel.PropertyNames.ENABLED.toString()).get(0).cast(Value.class);

        assertThat(xmlReporter1.getClazz(), equalTo(expectedImpl1.getValue()));
        assertThat(xmlReporter2.getClazz(), equalTo(expectedImpl2.getValue()));
        assertThat(xmlReporter1.isEnabled(), equalTo(Boolean.valueOf(expectedEnabled1.getValue())));

        assertImplementationProperties(xmlReporter1.getProperty(), pc4ideReporter1, "agentHostname");
        assertThat(filterProperties(xmlReporter2.getProperty()), empty());

        // check destinations
        Model pc4ideDestination1 = pc4ideReporter1.getProperties(ReporterModel.PropertyNames.DESTINATION.toString()).get(0).cast(Model.class);
        Model pc4ideDestination2 = pc4ideReporter1.getProperties(ReporterModel.PropertyNames.DESTINATION.toString()).get(1).cast(Model.class);

        Scenario.Reporting.Reporter.Destination xmlDestination1 = xmlReporter1.getDestination().get(0);
        Scenario.Reporting.Reporter.Destination xmlDestination2 = xmlReporter1.getDestination().get(1);

        Value expectedDestImpl1 = pc4ideDestination1.getProperties(DestinationModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class);
        Value expectedDestImpl2 = pc4ideDestination2.getProperties(DestinationModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class);
        Value expectedDestEnabled1 = pc4ideDestination1.getProperties(DestinationModel.PropertyNames.ENABLED.toString()).get(0).cast(Value.class);
        Value expectedDestEnabled2 = pc4ideDestination2.getProperties(DestinationModel.PropertyNames.ENABLED.toString()).get(0).cast(Value.class);

        assertThat(xmlDestination1.getClazz(), equalTo(expectedDestImpl1.getValue()));
        assertThat(xmlDestination2.getClazz(), equalTo(expectedDestImpl2.getValue()));
        assertThat(xmlDestination1.isEnabled(), equalTo(Boolean.valueOf(expectedDestEnabled1.getValue())));
        assertThat(xmlDestination2.isEnabled(), equalTo(Boolean.valueOf(expectedDestEnabled2.getValue())));

        assertImplementationProperties(xmlDestination1.getProperty(), pc4ideDestination1, "prefix");
        assertImplementationProperties(xmlDestination2.getProperty(), pc4ideDestination2, "level");

        assertThat(xmlReporter2.getDestination(), empty()); // reporter 2 has no destination

        // check period
        assertThat(xmlDestination1.getPeriod(), hasSize(1));
        KeyValue expectedPeriod1 = pc4ideDestination1.getProperties(DestinationModel.PropertyNames.PERIOD.toString()).get(0).cast(KeyValue.class);
        Scenario.Reporting.Reporter.Destination.Period xmlPeriod1 = xmlDestination1.getPeriod().get(0);

        assertThat(xmlPeriod1.getType(), equalTo(expectedPeriod1.getKey()));
        assertThat(xmlPeriod1.getValue(), equalTo(expectedPeriod1.getValue()));

        assertThat(xmlDestination2.getPeriod(), empty()); // destination2 has no period
    }

    @Test
    public void testMessages() {
        final Model pc4ideMessage1 = pc4ideModel.getProperties(ScenarioModel.PropertyNames.MESSAGES.toString()).get(0).cast(Model.class);
        final Model pc4ideMessage2 = pc4ideModel.getProperties(ScenarioModel.PropertyNames.MESSAGES.toString()).get(1).cast(Model.class);
        assertThat(xmlModel.getMessages().getMessage(), hasSize(2));
        final Scenario.Messages.Message xmlMessage1 = xmlModel.getMessages().getMessage().get(0);
        final Scenario.Messages.Message xmlMessage2 = xmlModel.getMessages().getMessage().get(1);

        Value expectedContent1 = pc4ideMessage1.getProperties(MessageModel.PropertyNames.CONTENT.toString()).get(0).cast(Value.class);
        assertThat(xmlMessage1.getContent(), equalTo(expectedContent1.getValue()));

        assertThat(xmlMessage1.getUri(), nullValue());
        assertThat(xmlMessage1.getMultiplicity(), nullValue());

        Value expectedUri2 = pc4ideMessage2.getProperties(MessageModel.PropertyNames.URI.toString()).get(0).cast(Value.class);
        Value expectedMultiplicity2 = pc4ideMessage2.getProperties(MessageModel.PropertyNames.MULTIPLICITY.toString()).get(0).cast(Value.class);

        assertThat(xmlMessage2.getContent(), nullValue());
        assertThat(xmlMessage2.getUri(), equalTo(expectedUri2.getValue()));
        assertThat(xmlMessage2.getMultiplicity(), equalTo(expectedMultiplicity2.getValue()));

        // test properties, validatorRefs and headers
        KeyValue expectedProperty1 = pc4ideMessage1.getProperties(MessageModel.PropertyNames.PROPERTIES.toString()).get(0).cast(KeyValue.class);
        assertThat(xmlMessage1.getProperty(), hasSize(1));
        PropertyType xmlProperty1 = xmlMessage1.getProperty().get(0);
        assertThat(xmlProperty1.getName(), equalTo(expectedProperty1.getKey()));
        assertThat(xmlProperty1.getValue(), equalTo(expectedProperty1.getValue()));

        Value expectedValidatorRef1 = pc4ideMessage1.getProperties(MessageModel.PropertyNames.VALIDATOR_REFS.toString()).get(0).cast(Value.class);

        assertThat(xmlMessage1.getValidatorRef(), hasSize(1));
        Scenario.Messages.Message.ValidatorRef xmlValidatorRef = xmlMessage1.getValidatorRef().get(0);
        assertThat(xmlValidatorRef.getId(), equalTo(expectedValidatorRef1.getValue()));

        KeyValue expectedHeader1 = pc4ideMessage1.getProperties(MessageModel.PropertyNames.HEADERS.toString()).get(0).cast(KeyValue.class);
        assertThat(xmlMessage1.getHeader(), hasSize(1));
        HeaderType xmlHeader = xmlMessage1.getHeader().get(0);
        assertThat(xmlHeader.getName(), equalTo(expectedHeader1.getKey()));
        assertThat(xmlHeader.getValue(), equalTo(expectedHeader1.getValue()));

        //second message has no properties, validatorRefs and headers
        assertThat(xmlMessage2.getProperty(), empty());
        assertThat(xmlMessage2.getHeader(), empty());
        assertThat(xmlMessage2.getValidatorRef(), empty());
    }

    @Test
    public void testValidation() {

        Value expectedValidationEnabled = pc4ideModel.getProperties(ScenarioModel.PropertyNames.VALIDATION_ENABLED.toString()).get(0).cast(Value.class);

        assertThat(xmlModel.getValidation().isEnabled(), equalTo(Boolean.valueOf(expectedValidationEnabled.getValue())));

        Value expectedValidationFf = pc4ideModel.getProperties(ScenarioModel.PropertyNames.VALIDATION_FAST_FORWARD.toString()).get(0).cast(Value.class);
        assertThat(xmlModel.getValidation().isFastForward(), equalTo(Boolean.valueOf(expectedValidationFf.getValue())));

        final Model pc4ideValidator1 = pc4ideModel.getProperties(ScenarioModel.PropertyNames.VALIDATORS.toString()).get(0).cast(Model.class);
        final Model pc4ideValidator2 = pc4ideModel.getProperties(ScenarioModel.PropertyNames.VALIDATORS.toString()).get(1).cast(Model.class);

        assertThat(xmlModel.getValidation().getValidator(), hasSize(2));
        Scenario.Validation.Validator xmlValidator1 = xmlModel.getValidation().getValidator().get(0);
        Scenario.Validation.Validator xmlValidator2 = xmlModel.getValidation().getValidator().get(1);

        // check first validator
        Value expectedImpl1 = pc4ideValidator1.getProperties(ValidatorModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class);
        Value expectedId1 = pc4ideValidator1.getProperties(ValidatorModel.PropertyNames.ID.toString()).get(0).cast(Value.class);
        assertThat(xmlValidator1.getClazz(), equalTo(expectedImpl1.getValue()));
        assertThat(xmlValidator1.getId(), equalTo(expectedId1.getValue()));
        assertImplementationProperties(xmlValidator1.getProperty(), pc4ideValidator1, "record");

        // check second validator
        Value expectedImpl2 = pc4ideValidator2.getProperties(ValidatorModel.PropertyNames.IMPLEMENTATION.toString()).get(0).cast(Value.class);
        Value expectedId2 = pc4ideValidator2.getProperties(ValidatorModel.PropertyNames.ID.toString()).get(0).cast(Value.class);

        assertThat(xmlValidator2.getClazz(), equalTo(expectedImpl2.getValue()));
        assertThat(xmlValidator2.getId(), equalTo(expectedId2.getValue()));
        assertImplementationProperties(xmlValidator2.getProperty(), pc4ideValidator2, "pattern");
    }

    private ScenarioModel createScenario() {
        ScenarioModel scenario = new ScenarioModel(docsService);

        scenario.addProperty(ScenarioModel.PropertyNames.PROPERTIES.toString(), new KeyValueImpl("my-property1", "my-property-value1"));
        scenario.addProperty(ScenarioModel.PropertyNames.PROPERTIES.toString(), new KeyValueImpl("my-property2", "my-property-value2"));

        // create generator

        GeneratorModel generatorModel = new GeneratorModel(docsService);
        generatorModel.addProperty(GeneratorModel.PropertyNames.IMPLEMENTATION.toString(), new SimpleValue("CustomProfileGenerator"));
        generatorModel.addProperty(GeneratorModel.PropertyNames.THREADS.toString(), new SimpleValue("15"));

        // this property is supported as implementation property of custom profile generator
        generatorModel.addProperty("autoReplay", new SimpleValue("false"));

        // create run
        KeyValueImpl runProperty = new KeyValueImpl("time", "123456");
        generatorModel.addProperty(GeneratorModel.PropertyNames.RUN.toString(), runProperty);

        scenario.addProperty(ScenarioModel.PropertyNames.GENERATOR.toString(), generatorModel);

        // create sequence
        SequenceModel sequenceModel = new SequenceModel(docsService);
        sequenceModel.addProperty(SequenceModel.PropertyNames.IMPLEMENTATION.toString(), new SimpleValue("FileLinesSequence"));
        sequenceModel.addProperty(SequenceModel.PropertyNames.ID.toString(), new SimpleValue("seq-id-123"));
        sequenceModel.addProperty("fileUrl", new SimpleValue("file:///my-file/location"));
        scenario.addProperty(ScenarioModel.PropertyNames.SEQUENCES.toString(), sequenceModel);

        //create Sender
        SenderModel senderModel = new SenderModel(docsService);
        senderModel.addProperty(SenderModel.PropertyNames.IMPLEMENTATION.toString(), new SimpleValue("HttpSender"));
        senderModel.addProperty(SenderModel.PropertyNames.TARGET.toString(), new SimpleValue("sender-target-123"));
        senderModel.addProperty("method", new SimpleValue("GET"));
        scenario.addProperty(ScenarioModel.PropertyNames.SENDER.toString(), senderModel);

        //create Receiver
        ReceiverModel receiverModel = new ReceiverModel(docsService);
        receiverModel.addProperty(ReceiverModel.PropertyNames.IMPLEMENTATION.toString(), new SimpleValue("HttpReceiver"));
        receiverModel.addProperty(ReceiverModel.PropertyNames.THREADS.toString(), new SimpleValue("11"));
        receiverModel.addProperty(ReceiverModel.PropertyNames.SOURCE.toString(), new SimpleValue("receiver-source"));
        receiverModel.addProperty("httpStatusCode", new SimpleValue("500"));

        // correlator
        CorrelatorModel correlatorModel = new CorrelatorModel(docsService);
        correlatorModel.addProperty(CorrelatorModel.PropertyNames.IMPLEMENTATION.toString(),
                new SimpleValue("GenerateHeaderCorrelator"));
        Scenario.Receiver.Correlator correlator = objectFactory.createScenarioReceiverCorrelator();

        receiverModel.addProperty(ReceiverModel.PropertyNames.CORRELATOR.toString(), correlatorModel);

        scenario.addProperty(ScenarioModel.PropertyNames.RECEIVER.toString(), receiverModel);

        // create reporting
        scenario.addProperty(ScenarioModel.PropertyNames.REPORTERS_PROPERTIES.toString(), new KeyValueImpl("rep-property", "prop"));

        //reporter1 with destinations
        ReporterModel reporterModel1 = new ReporterModel(docsService);
        reporterModel1.addProperty(ReporterModel.PropertyNames.IMPLEMENTATION.toString(), new SimpleValue("MemoryUsageReporter"));
        reporterModel1.addProperty(ReporterModel.PropertyNames.ENABLED.toString(), new SimpleValue("true"));
        reporterModel1.addProperty("agentHostname", new SimpleValue("my-hostname"));

        DestinationModel destinationModel1 = new DestinationModel(docsService);
        destinationModel1.addProperty(DestinationModel.PropertyNames.IMPLEMENTATION.toString(), new SimpleValue("ConsoleDestination"));
        destinationModel1.addProperty("prefix", new SimpleValue("d1:"));
        destinationModel1.addProperty(DestinationModel.PropertyNames.PERIOD.toString(), new KeyValueImpl("iterations", "55555"));
        destinationModel1.addProperty(DestinationModel.PropertyNames.ENABLED.toString(), new SimpleValue("true"));

        DestinationModel destinationModel2 = new DestinationModel(docsService);
        destinationModel2.addProperty(DestinationModel.PropertyNames.IMPLEMENTATION.toString(), new SimpleValue("Log4jDestination"));
        destinationModel2.addProperty("level", new SimpleValue("info"));
        destinationModel2.addProperty(DestinationModel.PropertyNames.ENABLED.toString(), new SimpleValue("false"));

        reporterModel1.addProperty(ReporterModel.PropertyNames.DESTINATION.toString(), destinationModel1);
        reporterModel1.addProperty(ReporterModel.PropertyNames.DESTINATION.toString(), destinationModel2);


        //reporter2 with destinations
        ReporterModel reporterModel2 = new ReporterModel(docsService);
        reporterModel2.addProperty(ReporterModel.PropertyNames.IMPLEMENTATION.toString(), new SimpleValue("WarmUpReporter"));
        reporterModel2.addProperty(ReporterModel.PropertyNames.ENABLED.toString(), new SimpleValue("false"));

        scenario.addProperty(ScenarioModel.PropertyNames.REPORTERS.toString(), reporterModel1);
        scenario.addProperty(ScenarioModel.PropertyNames.REPORTERS.toString(), reporterModel2);

        // create messages

        MessageModel messageModel1 = new MessageModel(docsService);
        messageModel1.addProperty(MessageModel.PropertyNames.CONTENT.toString(), new SimpleValue("Hello world!"));
        messageModel1.addProperty(MessageModel.PropertyNames.HEADERS.toString(), new KeyValueImpl("my-header-name", "my-header-value"));
        messageModel1.addProperty(MessageModel.PropertyNames.PROPERTIES.toString(), new KeyValueImpl("my-message-property", "my-message-property-value"));
        messageModel1.addProperty(MessageModel.PropertyNames.VALIDATOR_REFS.toString(), new SimpleValue("v2-regexp"));

        MessageModel messageModel2 = new MessageModel(docsService);
        messageModel2.addProperty(MessageModel.PropertyNames.URI.toString(), new SimpleValue("file:///my-message"));
        messageModel2.addProperty(MessageModel.PropertyNames.MULTIPLICITY.toString(), new SimpleValue("5"));

        scenario.addProperty(ScenarioModel.PropertyNames.MESSAGES.toString(), messageModel1);
        scenario.addProperty(ScenarioModel.PropertyNames.MESSAGES.toString(), messageModel2);

        //create validation
        ValidatorModel validatorModel1 = new ValidatorModel(docsService);
        validatorModel1.addProperty(ValidatorModel.PropertyNames.IMPLEMENTATION.toString(), new SimpleValue("DictionaryValidator"));
        validatorModel1.addProperty(ValidatorModel.PropertyNames.ID.toString(), new SimpleValue("dic-val-1"));
        validatorModel1.addProperty("record", new SimpleValue("true"));

        ValidatorModel validatorModel2 = new ValidatorModel(docsService);
        validatorModel2.addProperty(ValidatorModel.PropertyNames.IMPLEMENTATION.toString(), new SimpleValue("RegExpValidator"));
        validatorModel2.addProperty(ValidatorModel.PropertyNames.ID.toString(), new SimpleValue("v2-regexp"));
        validatorModel2.addProperty("pattern", new SimpleValue("my [Pp].*"));

        scenario.addProperty(ScenarioModel.PropertyNames.VALIDATION_ENABLED.toString(), new SimpleValue("false"));
        scenario.addProperty(ScenarioModel.PropertyNames.VALIDATION_FAST_FORWARD.toString(), new SimpleValue("true"));

        scenario.addProperty(ScenarioModel.PropertyNames.VALIDATORS.toString(), validatorModel1);
        scenario.addProperty(ScenarioModel.PropertyNames.VALIDATORS.toString(), validatorModel2);

        return scenario;
    }

    private void assertImplementationProperties(List<PropertyType> actualProperties, Model expectedModel, String... propertyNames) {

        List<PropertyType> actualFilteredProperties = filterProperties(actualProperties);

        assertThat(actualFilteredProperties, hasSize(propertyNames.length));

        for (int i = 0; i < actualFilteredProperties.size(); i++) {
            String propertyName = propertyNames[i];
            PropertyType actualProperty = actualFilteredProperties.get(i);

            assertThat(actualProperty.getName(), equalTo(propertyName));

            Value expectedValue = expectedModel.getSingleProperty(propertyName, Value.class);
            assertThat(expectedValue, notNullValue());
            assertThat(actualProperty.getValue(), equalTo(expectedValue.getValue()));
        }

    }

    /**
     * Filters custom properties. It removes special properties which were added for post processing.
     *
     * @param properties properties to be filtered
     * @return list of filtered properteis
     */
    private List<PropertyType> filterProperties(List<PropertyType> properties) {
        List<PropertyType> filtered = new ArrayList<>();

        for (PropertyType p : properties) {
            if (!p.getName().startsWith("org.perfcake.ide.postprocessing")) {
                filtered.add(p);
            }
        }
        return filtered;
    }

}
