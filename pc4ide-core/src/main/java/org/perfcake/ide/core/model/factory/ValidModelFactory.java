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

package org.perfcake.ide.core.model.factory;

import com.sun.tools.javac.jvm.Gen;
import org.perfcake.ide.core.components.ComponentCatalogue;
import org.perfcake.ide.core.components.PerfCakeComponent;
import org.perfcake.ide.core.docs.DocsService;
import org.perfcake.ide.core.model.Model;
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
import org.perfcake.ide.core.model.properties.KeyValue;
import org.perfcake.ide.core.model.properties.KeyValueImpl;
import org.perfcake.ide.core.model.properties.SimpleValue;
import org.perfcake.ide.core.model.properties.Value;
import org.perfcake.ide.core.model.validation.Validator;
import org.perfcake.message.correlator.GenerateHeaderCorrelator;
import org.perfcake.message.generator.DefaultMessageGenerator;
import org.perfcake.message.receiver.HttpReceiver;
import org.perfcake.message.sender.DummySender;
import org.perfcake.message.sequence.PrimitiveNumberSequence;
import org.perfcake.reporting.destination.ConsoleDestination;
import org.perfcake.reporting.reporter.IterationsPerSecondReporter;
import org.perfcake.validation.RegExpValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ValidModelFactory is a {@link ModelFactory}, which produces model objects which are immediately valid. It fills model object with all
 * required data. Therefore, if model object require a submodel reference. Then the submodel object is also created and referenced from the
 * model.
 *
 * @author Jakub Knetl
 */
public class ValidModelFactory implements ModelFactory {

    static final Logger logger = LoggerFactory.getLogger(ValidModelFactory.class);

    /**
     * DocsService injected into created models.
     */
    private DocsService docsService;
    private ComponentCatalogue componentCatalogue;

    /**
     * Creates new ValidModeFactory.
     *
     * @param docsService        documentation service
     * @param componentCatalogue catalogue of PerfCake component implementations.
     */
    public ValidModelFactory(DocsService docsService, ComponentCatalogue componentCatalogue) {
        this.docsService = docsService;
        this.componentCatalogue = componentCatalogue;
    }

    @Override
    public Model createModel(PerfCakeComponent component) {
        if (component == null) {
            throw new IllegalArgumentException("component must not be null");
        }

        Model model = null;

        switch (component) {
            case SEQUENCE:
                model = createSequence();
                break;
            case REPORTER:
                model = createReporter();
                break;
            case CORRELATOR:
                model = createCorrelator();
                break;
            case DESTINATION:
                model = createDestination();
                break;
            case GENERATOR:
                model = createGenerator();
                break;
            case MESSAGE:
                model = createMessage();
                break;
            case RECEIVER:
                model = createReceiver();
                break;
            case SCENARIO:
                model = createScenario();
                break;
            case SENDER:
                model = createSender();
                break;
            case VALIDATOR:
                model = createValidator();
                break;
            default:
                logger.warn("No model object created for component. Unknown type of component: {}.", component);

        }

        return model;
    }

    private Model createScenario() {
        Model scenario = new ScenarioModel(docsService);

        scenario.addProperty(ScenarioModel.PropertyNames.GENERATOR.toString(), createGenerator());
        scenario.addProperty(ScenarioModel.PropertyNames.SENDER.toString(), createSender());
        scenario.addProperty(ScenarioModel.PropertyNames.REPORTERS.toString(), createReporter());

        return scenario;
    }

    private Model createValidator() {
        Model validator = new ValidatorModel(docsService);

        Value impl = new SimpleValue(RegExpValidator.class.getSimpleName());
        Value id = new SimpleValue("regexp-validator");

        validator.addProperty(ValidatorModel.PropertyNames.IMPLEMENTATION.toString(), impl);
        validator.addProperty(ValidatorModel.PropertyNames.ID.toString(), id);

        return validator;
    }

    private Model createSender() {
        Model sender = new SenderModel(docsService);

        Value impl = new SimpleValue(DummySender.class.getSimpleName());
        Value target = new SimpleValue("sender-target");

        sender.addProperty(SenderModel.PropertyNames.IMPLEMENTATION.toString(), impl);
        sender.addProperty(SenderModel.PropertyNames.TARGET.toString(), target);

        return sender;
    }

    private Model createReceiver() {
        Model receiver = new ReceiverModel(docsService);

        Value impl = new SimpleValue(HttpReceiver.class.getSimpleName());
        Value source = new SimpleValue("http://localhost:8080/source-url");
        Model correlator = createCorrelator();

        receiver.addProperty(ReceiverModel.PropertyNames.IMPLEMENTATION.toString(), impl);
        receiver.addProperty(ReceiverModel.PropertyNames.SOURCE.toString(), source);
        receiver.addProperty(ReceiverModel.PropertyNames.CORRELATOR.toString(), correlator);

        return receiver;

    }

    private Model createMessage() {
        Model message = new MessageModel(docsService);

        return message;
    }

    private Model createGenerator() {
        Model generator = new GeneratorModel(docsService);

        Value impl = new SimpleValue(DefaultMessageGenerator.class.getSimpleName());
        KeyValue run = new KeyValueImpl("time", "10000");
        Value threads = new SimpleValue("1");

        generator.addProperty(GeneratorModel.PropertyNames.IMPLEMENTATION.toString(), impl);
        generator.addProperty(GeneratorModel.PropertyNames.RUN.toString(), run);
        generator.addProperty(GeneratorModel.PropertyNames.THREADS.toString(), threads);

        return generator;
    }

    private Model createReporter() {
        Model reporter = new ReporterModel(docsService);

        Value impl = new SimpleValue(IterationsPerSecondReporter.class.getSimpleName());
        reporter.addProperty(ReporterModel.PropertyNames.IMPLEMENTATION.toString(), impl);

        Model destination = createDestination();
        reporter.addProperty(ReporterModel.PropertyNames.DESTINATION.toString(), destination);

        return reporter;


    }

    private Model createDestination() {
        Model destination = new DestinationModel(docsService);

        Value impl = new SimpleValue(ConsoleDestination.class.getSimpleName());
        KeyValue period = new KeyValueImpl("time","5000");
        destination.addProperty(DestinationModel.PropertyNames.IMPLEMENTATION.toString(), impl);
        destination.addProperty(DestinationModel.PropertyNames.PERIOD.toString(), period);

        return destination;
    }

    private Model createCorrelator() {
        Model correlator = new CorrelatorModel(docsService);

        Value impl = new SimpleValue(GenerateHeaderCorrelator.class.getSimpleName());
        correlator.addProperty(CorrelatorModel.PropertyNames.IMPLEMENTATION.toString(), impl);

        return correlator;
    }

    private Model createSequence() {
        Model sequence = new SequenceModel(docsService);

        Value impl = new SimpleValue(PrimitiveNumberSequence.class.getSimpleName());
        sequence.addProperty(SequenceModel.PropertyNames.IMPLEMENTATION.toString(), impl);

        Value id = new SimpleValue("primitive-sequence");
        sequence.addProperty(SequenceModel.PropertyNames.ID.toString(), id);

        return sequence;
    }

    public DocsService getDocsService() {
        return docsService;
    }

    public void setDocsService(DocsService docsService) {
        this.docsService = docsService;
    }

    public ComponentCatalogue getComponentCatalogue() {
        return componentCatalogue;
    }

    public void setComponentCatalogue(ComponentCatalogue componentCatalogue) {
        this.componentCatalogue = componentCatalogue;
    }

}
