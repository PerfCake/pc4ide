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
/*
 * Perfclispe
 *
 *
 * Copyright (c) 2014 Jakub Knetl
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
 */

package org.perfcake.ide.core.model;

import java.util.ArrayList;
import java.util.List;

import org.perfcake.model.Scenario;
import org.perfcake.model.Scenario.Generator;
import org.perfcake.model.Scenario.Messages;
import org.perfcake.model.Scenario.Properties;
import org.perfcake.model.Scenario.Receiver;
import org.perfcake.model.Scenario.Reporting;
import org.perfcake.model.Scenario.Run;
import org.perfcake.model.Scenario.Sender;
import org.perfcake.model.Scenario.Sequences;
import org.perfcake.model.Scenario.Validation;

/**
 *  Model of a scenario.
 */
public class ScenarioModel extends AbstractModel {

    public static final String PROPERTY_GENERATOR = "scenario-generator";
    public static final String PROPERTY_SENDER = "scenario-sender";
    public static final String PROPERTY_MESSAGES = "scenario-messages";
    public static final String PROPERTY_PROPERTIES = "scenario-properties";
    public static final String PROPERTY_REPORTING = "scenario-reporting";
    public static final String PROPERTY_VALIDATION = "scenario-validation";
    public static final String PROPERTY_SEQUENCES = "scenario-sequences";
    public static final String PROPERTY_RECEIVER = "scenario-receiver";
    public static final String PROPERTY_RUN = "scenario-run";

    private Scenario scenario;

    ScenarioModel(Scenario scenario) {
        super();
        if (scenario == null) {
            throw new IllegalArgumentException("Scenario must not be null");
        }
        this.scenario = scenario;

        if (scenario.getSender() != null) {
            getMapper().bind(scenario.getSender(), new SenderModel(scenario.getSender()));
        }

        if (scenario.getGenerator() != null) {
            getMapper().bind(scenario.getGenerator(), new GeneratorModel(scenario.getGenerator()));
        }

        if (scenario.getRun() != null) {
            getMapper().bind(scenario.getRun(), new RunModel(scenario.getRun()));
        }

        if (scenario.getReporting() != null) {
            getMapper().bind(scenario.getReporting(), new ReportingModel(scenario.getReporting()));
        }

        if (scenario.getMessages() != null) {
            getMapper().bind(scenario.getMessages(), new MessagesModel(scenario.getMessages()));
        }

        if (scenario.getValidation() != null) {
            getMapper().bind(scenario.getValidation(), new ValidationModel(scenario.getValidation()));
        }

        if (scenario.getSequences() != null) {
            getMapper().bind(scenario.getSequences(), new SequencesModel(scenario.getSequences()));
        }

        if (scenario.getReceiver() != null) {
            getMapper().bind(scenario.getReceiver(), new ReceiverModel(scenario.getReceiver()));
        }

        if (scenario.getProperties() != null) {
            getMapper().bind(scenario.getProperties(), new PropertiesModel(scenario.getProperties()));
        }

    }

    /**
     * Creates new scenario model.
     */
    public ScenarioModel() {
        super();
        this.scenario = new Scenario();
    }

    /**
     * This method should not be used for modifying scenario (in a way getScenario().setSender(sender))
     * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
     *
     * @return PerfCake model of scenario
     */
    Scenario getScenario() {
        return scenario;
    }

    /**
     * Sets sender for scenario.
     *
     * @param sender sender to be set
     */
    public void setSender(SenderModel sender) {
        final Sender oldSender = scenario.getSender();
        AbstractModel oldSenderModel = null;

        scenario.setSender(sender.getSender());
        if (oldSender != null) {
            oldSenderModel = getMapper().getModel(oldSender);
            getMapper().unbind(oldSender, oldSenderModel);
        }

        getMapper().bind(sender.getSender(), sender);
        scenario.setSender(sender.getSender());
        getPropertyChangeSupport().firePropertyChange(PROPERTY_SENDER, oldSenderModel, sender);
    }

    public SenderModel getSender() {
        return (SenderModel) getMapper().getModel(scenario.getSender());
    }

    /**
     * Sets generator for the scenario.
     * @param generator generator instance
     */
    public void setGenerator(GeneratorModel generator) {
        final Generator oldGenerator = scenario.getGenerator();
        AbstractModel oldGeneratorModel = null;
        scenario.setGenerator(generator.getGenerator());
        if (oldGenerator != null) {
            oldGeneratorModel = getMapper().getModel(oldGenerator);
            getMapper().unbind(oldGenerator, oldGeneratorModel);
        }
        getMapper().bind(generator.getGenerator(), generator);
        scenario.setGenerator(generator.getGenerator());
        getPropertyChangeSupport().firePropertyChange(PROPERTY_GENERATOR, oldGeneratorModel, generator);
    }

    public GeneratorModel getGenerator() {
        return (GeneratorModel) getMapper().getModel(scenario.getGenerator());
    }

    /**
     * Sets run for the scenario.
     * @param run run instance
     */
    public void setRun(RunModel run) {
        final Run oldRun = scenario.getRun();
        AbstractModel oldRunModel = null;
        scenario.setRun(run.getRun());
        if (oldRun != null) {
            oldRunModel = getMapper().getModel(oldRun);
            getMapper().unbind(oldRun, oldRunModel);
        }
        getMapper().bind(run.getRun(), run);
        scenario.setRun(run.getRun());
        getPropertyChangeSupport().firePropertyChange(PROPERTY_RUN, oldRunModel, run);
    }

    public RunModel getRun() {
        return (RunModel) getMapper().getModel(scenario.getRun());
    }

    /**
     * Sets reporting for scenario.
     *
     * @param reporting reporting to be set
     */
    public void setReporting(ReportingModel reporting) {
        final Reporting oldReporting = scenario.getReporting();
        AbstractModel oldReportingModel = null;
        scenario.setReporting(reporting.getReporting());
        if (oldReporting != null) {
            oldReportingModel = getMapper().getModel(oldReporting);
            getMapper().unbind(oldReporting, oldReportingModel);
        }
        getMapper().bind(reporting.getReporting(), reporting);
        scenario.setReporting(reporting.getReporting());
        getPropertyChangeSupport().firePropertyChange(PROPERTY_REPORTING, oldReportingModel, reporting);
    }

    public ReportingModel getReporting() {
        return (ReportingModel) getMapper().getModel(scenario.getReporting());
    }

    /**
     * Set messages for the scenario
     * @param messages messages to be set.
     */
    public void setMessages(MessagesModel messages) {
        final Messages oldMessages = scenario.getMessages();
        AbstractModel oldMessagesModel = null;
        scenario.setMessages(messages.getMessages());
        if (oldMessages != null) {
            oldMessagesModel = getMapper().getModel(oldMessages);
            getMapper().unbind(oldMessages, oldMessagesModel);
        }
        getMapper().bind(messages.getMessages(), messages);
        scenario.setMessages(messages.getMessages());
        getPropertyChangeSupport().firePropertyChange(PROPERTY_MESSAGES, oldMessagesModel, messages);
    }

    public MessagesModel getMessages() {
        return (MessagesModel) getMapper().getModel(scenario.getMessages());
    }

    /**
     * Sets validation for the scenario.
     * @param validation validation to be set
     */
    public void setValidation(ValidationModel validation) {
        final Validation oldValidation = scenario.getValidation();
        AbstractModel oldValidationModel = null;
        scenario.setValidation(validation.getValidation());
        if (oldValidation != null) {
            oldValidationModel = getMapper().getModel(oldValidation);
            getMapper().unbind(oldValidation, oldValidationModel);
        }
        getMapper().bind(validation.getValidation(), validation);
        scenario.setValidation(validation.getValidation());
        getPropertyChangeSupport().firePropertyChange(PROPERTY_VALIDATION, oldValidationModel, validation);
    }

    public ValidationModel getValidation() {
        return (ValidationModel) getMapper().getModel(scenario.getValidation());
    }

    public SequencesModel getSequences() {
        return (SequencesModel) getMapper().getModel(scenario.getSequences());
    }

    /**
     * Set sequences for the scenario.
     * @param value sequences instance
     */
    public void setSequences(SequencesModel value) {
        final Sequences oldSequneces = scenario.getSequences();
        AbstractModel oldSequencesModel = null;
        if (oldSequneces != null) {
            oldSequencesModel = getMapper().getModel(oldSequneces);
            getMapper().unbind(oldSequneces, oldSequencesModel);
        }
        getMapper().bind(value.getSequences(), value);
        scenario.setSequences(value.getSequences());
        getPropertyChangeSupport().firePropertyChange(PROPERTY_SEQUENCES, oldSequencesModel, value);
    }

    public ReceiverModel getReceiver() {
        return (ReceiverModel) getMapper().getModel(scenario.getReceiver());
    }

    /**
     * Sets receiver for the scenario.
     *
     * @param value receiver instance
     */
    public void setReceiver(ReceiverModel value) {
        final Receiver oldReceiver = scenario.getReceiver();
        AbstractModel oldReceiverModel = null;
        if (oldReceiver != null) {
            oldReceiverModel = getMapper().getModel(oldReceiver);
            getMapper().unbind(oldReceiver, oldReceiverModel);
        }
        getMapper().bind(value.getReceiver(), value);
        scenario.setReceiver(value.getReceiver());
        getPropertyChangeSupport().firePropertyChange(PROPERTY_RECEIVER, oldReceiverModel, value);
    }

    /**
     * Sets scenario properties.
     *
     * @param properties properties to be set.
     */
    public void setProperties(PropertiesModel properties) {
        final Properties oldProperties = scenario.getProperties();
        AbstractModel oldPropertiesModel = null;
        scenario.setProperties(properties.getProperties());
        if (oldProperties != null) {
            oldPropertiesModel = getMapper().getModel(oldProperties);
            getMapper().unbind(oldProperties, oldPropertiesModel);
        }
        getMapper().bind(properties.getProperties(), properties);
        scenario.setProperties(properties.getProperties());
        getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, oldPropertiesModel, properties);
    }

    public PropertiesModel getProperties() {
        return (PropertiesModel) getMapper().getModel(scenario.getProperties());
    }

    @Override
    public List<AbstractModel> getModelChildren() {
        final List<AbstractModel> children = new ArrayList<>();
        children.add(getGenerator());
        children.add(getSender());
        children.add(getMessages());
        children.add(getReporting());
        children.add(getRun());
        children.add(getValidation());
        children.add(getProperties());
        return children;
    }
}
