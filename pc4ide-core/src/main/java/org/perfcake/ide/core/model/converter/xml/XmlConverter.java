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

package org.perfcake.ide.core.model.converter.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.perfcake.ide.core.docs.DocsService;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.model.AbstractModel;
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
import org.perfcake.ide.core.model.components.ScenarioModel.PropertyNames;
import org.perfcake.ide.core.model.components.SenderModel;
import org.perfcake.ide.core.model.components.SequenceModel;
import org.perfcake.ide.core.model.components.ValidatorModel;
import org.perfcake.ide.core.model.properties.KeyValue;
import org.perfcake.ide.core.model.properties.KeyValueImpl;
import org.perfcake.ide.core.model.properties.SimpleValue;
import org.perfcake.ide.core.model.properties.Value;
import org.perfcake.model.HeaderType;
import org.perfcake.model.ObjectFactory;
import org.perfcake.model.PropertyType;
import org.perfcake.model.Scenario;
import org.perfcake.model.Scenario.Messages.Message;
import org.perfcake.model.Scenario.Messages.Message.ValidatorRef;
import org.perfcake.model.Scenario.Receiver;
import org.perfcake.model.Scenario.Receiver.Correlator;
import org.perfcake.model.Scenario.Reporting.Reporter;
import org.perfcake.model.Scenario.Reporting.Reporter.Destination;
import org.perfcake.model.Scenario.Reporting.Reporter.Destination.Period;
import org.perfcake.model.Scenario.Sequences.Sequence;
import org.perfcake.model.Scenario.Validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XmlConvertor can convert model between pc4ide model and perfcake model.
 *
 * @author Jakub Knetl
 */
public class XmlConverter {

    static final Logger logger = LoggerFactory.getLogger(XmlConverter.class);

    public static final String POST_PROCESSING_PROPERTY_NAME = "org.perfcake.ide.postprocessing.xml.xpath";

    private DocsService docsService;

    private ObjectFactory objectFactory = new ObjectFactory();

    public XmlConverter(DocsService docsService) {
        this.docsService = docsService;
    }

    private class PostProcessingContext extends Message {
        private int postProcessorCounter = 0;
        private XPathPostProcessor xPathPostProcessor = new XPathPostProcessor();
        private XpathNodeRemoverPostProcessor xpathNodeRemoverPostProcessor = new XpathNodeRemoverPostProcessor();

        public int getPostProcessorCounter() {
            return postProcessorCounter;
        }

        public void increaseCounter() {
            postProcessorCounter++;
        }

        public XPathPostProcessor getXPathPostProcessor() {
            return xPathPostProcessor;
        }

        public XpathNodeRemoverPostProcessor getXpathNodeRemoverPostProcessor() {
            return xpathNodeRemoverPostProcessor;
        }
    }

    /**
     * Converts Scenario represented as PerfCake XML model into scenario represented by pc4ide model.
     *
     * @param xmlModel valid Scenario in PerfCake XML model.
     * @return pc4ide model of a Scenario
     * @throws ModelConversionException When model cannot be converted.
     */
    public ScenarioModel convertToPc4ideModel(Scenario xmlModel) throws ModelConversionException {

        if (xmlModel == null) {
            throw new IllegalArgumentException("Model can't be null.");
        }

        ScenarioModel pc4ideModel = new ScenarioModel(docsService);

        // Convert scenario properties
        if (xmlModel.getProperties() != null) {
            PropertyInfo propertyInfo = pc4ideModel.getSupportedProperty(PropertyNames.PROPERTIES.toString());
            for (PropertyType xmlProperty : xmlModel.getProperties().getProperty()) {
                KeyValue pc4ideProperty = new KeyValueImpl(xmlProperty.getName(), xmlProperty.getValue());
                pc4ideModel.addProperty(propertyInfo, pc4ideProperty);
            }
        }

        // Convert generator
        if (xmlModel.getGenerator() != null) {
            convertGeneratorToModel(xmlModel, pc4ideModel);
        }

        // Convert sender
        if (xmlModel.getSender() != null) {
            convertSenderToModel(xmlModel, pc4ideModel);
        }

        // convert sequences
        if (xmlModel.getSequences() != null && xmlModel.getSequences().getSequence() != null) {
            convertSequencesToModel(xmlModel.getSequences().getSequence(), pc4ideModel);
        }

        // convert Receiver
        if (xmlModel.getReceiver() != null) {
            convertReceiverToModel(xmlModel.getReceiver(), pc4ideModel);
        }

        // convert reporters
        if (xmlModel.getReporting() != null) {
            if (xmlModel.getReporting().getReporter() != null) {
                convertReportersToModel(xmlModel.getReporting().getReporter(), pc4ideModel);
            }

            PropertyInfo propertyInfo = pc4ideModel.getSupportedProperty(PropertyNames.REPORTERS_PROPERTIES.toString());
            for (PropertyType xmlProperty : xmlModel.getReporting().getProperty()) {
                Property property = new KeyValueImpl(xmlProperty.getName(), xmlProperty.getValue());
                pc4ideModel.addProperty(propertyInfo, property);
            }
        }

        //
        if (xmlModel.getMessages() != null && xmlModel.getMessages().getMessage() != null) {
            convertMessagesToModel(xmlModel.getMessages().getMessage(), pc4ideModel);
        }

        if (xmlModel.getValidation() != null && xmlModel.getValidation().getValidator() != null) {
            convertValidatorsToModel(xmlModel.getValidation().getValidator(), pc4ideModel);
        }

        if (xmlModel.getValidation() != null) {

            PropertyInfo enabledInfo = pc4ideModel.getSupportedProperty(PropertyNames.VALIDATION_ENABLED.toString());
            Property enabled = new SimpleValue(String.valueOf(xmlModel.getValidation().isEnabled()));
            pc4ideModel.addProperty(enabledInfo, enabled);

            PropertyInfo fastForwardInfo = pc4ideModel.getSupportedProperty(PropertyNames.VALIDATION_FAST_FORWARD.toString());
            Property fastForward = new SimpleValue(String.valueOf(xmlModel.getValidation().isFastForward()));
            pc4ideModel.addProperty(fastForwardInfo, fastForward);
        }

        return pc4ideModel;

    }

    /**
     * Converts valid pc4ide scenario model into PerfCake xml model. It may store required post processors int postProcessor arguments.
     * These post processor should client execute after serialization of returned scenario in order to ensure proper conversion.
     *
     * @param scenarioModel  Model to be converted.
     * @param postProcessors List to which required post processor will be stored.
     * @return XML representation of PerfCake scenario.
     * @throws ModelConversionException if scenarioModel cannot be converted.
     */
    public Scenario convertToXmlModel(ScenarioModel scenarioModel, List<SerializationPostProcessor> postProcessors)
            throws ModelConversionException {

        PostProcessingContext postProcessingContext = new PostProcessingContext();
        Scenario scenario = objectFactory.createScenario();

        // convert scenario properties
        List<Property> scenarioPropertiesModel = scenarioModel.getProperties(PropertyNames.PROPERTIES.toString());
        List<PropertyType> scenarioProperties = extractStandardProperties(scenarioPropertiesModel);
        if (!scenarioProperties.isEmpty()) {
            Scenario.Properties properties = objectFactory.createScenarioProperties();
            properties.getProperty().addAll(scenarioProperties);
            if (scenario.getProperties() == null) {
                scenario.setProperties(objectFactory.createScenarioProperties());
            }
            scenario.getProperties().getProperty().addAll(scenarioProperties);
        }


        // convert generator
        Model generatorModel = scenarioModel.getSingleProperty(PropertyNames.GENERATOR.toString(), Model.class);
        if (generatorModel != null) {
            Scenario.Generator generator = convertGeneratorToXml(generatorModel, postProcessingContext);
            scenario.setGenerator(generator);

            KeyValue runKeyValue = extractSinglePropertyKeyValue(generatorModel, GeneratorModel.PropertyNames.RUN.toString());

            // convert run separately since it is not part of generator in XML scenarioModel
            if (runKeyValue != null) {
                Scenario.Run run = objectFactory.createScenarioRun();
                run.setType(runKeyValue.getKey());
                run.setValue(runKeyValue.getValue());
                scenario.setRun(run);
            }
        }

        // convert sender
        Model senderModel = scenarioModel.getSingleProperty(PropertyNames.SENDER.toString(), Model.class);
        if (senderModel != null) {
            Scenario.Sender sender = convertSenderToXml(senderModel, postProcessingContext);

            scenario.setSender(sender);
        }

        // convert sequences
        List<Property> sequencesModel = scenarioModel.getProperties(PropertyNames.SEQUENCES.toString());
        if (!sequencesModel.isEmpty()) {
            Scenario.Sequences sequences = objectFactory.createScenarioSequences();
            for (Property sequenceModel : sequencesModel) {
                Sequence sequence = convertSequenceToXml(sequenceModel.cast(Model.class), postProcessingContext);
                sequences.getSequence().add(sequence);
            }
            scenario.setSequences(sequences);
        }

        // convert receiver
        Model receiverModel = scenarioModel.getSingleProperty(PropertyNames.RECEIVER.toString(), Model.class);
        if (receiverModel != null) {
            Receiver receiver = convertReceiverToXml(receiverModel, postProcessingContext);

            scenario.setReceiver(receiver);
        }

        // convert reporting properties
        List<Property> reportingPropertiesModel = scenarioModel.getProperties(PropertyNames.REPORTERS_PROPERTIES.toString());
        List<PropertyType> reportingProperties = extractStandardProperties(reportingPropertiesModel);
        if (!reportingProperties.isEmpty()) {

            if (scenario.getReporting() == null) {
                scenario.setReporting(objectFactory.createScenarioReporting());
            }
            scenario.getReporting().getProperty().addAll(reportingProperties);
        }

        // convert reporters
        List<Property> reportersModel = scenarioModel.getProperties(PropertyNames.REPORTERS.toString());
        if (!reportersModel.isEmpty()) {
            if (scenario.getReporting() == null) {
                scenario.setReporting(objectFactory.createScenarioReporting());
            }
            for (Property reporterModel : reportersModel) {
                Reporter reporter = convertReporterToXml(reporterModel.cast(Model.class), postProcessingContext);
                scenario.getReporting().getReporter().add(reporter);
            }
        }

        // convert messages
        List<Property> messagesModel = scenarioModel.getProperties(PropertyNames.MESSAGES.toString());
        if (!messagesModel.isEmpty()) {
            Scenario.Messages messages = objectFactory.createScenarioMessages();

            for (Property messageModel : messagesModel) {
                messages.getMessage().add(convertMessageToXml(messageModel.cast(Model.class), postProcessingContext));
            }

            scenario.setMessages(messages);
        }

        // convert validators
        List<Property> validatorsModel = scenarioModel.getProperties(PropertyNames.VALIDATORS.toString());
        if (!validatorsModel.isEmpty()) {
            Scenario.Validation validation = objectFactory.createScenarioValidation();
            for (Property validatorModel : validatorsModel) {
                validation.getValidator().add(convertValidatorToXml(validatorModel.cast(Model.class), postProcessingContext));
            }
            scenario.setValidation(validation);
        }

        // convert validators fast forward and validators enabled
        if (scenario.getValidation() != null) {
            Value enabled = extractSinglePropertyValue(scenarioModel, PropertyNames.VALIDATION_ENABLED.toString());
            if (enabled != null) {
                scenario.getValidation().setEnabled(Boolean.valueOf(enabled.getValue()));

                if (!enabled.getValue().equalsIgnoreCase("true") && !enabled.getValue().equalsIgnoreCase("false")) {
                    postProcessingContext.getXPathPostProcessor().getExpressions().put("/scenario/validation/@enabled", enabled.getValue());
                }

            }
            Value fastForward = extractSinglePropertyValue(scenarioModel, PropertyNames.VALIDATION_FAST_FORWARD.toString());
            if (fastForward != null) {
                scenario.getValidation().setFastForward(Boolean.valueOf(fastForward.getValue()));
                if (!fastForward.getValue().equalsIgnoreCase("true") && !fastForward.getValue().equalsIgnoreCase("false")) {
                    postProcessingContext.getXPathPostProcessor().getExpressions()
                            .put("/scenario/validation/@fastForward", fastForward.getValue());
                }
            }
        }

        if (!postProcessingContext.getXPathPostProcessor().getExpressions().isEmpty()) {
            String postProcessingProps = String.format("//property[@name=\"{}\"]", POST_PROCESSING_PROPERTY_NAME);
            postProcessingContext.getXpathNodeRemoverPostProcessor().getExpressions().add(postProcessingProps);
        }

        return scenario;
    }

    private Validator convertValidatorToXml(Model validatorModel, PostProcessingContext postProcessingContext) {
        Validator validator = objectFactory.createScenarioValidationValidator();

        Value clazz = extractSinglePropertyValue(validatorModel, ValidatorModel.PropertyNames.IMPLEMENTATION.toString());
        if (clazz != null) {
            validator.setClazz(clazz.getValue());
        }

        Value id = extractSinglePropertyValue(validatorModel, ValidatorModel.PropertyNames.ID.toString());
        if (id != null) {
            validator.setId(id.getValue());
        }

        validator.getProperty().addAll(extractImplProperties(validatorModel));

        return validator;
    }

    private Message convertMessageToXml(Model messageModel, PostProcessingContext postProcessingContext) {

        Message message = objectFactory.createScenarioMessagesMessage();

        Value uri = extractSinglePropertyValue(messageModel, MessageModel.PropertyNames.URI.toString());
        if (uri != null) {
            message.setUri(uri.getValue());
        }
        Value multiplicity = extractSinglePropertyValue(messageModel, MessageModel.PropertyNames.MULTIPLICITY.toString());
        if (multiplicity != null) {
            message.setMultiplicity(multiplicity.getValue());
        }
        Value content = extractSinglePropertyValue(messageModel, MessageModel.PropertyNames.CONTENT.toString());
        if (content != null) {
            message.setContent(content.getValue());
        }

        // convert headers
        List<Property> headersModel = messageModel.getProperties(MessageModel.PropertyNames.HEADERS.toString());
        for (Property headerModel : headersModel) {
            HeaderType header = objectFactory.createHeaderType();
            header.setName(headerModel.cast(KeyValue.class).getKey());
            header.setValue(headerModel.cast(KeyValue.class).getValue());

            message.getHeader().add(header);

        }

        // convert properties
        List<Property> propertiesModel = messageModel.getProperties(MessageModel.PropertyNames.PROPERTIES.toString());
        for (Property propertyModel : propertiesModel) {
            PropertyType property = objectFactory.createPropertyType();
            property.setName(propertyModel.cast(KeyValue.class).getKey());
            property.setValue(propertyModel.cast(KeyValue.class).getValue());

            message.getProperty().add(property);

        }
        // convert validator references
        List<Property> validatorRefsModel = messageModel.getProperties(MessageModel.PropertyNames.VALIDATOR_REFS.toString());
        for (Property validatorRefModel : validatorRefsModel) {
            ValidatorRef validatorRef = objectFactory.createScenarioMessagesMessageValidatorRef();
            validatorRef.setId(validatorRefModel.cast(Value.class).getValue());

            message.getValidatorRef().add(validatorRef);
        }

        return message;
    }

    private Reporter convertReporterToXml(Model reporterModel, PostProcessingContext postProcessingContext) {
        Reporter reporter = objectFactory.createScenarioReportingReporter();

        Value clazz = extractSinglePropertyValue(reporterModel, ReporterModel.PropertyNames.IMPLEMENTATION.toString());
        if (clazz != null) {
            reporter.setClazz(clazz.getValue());
        }

        // convert implementation properties
        reporter.getProperty().addAll(extractImplProperties(reporterModel));


        // convert destinations
        List<Property> destinationsModel = reporterModel.getProperties(ReporterModel.PropertyNames.DESTINATION.toString());

        if (!destinationsModel.isEmpty()) {
            for (Property destinationModel : destinationsModel) {
                Destination destination = convertDestinationToXml(destinationModel.cast(Model.class), postProcessingContext);
                reporter.getDestination().add(destination);
            }
        }

        Value enabledValue = extractSinglePropertyValue(reporterModel, ReporterModel.PropertyNames.ENABLED.toString());
        if (enabledValue != null) {

            Boolean enabled = Boolean.valueOf(enabledValue.getValue());
            reporter.setEnabled(enabled);

            // if value is not simply true or false, then store postprocessing request into the post processing context
            if (enabledValue.getValue().equalsIgnoreCase("true") || enabledValue.getValue().equalsIgnoreCase("false")) {
                PropertyType postProccessingProperty = createPostProcessingProperty(postProcessingContext);
                reporter.getProperty().add(postProccessingProperty);

                // construct post processing xpath
                String xPathExp = createEnabledPostProcessingXpath(Reporter.class, postProccessingProperty);

                postProcessingContext.getXPathPostProcessor().getExpressions().put(xPathExp, enabledValue.getValue());
            }
        }


        return reporter;
    }

    private Destination convertDestinationToXml(Model destinationModel, PostProcessingContext postProcessingContext) {
        Destination destination = objectFactory.createScenarioReportingReporterDestination();

        Value clazz = extractSinglePropertyValue(destinationModel, DestinationModel.PropertyNames.IMPLEMENTATION.toString());
        if (clazz != null) {
            destination.setClazz(clazz.getValue());
        }

        Value enabledValue = extractSinglePropertyValue(destinationModel, DestinationModel.PropertyNames.ENABLED.toString());
        if (enabledValue != null) {
            Boolean enabled = Boolean.valueOf(enabledValue.getValue());
            destination.setEnabled(Boolean.valueOf(enabledValue.getValue()));

            // if value is not simply true or false, then store postprocessing request into the post processing context
            if (enabledValue.getValue().equalsIgnoreCase("true") || enabledValue.getValue().equalsIgnoreCase("false")) {
                PropertyType postProccessingProperty = createPostProcessingProperty(postProcessingContext);
                destination.getProperty().add(postProccessingProperty);

                // construct post processing xpath
                String xPathExp = createEnabledPostProcessingXpath(Destination.class, postProccessingProperty);

                postProcessingContext.getXPathPostProcessor().getExpressions().put(xPathExp, enabledValue.getValue());
            }
        }

        // parse periods
        List<Property> periodsModel = destinationModel.getProperties(DestinationModel.PropertyNames.PERIOD.toString());
        for (Property periodModel : periodsModel) {
            Period period = objectFactory.createScenarioReportingReporterDestinationPeriod();
            period.setType(periodModel.cast(KeyValue.class).getKey());
            period.setValue(periodModel.cast(KeyValue.class).getValue());
            destination.getPeriod().add(period);
        }

        destination.getProperty().addAll(extractImplProperties(destinationModel));

        return destination;
    }

    private Receiver convertReceiverToXml(Model receiverModel, PostProcessingContext postProcessingContext) {
        Receiver receiver = objectFactory.createScenarioReceiver();

        Value clazz = extractSinglePropertyValue(receiverModel, ReceiverModel.PropertyNames.IMPLEMENTATION.toString());
        if (clazz != null) {
            receiver.setClazz(clazz.getValue());
        }

        Value threads = extractSinglePropertyValue(receiverModel, ReceiverModel.PropertyNames.THREADS.toString());
        if (threads != null) {
            receiver.setThreads(threads.getValue());
        }

        Value source = extractSinglePropertyValue(receiverModel, ReceiverModel.PropertyNames.SOURCE.toString());
        if (source != null) {
            receiver.setSource(source.getValue());
        }

        receiver.getProperty().addAll(extractImplProperties(receiverModel));

        Model correlatorModel = receiverModel.getSingleProperty(ReceiverModel.PropertyNames.CORRELATOR.toString(), Model.class);
        if (correlatorModel != null) {
            Correlator correlator = convertCorrelatorToXml(correlatorModel);
            receiver.setCorrelator(correlator);
        }

        return receiver;
    }

    private Correlator convertCorrelatorToXml(Model correlatorModel) {
        Correlator correlator = objectFactory.createScenarioReceiverCorrelator();

        Value clazz = extractSinglePropertyValue(correlatorModel, CorrelatorModel.PropertyNames.IMPLEMENTATION.toString());
        if (clazz != null) {
            correlator.setClazz(clazz.getValue());
        }

        correlator.getProperty().addAll(extractImplProperties(correlatorModel));

        return correlator;
    }

    private Sequence convertSequenceToXml(Model sequenceModel, PostProcessingContext postProcessingContext) {
        Sequence sequence = objectFactory.createScenarioSequencesSequence();
        Value clazz = extractSinglePropertyValue(sequenceModel, SequenceModel.PropertyNames.IMPLEMENTATION.toString());
        if (clazz != null) {
            sequence.setClazz(clazz.getValue());
        }

        Value id = extractSinglePropertyValue(sequenceModel, SequenceModel.PropertyNames.ID.toString());
        if (id != null) {
            sequence.setId(id.getValue());
        }

        sequence.getProperty().addAll(extractImplProperties(sequenceModel));

        return sequence;
    }

    private Scenario.Sender convertSenderToXml(Model senderModel, PostProcessingContext postProcessingContext) {
        Scenario.Sender sender = objectFactory.createScenarioSender();

        Value target = extractSinglePropertyValue(senderModel, SenderModel.PropertyNames.TARGET.toString());
        if (target != null) {
            sender.setTarget(target.getValue());
        }

        Value clazz = extractSinglePropertyValue(senderModel, SenderModel.PropertyNames.IMPLEMENTATION.toString());
        if (clazz != null) {
            sender.setClazz(clazz.getValue());
        }

        sender.getProperty().addAll(extractImplProperties(senderModel));

        return sender;
    }

    private Scenario.Generator convertGeneratorToXml(Model generatorModel, PostProcessingContext postProcessingContext) {
        Scenario.Generator generator = objectFactory.createScenarioGenerator();

        Value clazz = extractSinglePropertyValue(generatorModel, GeneratorModel.PropertyNames.IMPLEMENTATION.toString());

        if (clazz != null) {
            generator.setClazz(clazz.getValue());
        }

        Value threads = extractSinglePropertyValue(generatorModel, GeneratorModel.PropertyNames.THREADS.toString());
        if (threads != null) {
            generator.setThreads(threads.getValue());
        }

        generator.getProperty().addAll(extractImplProperties(generatorModel));

        return generator;
    }

    private ScenarioModel convertGeneratorToModel(Scenario xmlModel, ScenarioModel pc4ideModel) {
        final PropertyInfo generatorInfo = pc4ideModel.getSupportedProperty(PropertyNames.GENERATOR.toString());
        Model generatorModel = new GeneratorModel(docsService);

        // convert implementation
        if (xmlModel.getGenerator().getClazz() != null) {
            PropertyInfo implInfo = generatorModel.getSupportedProperty(GeneratorModel.PropertyNames.IMPLEMENTATION.toString());
            Property impl = new SimpleValue(xmlModel.getGenerator().getClazz());
            generatorModel.addProperty(implInfo, impl);
        }

        // convert run
        if (xmlModel.getRun() != null) {
            PropertyInfo runInfo = generatorModel.getSupportedProperty(GeneratorModel.PropertyNames.RUN.toString());
            KeyValue run = new KeyValueImpl(xmlModel.getRun().getType(), xmlModel.getRun().getValue());
            generatorModel.addProperty(runInfo, run);
        }

        // convert threads
        if (xmlModel.getGenerator().getThreads() != null) {
            PropertyInfo threadsInfo = generatorModel.getSupportedProperty(GeneratorModel.PropertyNames.THREADS.toString());
            Property threads = new SimpleValue(xmlModel.getGenerator().getThreads());
            generatorModel.addProperty(threadsInfo, threads);
        }

        //convert implementation specific properties
        if (xmlModel.getGenerator().getProperty() != null) {
            convertImplementationPropertiesToModel(xmlModel.getGenerator().getProperty(), generatorModel);
        }

        pc4ideModel.addProperty(generatorInfo, generatorModel);

        return pc4ideModel;
    }

    private void convertSenderToModel(Scenario xmlModel, ScenarioModel pc4ideModel) {
        final PropertyInfo senderInfo = pc4ideModel.getSupportedProperty(PropertyNames.SENDER.toString());
        Model senderModel = new SenderModel(docsService);

        // Convert implementation
        if (xmlModel.getSender().getClazz() != null) {
            PropertyInfo implInfo = senderModel.getSupportedProperty(SenderModel.PropertyNames.IMPLEMENTATION.toString());
            Property impl = new SimpleValue(xmlModel.getSender().getClazz());
            senderModel.addProperty(implInfo, impl);
        }

        // Convert target
        if (xmlModel.getSender().getTarget() != null) {
            PropertyInfo targetInfo = senderModel.getSupportedProperty(SenderModel.PropertyNames.TARGET.toString());
            Property target = new SimpleValue(xmlModel.getSender().getTarget());
            senderModel.addProperty(targetInfo, target);
        }

        // convert implementation properties
        if (xmlModel.getSender().getProperty() != null) {
            convertImplementationPropertiesToModel(xmlModel.getSender().getProperty(), senderModel);
        }

        pc4ideModel.addProperty(senderInfo, senderModel);
    }

    private void convertSequencesToModel(List<Sequence> sequences, ScenarioModel pc4ideModel) {
        for (Sequence xmlSequence : sequences) {
            final PropertyInfo sequenceInfo = pc4ideModel.getSupportedProperty(PropertyNames.SEQUENCES.toString());
            Model sequenceModel = new SequenceModel(docsService);

            if (xmlSequence.getClazz() != null) {
                PropertyInfo implInfo = sequenceModel.getSupportedProperty(SequenceModel.PropertyNames.IMPLEMENTATION.toString());
                Property implProperty = new SimpleValue(xmlSequence.getClazz());
                sequenceModel.addProperty(implInfo, implProperty);
            }

            if (xmlSequence.getId() != null) {
                PropertyInfo idInfo = sequenceModel.getSupportedProperty(SequenceModel.PropertyNames.ID.toString());
                Property idProperty = new SimpleValue(xmlSequence.getId());
                sequenceModel.addProperty(idInfo, idProperty);
            }

            if (xmlSequence.getProperty() != null) {
                convertImplementationPropertiesToModel(xmlSequence.getProperty(), sequenceModel);
            }

            pc4ideModel.addProperty(sequenceInfo, sequenceModel);
        }

    }

    private void convertReceiverToModel(Receiver xmlReceiver, ScenarioModel pc4ideModel) {

        final PropertyInfo receiverInfo = pc4ideModel.getSupportedProperty(PropertyNames.RECEIVER.toString());
        Model receiverModel = new ReceiverModel(docsService);

        if (xmlReceiver.getClazz() != null) {
            PropertyInfo implInfo = receiverModel.getSupportedProperty(ReceiverModel.PropertyNames.IMPLEMENTATION.toString());
            Property impl = new SimpleValue(xmlReceiver.getClazz());
            receiverModel.addProperty(implInfo, impl);
        }

        if (xmlReceiver.getThreads() != null) {
            PropertyInfo threadsInfo = receiverModel.getSupportedProperty(ReceiverModel.PropertyNames.THREADS.toString());
            Property threads = new SimpleValue(xmlReceiver.getThreads());
            receiverModel.addProperty(threadsInfo, threads);
        }

        if (xmlReceiver.getSource() != null) {
            PropertyInfo sourceInfo = receiverModel.getSupportedProperty(ReceiverModel.PropertyNames.SOURCE.toString());
            Property source = new SimpleValue(xmlReceiver.getSource());
            receiverModel.addProperty(sourceInfo, source);
        }

        if (xmlReceiver.getProperty() != null) {
            convertImplementationPropertiesToModel(xmlReceiver.getProperty(), receiverModel);
        }

        if (xmlReceiver.getCorrelator() != null) {
            convertCorrelatorToModel(xmlReceiver.getCorrelator(), receiverModel);
        }

        pc4ideModel.addProperty(receiverInfo, receiverModel);
    }

    private void convertCorrelatorToModel(Correlator xmlCorrelator, Model receiverModel) {
        PropertyInfo correlatorInfo = receiverModel.getSupportedProperty(ReceiverModel.PropertyNames.CORRELATOR.toString());
        Model correlatorModel = new CorrelatorModel(docsService);

        if (xmlCorrelator.getClazz() != null) {
            PropertyInfo implInfo = correlatorModel.getSupportedProperty(ReceiverModel.PropertyNames.IMPLEMENTATION.toString());
            Property impl = new SimpleValue(xmlCorrelator.getClazz());
            correlatorModel.addProperty(implInfo, impl);
        }

        if (xmlCorrelator.getProperty() != null) {
            convertImplementationPropertiesToModel(xmlCorrelator.getProperty(), correlatorModel);
        }

        receiverModel.addProperty(correlatorInfo, correlatorModel);
    }

    private void convertReportersToModel(List<Reporter> xmlReporters, ScenarioModel pc4ideModel) {
        PropertyInfo reporterInfo = pc4ideModel.getSupportedProperty(PropertyNames.REPORTERS.toString());

        for (Reporter xmlReporter : xmlReporters) {
            Model reporterModel = new ReporterModel(docsService);

            if (xmlReporter.getClazz() != null) {
                PropertyInfo implInfo = reporterModel.getSupportedProperty(ReporterModel.PropertyNames.IMPLEMENTATION.toString());
                Property impl = new SimpleValue(xmlReporter.getClazz());
                reporterModel.addProperty(implInfo, impl);
            }

            if (xmlReporter.isEnabled()) {
                PropertyInfo enabledInfo = reporterModel.getSupportedProperty(ReporterModel.PropertyNames.ENABLED.toString());
                Property enabled = new SimpleValue("true");
                reporterModel.addProperty(enabledInfo, enabled);
            }

            if (xmlReporter.getDestination() != null) {
                convertDestinationsToModel(xmlReporter.getDestination(), reporterModel);
            }

            if (xmlReporter.getProperty() != null) {
                convertImplementationPropertiesToModel(xmlReporter.getProperty(), reporterModel);
            }

            pc4ideModel.addProperty(reporterInfo, reporterModel);
        }

    }

    private void convertDestinationsToModel(List<Destination> xmlDestinations, Model reporterModel) {
        PropertyInfo destinationInfo = reporterModel.getSupportedProperty(ReporterModel.PropertyNames.DESTINATION.toString());

        for (Destination xmlDestination : xmlDestinations) {
            Model destinationModel = new DestinationModel(docsService);

            if (xmlDestination.getClazz() != null) {
                PropertyInfo implInfo = destinationModel.getSupportedProperty(DestinationModel.PropertyNames.IMPLEMENTATION.toString());
                Property impl = new SimpleValue(xmlDestination.getClazz());
                destinationModel.addProperty(implInfo, impl);
            }

            if (xmlDestination.getPeriod() != null) {
                PropertyInfo periodInfo = destinationModel.getSupportedProperty(DestinationModel.PropertyNames.PERIOD.toString());
                for (Period xmlPeriod : xmlDestination.getPeriod()) {
                    final Property period = new KeyValueImpl(xmlPeriod.getType(), xmlPeriod.getValue());
                    destinationModel.addProperty(periodInfo, period);
                }
            }

            if (xmlDestination.isEnabled()) {
                PropertyInfo enabledInfo = destinationModel.getSupportedProperty(DestinationModel.PropertyNames.ENABLED.toString());
                Property enabled = new SimpleValue("true");
                destinationModel.addProperty(enabledInfo, enabled);
            }

            if (xmlDestination.getProperty() != null) {
                convertImplementationPropertiesToModel(xmlDestination.getProperty(), destinationModel);
            }

            reporterModel.addProperty(destinationInfo, destinationModel);
        }
    }

    private void convertMessagesToModel(List<Message> xmlMessages, ScenarioModel pc4ideModel) {
        PropertyInfo messageInfo = pc4ideModel.getSupportedProperty(PropertyNames.MESSAGES.toString());

        for (Message xmlMessage : xmlMessages) {
            Model messageModel = new MessageModel(docsService);

            if (xmlMessage.getContent() != null) {
                PropertyInfo contentInfo = messageModel.getSupportedProperty(MessageModel.PropertyNames.CONTENT.toString());
                Property content = new SimpleValue(xmlMessage.getContent());
                messageModel.addProperty(contentInfo, content);
            }

            if (xmlMessage.getUri() != null) {
                PropertyInfo uriInfo = messageModel.getSupportedProperty(MessageModel.PropertyNames.URI.toString());
                Property uri = new SimpleValue(xmlMessage.getUri());
                messageModel.addProperty(uriInfo, uri);
            }

            if (xmlMessage.getMultiplicity() != null) {
                PropertyInfo multiplicityInfo = messageModel.getSupportedProperty(MessageModel.PropertyNames.MULTIPLICITY.toString());
                Property multiplicity = new SimpleValue(xmlMessage.getMultiplicity());
                messageModel.addProperty(multiplicityInfo, multiplicity);
            }

            if (xmlMessage.getProperty() != null) {
                PropertyInfo propertyInfo = messageModel.getSupportedProperty(MessageModel.PropertyNames.PROPERTIES.toString());

                for (PropertyType xmlProperty : xmlMessage.getProperty()) {
                    Property message = new KeyValueImpl(xmlProperty.getName(), xmlProperty.getValue());
                    messageModel.addProperty(propertyInfo, message);
                }
            }

            if (xmlMessage.getHeader() != null) {
                PropertyInfo headerInfo = messageModel.getSupportedProperty(MessageModel.PropertyNames.HEADERS.toString());

                for (HeaderType xmlHeader : xmlMessage.getHeader()) {
                    Property header = new KeyValueImpl(xmlHeader.getName(), xmlHeader.getValue());
                    messageModel.addProperty(headerInfo, header);
                }
            }

            if (xmlMessage.getValidatorRef() != null) {
                PropertyInfo validatorRefInfo = messageModel.getSupportedProperty(MessageModel.PropertyNames.VALIDATOR_REFS.toString());

                for (ValidatorRef xmlValidatorRef : xmlMessage.getValidatorRef()) {
                    Property validatorRef = new SimpleValue(xmlValidatorRef.getId());
                    messageModel.addProperty(validatorRefInfo, validatorRef);
                }
            }

            pc4ideModel.addProperty(messageInfo, messageModel);
        }
    }

    private void convertValidatorsToModel(List<Validator> xmlValidators, ScenarioModel pc4ideModel) {
        PropertyInfo validatorInfo = pc4ideModel.getSupportedProperty(PropertyNames.VALIDATORS.toString());

        for (Validator xmlValidator : xmlValidators) {
            Model validatorModel = new ValidatorModel(docsService);

            if (xmlValidator.getClazz() != null) {
                PropertyInfo implInfo = validatorModel.getSupportedProperty(ValidatorModel.PropertyNames.IMPLEMENTATION.toString());
                Property impl = new SimpleValue(xmlValidator.getClazz());
                validatorModel.addProperty(implInfo, impl);
            }

            if (xmlValidator.getId() != null) {
                PropertyInfo idInfo = validatorModel.getSupportedProperty(ValidatorModel.PropertyNames.ID.toString());
                Property id = new SimpleValue(xmlValidator.getId());
                validatorModel.addProperty(idInfo, id);
            }

            convertImplementationPropertiesToModel(xmlValidator.getProperty(), validatorModel);

            pc4ideModel.addProperty(validatorInfo, validatorModel);
        }
    }

    private void convertImplementationPropertiesToModel(List<PropertyType> xmlProperties, Model componentModel) {
        for (PropertyType xmlProperty : xmlProperties) {
            PropertyInfo propertyInfo = componentModel.getSupportedProperty(xmlProperty.getName());

            // if the defined property is supported by current implementation
            if (propertyInfo != null) {
                Property property = new SimpleValue(xmlProperty.getValue());
                componentModel.addProperty(propertyInfo, property);
            } else {
                PropertyInfo implInfo = componentModel.getSupportedProperty(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY);
                Iterator<Property> iterator = componentModel.propertyIterator(implInfo);
                Property currentImplementation = iterator.next();

                logger.warn("Ignoring property {} which is not supported by current inspector ({})",
                        xmlProperty.getName(), currentImplementation.cast(Value.class).getValue());
            }
        }
    }

    private Property extractSingleProperty(Model model, String propertyName) {
        PropertyInfo propertyInfo = model.getSupportedProperty(propertyName);
        if (propertyInfo == null) {
            logger.warn("Trying to get single property with name {}, but no such property found.", propertyName);
            return null;
        }

        List<Property> properties = model.getProperties(propertyInfo);

        if (properties.size() > 1) {
            logger.warn("Multiple properties '{}' exists when trying to get single one.", propertyName);
        }

        Property result = null;

        if (!properties.isEmpty()) {
            result = properties.get(0);
        }

        return result;
    }

    private Value extractSinglePropertyValue(Model model, String propertyName) {
        Property property = extractSingleProperty(model, propertyName);
        Value result = null;

        if (property != null) {
            result = property.cast(Value.class);
        }

        return result;
    }

    private KeyValue extractSinglePropertyKeyValue(Model model, String propertyName) {
        Property property = extractSingleProperty(model, propertyName);
        KeyValue result = null;

        if (property != null) {
            result = property.cast(KeyValue.class);
        }

        return result;
    }

    private List<PropertyType> extractStandardProperties(List<Property> scenarioPropertiesModel) {
        List<PropertyType> scenarioProperties = new ArrayList<>(scenarioPropertiesModel.size());
        for (Property p : scenarioPropertiesModel) {
            PropertyType property = objectFactory.createPropertyType();

            property.setName(p.cast(KeyValue.class).getKey());
            property.setValue(p.cast(KeyValue.class).getValue());
            scenarioProperties.add(property);

            //TODO(jknetl): handle "any" element
        }
        return scenarioProperties;
    }

    private List<PropertyType> extractImplProperties(Model model) {
        List<PropertyType> properties = new ArrayList<>();

        for (PropertyInfo propertyInfo : model.getSupportedImplProperties()) {
            List<Property> propertyModel = model.getProperties(propertyInfo);
            for (Property p : propertyModel) {
                PropertyType propertyType = new PropertyType();
                propertyType.setName(propertyInfo.getName());
                propertyType.setValue(p.cast(Value.class).getValue());
                properties.add(propertyType);
            }
        }

        return properties;
    }

    /**
     * Creates xpath from the class name.
     *
     * @param clazz PerfCake XML model class (Except property and header)
     * @return xpath from root of xml document to the given class.
     */
    private String constructXpath(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz cannot be null");
        }

        StringTokenizer tokenizer = new StringTokenizer(clazz.getCanonicalName(), ".");
        StringBuilder builder = new StringBuilder();
        while (tokenizer.hasMoreTokens()) {
            builder.append("/")
                    .append(tokenizer.nextToken().toLowerCase());
        }

        return builder.toString();
    }

    /**
     * Creates post processing xpath expression for enabled attribute of perfcake xml representation of a component.
     *
     * @param clazz    clazz of the PerfCake XML model which accept properties
     * @param property postprocessing property
     * @return xpath describing the node with given class
     */
    private String createEnabledPostProcessingXpath(Class<?> clazz, PropertyType property) {
        StringBuilder xPathExp = new StringBuilder();
        xPathExp.append(constructXpath(clazz))
                .append(String.format("/property[@name=\"{}\" and @value=\"{}\"]", property.getName(), property.getValue()))
                .append("/../@enabled");

        return xPathExp.toString();
    }

    /**
     * Creates property with post-processing identifier. It also automatically increases context counter
     *
     * @param context post processing  context
     * @return Property which uniquely specifies point of post processing
     */
    private PropertyType createPostProcessingProperty(PostProcessingContext context) {
        PropertyType postProccessingProperty = objectFactory.createPropertyType();
        postProccessingProperty.setName(POST_PROCESSING_PROPERTY_NAME);
        postProccessingProperty.setValue(String.valueOf(context.getPostProcessorCounter()));
        context.increaseCounter();

        return postProccessingProperty;
    }

}
