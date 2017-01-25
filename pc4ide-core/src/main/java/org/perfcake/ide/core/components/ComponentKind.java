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

package org.perfcake.ide.core.components;

import java.lang.reflect.Modifier;
import java.time.Period;
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


/**
 * Represents type of the perfcake component.
 *
 * @author jknetl
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

    /*
     * Model classes which are not bindinded to the perfcake component since it is represented as
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

        isAbstract = componentClazz.isInterface() || isClassAbstract(componentClazz);
    }

    /**
     * Gets class representing component kind.
     * @return Class which represents the component in the PerfCake
     */
    public Class<?> getComponentClazz() {
        return componentClazz;
    }

    /**
     * Get class represinting model of the component.
     * @return Class which is used as model of the component in pc4ide
     */
    public Class<?> getModelClazz() {
        return modelClazz;
    }

    /**
     * Is component abstract?
     * @return true if the component cannot be used in the scenario directly but some implementation must be provided. False otherwise.
     */
    public boolean isAbstract() {
        return isAbstract;
    }

    /**
     * Computes PerfCake component class from pc4ide model class.
     *
     * @param modelClazz model class of the component
     * @return PerfCake component class or null if no component is binded to the modelClazz
     */
    public static Class<?> getComponentClazzByModel(Class<?> modelClazz) {
        Class<?> componentClazz = null;
        for (final ComponentKind kind : values()) {
            if (kind.getModelClazz().equals(modelClazz)) {
                componentClazz = kind.getComponentClazz();
                break;
            }
        }

        return componentClazz;
    }

    /**
     * Gets component tybe based on model class.
     *
     * @param modelClazz model class of the component.
     * @return Kind of the component or null if no such component kind was found.
     */
    public static ComponentKind getComponentKindByModelClazz(Class<?> modelClazz) {
        ComponentKind kind = null;

        for (final ComponentKind k : values()) {
            if (k.getModelClazz().equals(modelClazz)) {
                kind = k;
                break;
            }
        }

        return kind;

    }

    private boolean isClassAbstract(Class<?> clazz) {
        return Modifier.isAbstract(clazz.getModifiers());
    }

}
