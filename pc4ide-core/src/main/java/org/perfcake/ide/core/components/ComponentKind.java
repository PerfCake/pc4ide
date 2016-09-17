package org.perfcake.ide.core.components;

import org.perfcake.RunInfo;
import org.perfcake.ide.core.model.CorrelatorModel;
import org.perfcake.ide.core.model.DestinationModel;
import org.perfcake.ide.core.model.GeneratorModel;
import org.perfcake.ide.core.model.MessageModel;
import org.perfcake.ide.core.model.PeriodModel;
import org.perfcake.ide.core.model.ReceiverModel;
import org.perfcake.ide.core.model.ReporterModel;
import org.perfcake.ide.core.model.ReportingModel;
import org.perfcake.ide.core.model.RunModel;
import org.perfcake.ide.core.model.ScenarioModel;
import org.perfcake.ide.core.model.SenderModel;
import org.perfcake.ide.core.model.SequenceModel;
import org.perfcake.ide.core.model.SequencesModel;
import org.perfcake.ide.core.model.ValidationModel;
import org.perfcake.ide.core.model.ValidatorModel;
import org.perfcake.message.Message;
import org.perfcake.message.correlator.Correlator;
import org.perfcake.message.generator.MessageGenerator;
import org.perfcake.message.receiver.Receiver;
import org.perfcake.message.sender.MessageSender;
import org.perfcake.message.sequence.Sequence;
import org.perfcake.message.sequence.SequenceManager;
import org.perfcake.reporting.ReportManager;
import org.perfcake.reporting.destination.Destination;
import org.perfcake.reporting.reporter.Reporter;
import org.perfcake.scenario.Scenario;
import org.perfcake.validation.MessageValidator;
import org.perfcake.validation.ValidationManager;

import java.lang.reflect.Modifier;
import java.time.Period;

/**
 * Represents type of the perfcake component.
 *
 * @author jknetl
 *
 */
public enum ComponentKind {

	SCENARIO(Scenario.class, ScenarioModel.class),
	RUN_INFO(RunInfo.class, RunModel.class),
	GENERATOR(MessageGenerator.class, GeneratorModel.class),
	SENDER(MessageSender.class, SenderModel.class),
	RECEIVER(Receiver.class, ReceiverModel.class),
	CORRELATOR(Correlator.class, CorrelatorModel.class),
	SEQUENCE(Sequence.class, SequenceModel.class),
	REPORTER(Reporter.class, ReporterModel.class),
	DESTINATION(Destination.class, DestinationModel.class),
	VALIDATOR(MessageValidator.class, ValidatorModel.class),
	MESSAGE(Message.class, MessageModel.class),
	PERIOD(Period.class, PeriodModel.class),
	REPORTING(ReportManager.class, ReportingModel.class),
	SEQUENCES(SequenceManager.class, SequencesModel.class),
	VALIDATOIN(ValidationManager.class, ValidationModel.class);


	/* Model classes which are not bindinded to the perfcake component since it is represented as
	 * some simple java type:
	 *
	 * HeaderModel (just java.util.Property)
	 * PropertyModel (just java.util.Property)
	 * PropertiesModel (just java.util.Properties)
	 * MessagesModel (just a list<Message>)
	 *
	 */

	/**
	 * Class of the component.
	 */
	private Class<?> componentClazz;

	/**
	 * Class which is used as a model.
	 */
	private Class<?> modelClazz;

	/**
	 * Denotes whether the component is abstract and thus never use directly.
	 */
	private boolean isAbstract;

	ComponentKind(Class<?> componentClazz, Class<?> modelClazz) {
		this.componentClazz = componentClazz;
		this.modelClazz = modelClazz;

		isAbstract = componentClazz.isInterface() || isAbstract(componentClazz);
	}

	/**
	 *
	 * @return Class which represents the component in the PerfCake
	 */
	public Class<?> getComponentClazz() {
		return componentClazz;
	}

	/**
	 *
	 * @return Class which is used as model of the component in pc4ide
	 */
	public Class<?> getModelClazz() {
		return modelClazz;
	}

	/**
	 *
	 * @return true if the component cannot be used in the scenario directly but some implementation must be provided. False otherwise.
	 */
	public boolean isAbstract() {
		return isAbstract;
	}

	/**
	 * Compoutes PerfCake component class from pc4ide model class.
	 *
	 * @param modelClazz
	 * @return PerfCake component class or null if no component is binded to the modelClazz
	 */
	public static Class<?> getComonentClazz(Class<?> modelClazz) {
		Class<?> componentClazz = null;
		for (final ComponentKind kind : values()) {
			if (kind.getModelClazz().equals(modelClazz)) {
				componentClazz = kind.getComponentClazz();
				break;
			}
		}

		return componentClazz;
	}

	private boolean isAbstract(Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}

}
