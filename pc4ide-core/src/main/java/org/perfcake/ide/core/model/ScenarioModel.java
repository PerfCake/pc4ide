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

import org.perfcake.model.Scenario;
import org.perfcake.model.Scenario.Generator;
import org.perfcake.model.Scenario.Messages;
import org.perfcake.model.Scenario.Properties;
import org.perfcake.model.Scenario.Reporting;
import org.perfcake.model.Scenario.Run;
import org.perfcake.model.Scenario.Sender;
import org.perfcake.model.Scenario.Validation;

public class ScenarioModel extends AbstractModel {

	public static final String PROPERTY_GENERATOR = "scenario-generator";
	public static final String PROPERTY_SENDER = "scenario-sender";
	public static final String PROPERTY_MESSAGES = "scenario-messages";
	public static final String PROPERTY_PROPERTIES = "scenario-properties";
	public static final String PROPERTY_REPORTING = "scenario-reporting";
	public static final String PROPERTY_VALIDATION = "scenario-validation";
	public static final String PROPERTY_RUN = "scenario-run";

	private Scenario scenario;

	ScenarioModel(Scenario scenario) {
		super();
		if (scenario == null) {
			throw new IllegalArgumentException("Scenario must not be null");
		}
		this.scenario = scenario;
	}

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

	public void setSender(SenderModel sender) {
		final Sender oldSender = scenario.getSender();
		AbstractModel oldSenderModel = null;

		scenario.setSender(sender.getSender());
		if (oldSender != null) {
			oldSenderModel = getMapper().getModel(oldSender);
			getMapper().unbind(oldSender, oldSenderModel);
		}

		getMapper().bind(sender.getSender(), sender);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_SENDER, oldSenderModel, sender);
	}

	public void setGenerator(GeneratorModel generator) {
		final Generator oldGenerator = scenario.getGenerator();
		AbstractModel oldGeneratorModel = null;
		scenario.setGenerator(generator.getGenerator());
		if (oldGenerator != null) {
			oldGeneratorModel = getMapper().getModel(oldGenerator);
			getMapper().unbind(oldGenerator, oldGeneratorModel);
		}
		getMapper().bind(generator.getGenerator(), generator);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_GENERATOR, oldGeneratorModel, generator);
	}

	public void setRun(RunModel run) {
		final Run oldRun = scenario.getRun();
		AbstractModel oldRunModel = null;
		scenario.setRun(run.getRun());
		if (oldRun != null) {
			oldRunModel = getMapper().getModel(oldRun);
			getMapper().unbind(oldRun, oldRunModel);
		}
		getMapper().bind(run.getRun(), run);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_RUN, oldRunModel, run);
	}

	public void setReporting(ReportingModel reporting) {
		final Reporting oldReporting = scenario.getReporting();
		AbstractModel oldReportingModel = null;
		scenario.setReporting(reporting.getReporting());
		if (oldReporting != null) {
			oldReportingModel = getMapper().getModel(oldReporting);
			getMapper().unbind(oldReporting, oldReportingModel);
		}
		getMapper().bind(reporting.getReporting(), reporting);

		getPropertyChangeSupport().firePropertyChange(PROPERTY_REPORTING, oldReportingModel, reporting);
	}

	public void setMessages(MessagesModel messages) {
		final Messages oldMessages = scenario.getMessages();
		AbstractModel oldMessagesModel = null;
		scenario.setMessages(messages.getMessages());
		if (oldMessages != null) {
			oldMessagesModel = getMapper().getModel(oldMessages);
			getMapper().unbind(oldMessages, oldMessagesModel);
		}
		getMapper().bind(messages.getMessages(), messages);

		getPropertyChangeSupport().firePropertyChange(PROPERTY_MESSAGES, oldMessagesModel, messages);
	}

	public void setValidation(ValidationModel validation) {
		final Validation oldValidation = scenario.getValidation();
		AbstractModel oldValidationModel = null;
		scenario.setValidation(validation.getValidation());
		if (oldValidation != null) {
			oldValidationModel = getMapper().getModel(oldValidation);
			getMapper().unbind(oldValidation, oldValidationModel);
		}
		getMapper().bind(validation.getValidation(), validation);

		getPropertyChangeSupport().firePropertyChange(PROPERTY_VALIDATION, oldValidationModel, validation);
	}

	public void setProperties(PropertiesModel properties) {
		final Properties oldProperties = scenario.getProperties();
		AbstractModel oldPropertiesModel = null;
		scenario.setProperties(properties.getProperties());
		if (oldProperties != null) {
			oldPropertiesModel = getMapper().getModel(oldProperties);
			getMapper().unbind(oldProperties, oldPropertiesModel);
		}
		getMapper().bind(properties.getProperties(), properties);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, oldPropertiesModel, properties);
	}
}