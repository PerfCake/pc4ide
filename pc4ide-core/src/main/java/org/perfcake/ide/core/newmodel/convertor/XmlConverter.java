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

package org.perfcake.ide.core.newmodel.convertor;

import java.util.Iterator;
import java.util.List;
import org.perfcake.ide.core.components.ComponentManager;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.newmodel.AbstractModel;
import org.perfcake.ide.core.newmodel.Model;
import org.perfcake.ide.core.newmodel.Property;
import org.perfcake.ide.core.newmodel.PropertyInfo;
import org.perfcake.ide.core.newmodel.modeltypes.CorrelatorModel;
import org.perfcake.ide.core.newmodel.modeltypes.DestinationModel;
import org.perfcake.ide.core.newmodel.modeltypes.GeneratorModel;
import org.perfcake.ide.core.newmodel.modeltypes.MessageModel;
import org.perfcake.ide.core.newmodel.modeltypes.ReceiverModel;
import org.perfcake.ide.core.newmodel.modeltypes.ReporterModel;
import org.perfcake.ide.core.newmodel.modeltypes.ScenarioModel;
import org.perfcake.ide.core.newmodel.modeltypes.ScenarioModel.PropertyNames;
import org.perfcake.ide.core.newmodel.modeltypes.SenderModel;
import org.perfcake.ide.core.newmodel.modeltypes.SequenceModel;
import org.perfcake.ide.core.newmodel.modeltypes.ValidatorModel;
import org.perfcake.ide.core.newmodel.simple.KeyValue;
import org.perfcake.ide.core.newmodel.simple.KeyValueImpl;
import org.perfcake.ide.core.newmodel.simple.SimpleValue;
import org.perfcake.ide.core.newmodel.simple.Value;
import org.perfcake.model.HeaderType;
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

    private ComponentManager manager;

    public XmlConverter(ComponentManager manager) {
        this.manager = manager;
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

        ScenarioModel pc4ideModel = new ScenarioModel(manager);

        // Convert scenario properties
        if (xmlModel.getProperties() != null) {
            PropertyInfo propertyInfo = pc4ideModel.getSupportedProperty(PropertyNames.PROPERTIES.toString());
            for (PropertyType xmlProperty : xmlModel.getProperties().getProperty()) {
                KeyValue propertyContent = new KeyValueImpl(xmlProperty.getName(), xmlProperty.getValue());
                Property pc4ideProperty = new Property(propertyInfo, propertyContent);
                pc4ideModel.addProperty(propertyInfo, pc4ideProperty);
            }
        }

        // Convert generator
        if (xmlModel.getGenerator() != null) {
            convertGenerator(xmlModel, pc4ideModel);
        }

        // Convert sender
        if (xmlModel.getSender() != null) {
            convertSender(xmlModel, pc4ideModel);
        }

        // convert sequences
        if (xmlModel.getSequences() != null && xmlModel.getSequences().getSequence() != null) {
            convertSequences(xmlModel.getSequences().getSequence(), pc4ideModel);
        }

        // convert Receiver
        if (xmlModel.getReceiver() != null) {
            convertReceiver(xmlModel.getReceiver(), pc4ideModel);
        }

        // convert reporters
        if (xmlModel.getReporting() != null) {
            if (xmlModel.getReporting().getReporter() != null) {
                convertReporters(xmlModel.getReporting().getReporter(), pc4ideModel);
            }

            PropertyInfo propertyInfo = pc4ideModel.getSupportedProperty(PropertyNames.REPORTERS_PROPERTIES.toString());
            for (PropertyType xmlProperty : xmlModel.getReporting().getProperty()) {
                Property property = new Property(propertyInfo,
                        new KeyValueImpl(xmlProperty.getName(), xmlProperty.getValue()));
                pc4ideModel.addProperty(propertyInfo, property);
            }
        }

        //
        if (xmlModel.getMessages() != null && xmlModel.getMessages().getMessage() != null) {
            convertMessages(xmlModel.getMessages().getMessage(), pc4ideModel);
        }

        if (xmlModel.getValidation() != null && xmlModel.getValidation().getValidator() != null) {
            convertValidators(xmlModel.getValidation().getValidator(), pc4ideModel);
        }

        if (xmlModel.getValidation() != null) {

            PropertyInfo enabledInfo = pc4ideModel.getSupportedProperty(PropertyNames.VALIDATION_ENABLED.toString());
            Property enabled = new Property(enabledInfo,
                    new SimpleValue(String.valueOf(xmlModel.getValidation().isEnabled())));
            pc4ideModel.addProperty(enabledInfo, enabled);

            PropertyInfo fastForwardInfo = pc4ideModel.getSupportedProperty(PropertyNames.VALIDATION_FAST_FORWARD.toString());
            Property fastForward = new Property(fastForwardInfo,
                    new SimpleValue(String.valueOf(xmlModel.getValidation().isFastForward())));
            pc4ideModel.addProperty(fastForwardInfo, fastForward);
        }

        return pc4ideModel;

    }

    private ScenarioModel convertGenerator(Scenario xmlModel, ScenarioModel pc4ideModel) {
        final PropertyInfo generatorInfo = pc4ideModel.getSupportedProperty(PropertyNames.GENERATOR.toString());
        Model generatorModel = new GeneratorModel(manager);

        // convert implementation
        if (xmlModel.getGenerator().getClazz() != null) {
            PropertyInfo implInfo = generatorModel.getSupportedProperty(GeneratorModel.PropertyNames.IMPLEMENTATION.toString());
            Property impl = new Property(implInfo, new SimpleValue(xmlModel.getGenerator().getClazz()));
            generatorModel.addProperty(implInfo, impl);
        }

        // convert run
        if (xmlModel.getRun() != null) {
            PropertyInfo runInfo = generatorModel.getSupportedProperty(GeneratorModel.PropertyNames.RUN.toString());
            KeyValue run = new KeyValueImpl(xmlModel.getRun().getType(), xmlModel.getRun().getValue());
            Property runProperty = new Property(runInfo, run);
            generatorModel.addProperty(runInfo, runProperty);
        }

        // convert threads
        if (xmlModel.getGenerator().getThreads() != null) {
            PropertyInfo threadsInfo = generatorModel.getSupportedProperty(GeneratorModel.PropertyNames.THREADS.toString());
            Property threads = new Property(threadsInfo, new SimpleValue(xmlModel.getGenerator().getThreads()));
            generatorModel.addProperty(threadsInfo, threads);
        }

        //convert implementation specific properties
        if (xmlModel.getGenerator().getProperty() != null) {
            convertImplementationProperties(xmlModel.getGenerator().getProperty(), generatorModel);
        }

        Property generatorProperty = new Property(generatorInfo, generatorModel);
        pc4ideModel.addProperty(generatorInfo, generatorProperty);

        return pc4ideModel;
    }

    private void convertSender(Scenario xmlModel, ScenarioModel pc4ideModel) {
        final PropertyInfo senderInfo = pc4ideModel.getSupportedProperty(PropertyNames.SENDER.toString());
        Model senderModel = new SenderModel(manager);

        // Convert implementation
        if (xmlModel.getSender().getClazz() != null) {
            PropertyInfo implInfo = senderModel.getSupportedProperty(SenderModel.PropertyNames.IMPLEMENTATION.toString());
            Property impl = new Property(implInfo, new SimpleValue(xmlModel.getSender().getClazz()));
            senderModel.addProperty(implInfo, impl);
        }

        // Convert target
        if (xmlModel.getSender().getTarget() != null) {
            PropertyInfo targetInfo = senderModel.getSupportedProperty(SenderModel.PropertyNames.TARGET.toString());
            Property target = new Property(targetInfo, new SimpleValue(xmlModel.getSender().getTarget()));
            senderModel.addProperty(targetInfo, target);
        }

        // convert implementation properties
        if (xmlModel.getSender().getProperty() != null) {
            convertImplementationProperties(xmlModel.getSender().getProperty(), senderModel);
        }

        Property senderProperty = new Property(senderInfo, senderModel);
        pc4ideModel.addProperty(senderInfo, senderProperty);
    }

    private void convertSequences(List<Sequence> sequences, ScenarioModel pc4ideModel) {
        for (Sequence xmlSequence : sequences) {
            final PropertyInfo sequenceInfo = pc4ideModel.getSupportedProperty(PropertyNames.SEQUENCES.toString());
            Model sequenceModel = new SequenceModel(manager);

            if (xmlSequence.getClazz() != null) {
                PropertyInfo implInfo = sequenceModel.getSupportedProperty(SequenceModel.PropertyNames.IMPLEMENTATION.toString());
                Property implProperty = new Property(implInfo, new SimpleValue(xmlSequence.getClazz()));
                sequenceModel.addProperty(implInfo, implProperty);
            }

            if (xmlSequence.getId() != null) {
                PropertyInfo idInfo = sequenceModel.getSupportedProperty(SequenceModel.PropertyNames.ID.toString());
                Property idProperty = new Property(idInfo, new SimpleValue(xmlSequence.getId()));
                sequenceModel.addProperty(idInfo, idProperty);
            }

            if (xmlSequence.getProperty() != null) {
                convertImplementationProperties(xmlSequence.getProperty(), sequenceModel);
            }

            pc4ideModel.addProperty(sequenceInfo, new Property(sequenceInfo, sequenceModel));
        }

    }

    private void convertReceiver(Receiver xmlReceiver, ScenarioModel pc4ideModel) {

        final PropertyInfo receiverInfo = pc4ideModel.getSupportedProperty(PropertyNames.RECEIVER.toString());
        Model receiverModel = new ReceiverModel(manager);

        if (xmlReceiver.getClazz() != null) {
            PropertyInfo implInfo = receiverModel.getSupportedProperty(ReceiverModel.PropertyNames.IMPLEMENTATION.toString());
            Property impl = new Property(implInfo, new SimpleValue(xmlReceiver.getClazz()));
            receiverModel.addProperty(implInfo, impl);
        }

        if (xmlReceiver.getThreads() != null) {
            PropertyInfo threadsInfo = receiverModel.getSupportedProperty(ReceiverModel.PropertyNames.THREADS.toString());
            Property threads = new Property(threadsInfo, new SimpleValue(xmlReceiver.getThreads()));
            receiverModel.addProperty(threadsInfo, threads);
        }

        if (xmlReceiver.getSource() != null) {
            PropertyInfo sourceInfo = receiverModel.getSupportedProperty(ReceiverModel.PropertyNames.SOURCE.toString());
            Property source = new Property(sourceInfo, new SimpleValue(xmlReceiver.getSource()));
        }

        if (xmlReceiver.getProperty() != null) {
            convertImplementationProperties(xmlReceiver.getProperty(), receiverModel);
        }

        if (xmlReceiver.getCorrelator() != null) {
            convertCorrelator(xmlReceiver.getCorrelator(), receiverModel);
        }

        pc4ideModel.addProperty(receiverInfo, new Property(receiverInfo, receiverModel));
    }

    private void convertCorrelator(Correlator xmlCorrelator, Model receiverModel) {
        PropertyInfo correlatorInfo = receiverModel.getSupportedProperty(ReceiverModel.PropertyNames.CORRELATOR.toString());
        Model correlatorModel = new CorrelatorModel(manager);

        if (xmlCorrelator.getClazz() != null) {
            PropertyInfo implInfo = correlatorModel.getSupportedProperty(ReceiverModel.PropertyNames.IMPLEMENTATION.toString());
            Property impl = new Property(implInfo, new SimpleValue(xmlCorrelator.getClazz()));
            correlatorModel.addProperty(implInfo, impl);
        }

        if (xmlCorrelator.getProperty() != null) {
            convertImplementationProperties(xmlCorrelator.getProperty(), correlatorModel);
        }

        receiverModel.addProperty(correlatorInfo, new Property(correlatorInfo, correlatorModel));
    }

    private void convertReporters(List<Reporter> xmlReporters, ScenarioModel pc4ideModel) {
        PropertyInfo reporterInfo = pc4ideModel.getSupportedProperty(PropertyNames.REPORTERS.toString());

        for (Reporter xmlReporter : xmlReporters) {
            Model reporterModel = new ReporterModel(manager);

            if (xmlReporter.getClazz() != null) {
                PropertyInfo implInfo = reporterModel.getSupportedProperty(ReporterModel.PropertyNames.IMPLEMENTATION.toString());
                Property impl = new Property(implInfo, new SimpleValue(xmlReporter.getClazz()));
                reporterModel.addProperty(implInfo, impl);
            }

            if (xmlReporter.isEnabled()) {
                PropertyInfo enabledInfo = reporterModel.getSupportedProperty(ReporterModel.PropertyNames.ENABLED.toString());
                Property enabled = new Property(enabledInfo, new SimpleValue("true"));
                reporterModel.addProperty(enabledInfo, enabled);
            }

            if (xmlReporter.getDestination() != null) {
                convertDestinations(xmlReporter.getDestination(), reporterModel);
            }

            if (xmlReporter.getProperty() != null) {
                convertImplementationProperties(xmlReporter.getProperty(), reporterModel);
            }

            pc4ideModel.addProperty(reporterInfo, new Property(reporterInfo, reporterModel));
        }

    }

    private void convertDestinations(List<Destination> xmlDestinations, Model reporterModel) {
        PropertyInfo destinationInfo = reporterModel.getSupportedProperty(ReporterModel.PropertyNames.DESTINATION.toString());

        for (Destination xmlDestination : xmlDestinations) {
            Model destinationModel = new DestinationModel(manager);

            if (xmlDestination.getClazz() != null) {
                PropertyInfo implInfo = destinationModel.getSupportedProperty(DestinationModel.PropertyNames.IMPLEMENTATION.toString());
                Property impl = new Property(implInfo, new SimpleValue(xmlDestination.getClazz()));
                destinationModel.addProperty(implInfo, impl);
            }

            if (xmlDestination.getPeriod() != null) {
                PropertyInfo periodInfo = destinationModel.getSupportedProperty(DestinationModel.PropertyNames.PERIOD.toString());
                for (Period xmlPeriod : xmlDestination.getPeriod()) {
                    final Property period = new Property(periodInfo,
                            new KeyValueImpl(xmlPeriod.getType(), xmlPeriod.getValue()));
                    destinationModel.addProperty(periodInfo, period);
                }
            }

            if (xmlDestination.getProperty() != null) {
                convertImplementationProperties(xmlDestination.getProperty(), destinationModel);
            }

            reporterModel.addProperty(destinationInfo, new Property(destinationInfo, destinationModel));
        }
    }

    private void convertMessages(List<Message> xmlMessages, ScenarioModel pc4ideModel) {
        PropertyInfo messageInfo = pc4ideModel.getSupportedProperty(PropertyNames.MESSAGES.toString());

        for (Message xmlMessage : xmlMessages) {
            Model messageModel = new MessageModel(manager);

            if (xmlMessage.getContent() != null) {
                PropertyInfo contentInfo = messageModel.getSupportedProperty(MessageModel.PropertyNames.CONTENT.toString());
                Property content = new Property(contentInfo, new SimpleValue(xmlMessage.getContent()));
                messageModel.addProperty(contentInfo, content);
            }

            if (xmlMessage.getUri() != null) {
                PropertyInfo uriInfo = messageModel.getSupportedProperty(MessageModel.PropertyNames.URI.toString());
                Property uri = new Property(uriInfo, new SimpleValue(xmlMessage.getUri()));
                messageModel.addProperty(uriInfo, uri);
            }

            if (xmlMessage.getMultiplicity() != null) {
                PropertyInfo multiplicityInfo = messageModel.getSupportedProperty(MessageModel.PropertyNames.MULTIPLICITY.toString());
                Property multiplicity = new Property(multiplicityInfo, new SimpleValue(xmlMessage.getMultiplicity()));
                messageModel.addProperty(multiplicityInfo, multiplicity);
            }

            if (xmlMessage.getProperty() != null) {
                PropertyInfo propertyInfo = messageModel.getSupportedProperty(MessageModel.PropertyNames.PROPERTIES.toString());

                for (PropertyType xmlProperty : xmlMessage.getProperty()) {
                    Property message = new Property(propertyInfo,
                            new KeyValueImpl(xmlProperty.getName(), xmlProperty.getValue()));
                    messageModel.addProperty(propertyInfo, message);
                }
            }

            if (xmlMessage.getHeader() != null) {
                PropertyInfo headerInfo = messageModel.getSupportedProperty(MessageModel.PropertyNames.HEADERS.toString());

                for (HeaderType xmlHeader : xmlMessage.getHeader()) {
                    Property header = new Property(headerInfo, new KeyValueImpl(xmlHeader.getName(), xmlHeader.getValue()));
                    messageModel.addProperty(headerInfo, header);
                }
            }

            if (xmlMessage.getValidatorRef() != null) {
                PropertyInfo validatorRefInfo = messageModel.getSupportedProperty(MessageModel.PropertyNames.VALIDATOR_REFS.toString());

                for (ValidatorRef xmlValidatorRef : xmlMessage.getValidatorRef()) {
                    Property validatorRef = new Property(validatorRefInfo, new SimpleValue(xmlValidatorRef.getId()));
                    messageModel.addProperty(validatorRefInfo, validatorRef);
                }
            }

            pc4ideModel.addProperty(messageInfo, new Property(messageInfo, messageModel));
        }
    }

    private void convertValidators(List<Validator> xmlValidators, ScenarioModel pc4ideModel) {
        PropertyInfo validatorInfo = pc4ideModel.getSupportedProperty(PropertyNames.VALIDATORS.toString());

        for (Validator xmlValidator : xmlValidators) {
            Model validatorModel = new ValidatorModel(manager);

            if (xmlValidator.getClazz() != null) {
                PropertyInfo implInfo = validatorModel.getSupportedProperty(ValidatorModel.PropertyNames.IMPLEMENTATION.toString());
                Property impl = new Property(implInfo, new SimpleValue(xmlValidator.getClazz()));
                validatorModel.addProperty(implInfo, impl);
            }

            if (xmlValidator.getId() != null) {
                PropertyInfo idInfo = validatorModel.getSupportedProperty(ValidatorModel.PropertyNames.ID.toString());
                Property id = new Property(idInfo, new SimpleValue(xmlValidator.getId()));
                validatorModel.addProperty(idInfo, id);
            }

            pc4ideModel.addProperty(validatorInfo, new Property(validatorInfo, validatorModel));
        }
    }

    private void convertImplementationProperties(List<PropertyType> xmlProperties, Model componentModel) {
        for (PropertyType xmlProperty : xmlProperties) {
            PropertyInfo propertyInfo = componentModel.getSupportedProperty(xmlProperty.getName());

            // if the defined property is supported by current implementation
            if (propertyInfo != null) {
                Property property = new Property(propertyInfo,
                        new KeyValueImpl(xmlProperty.getName(), xmlProperty.getValue()));
                componentModel.addProperty(propertyInfo, property);
            } else {
                PropertyInfo implInfo = componentModel.getSupportedProperty(AbstractModel.IMPLEMENTATION_CLASS_PROPERTY);
                Iterator<Property> iterator = componentModel.propertyIterator(implInfo);
                Property currentImplementation = iterator.next();

                logger.warn("Ignoring property {} which is not supported by current component ({})",
                        xmlProperty.getName(), currentImplementation.getValue(Value.class).getValue());
            }
        }
    }
}
