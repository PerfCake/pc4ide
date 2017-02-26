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

import org.perfcake.message.Message;
import org.perfcake.message.correlator.Correlator;
import org.perfcake.message.generator.MessageGenerator;
import org.perfcake.message.receiver.Receiver;
import org.perfcake.message.sender.MessageSender;
import org.perfcake.message.sequence.Sequence;
import org.perfcake.reporting.destination.Destination;
import org.perfcake.reporting.reporter.Reporter;
import org.perfcake.scenario.Scenario;
import org.perfcake.scenario.ScenarioFactory;
import org.perfcake.validation.MessageValidator;

/**
 * Represents a PerfCake component. Only the components which are has own model class in pc4ide are
 * listed in this enum. Other components are considered too simple, so they are represented just as
 * a property of some other component.
 *
 * @author jknetl
 */
public enum PerfCakeComponent {

    GENERATOR(MessageGenerator.class, ScenarioFactory.DEFAULT_GENERATOR_PACKAGE),
    SENDER(MessageSender.class, ScenarioFactory.DEFAULT_SENDER_PACKAGE),
    RECEIVER(Receiver.class, ScenarioFactory.DEFAULT_RECEIVER_PACKAGE),
    CORRELATOR(Correlator.class, ScenarioFactory.DEFAULT_CORRELATOR_PACKAGE),
    SEQUENCE(Sequence.class, ScenarioFactory.DEFAULT_SEQUENCE_PACKAGE),
    REPORTER(Reporter.class, ScenarioFactory.DEFAULT_REPORTER_PACKAGE),
    DESTINATION(Destination.class, ScenarioFactory.DEFAULT_DESTINATION_PACKAGE),
    VALIDATOR(MessageValidator.class, ScenarioFactory.DEFAULT_VALIDATION_PACKAGE),
    MESSAGE(Message.class, null), //message has no implementation classes
    SCENARIO(Scenario.class, null); //scenario has no implementation classes

    /**
     * Class of the inspector interface.
     */
    private Class<?> api;

    /**
     * Default package for implementation classes.
     */
    private String defaultPackage;

    PerfCakeComponent(Class<?> api, String defaultPackage) {
        this.api = api;
        this.defaultPackage = defaultPackage;
    }

    /**
     * Gets an interface which represent supertype of inspector..
     *
     * @return Class which represents the inspector type in the PerfCake
     */
    public Class<?> getApi() {
        return api;
    }

    /**
     * Gets default package for implementation classes.
     *
     * @return the default package for implementation classes of this component.
     */
    public String getDefaultPackage() {
        return defaultPackage;
    }

    /**
     * Detects PerfCake component by implementation class.
     *
     * @param clazz implementation of some component.
     * @return PerfCake component which is implemented by the clazz or null if clazz is not a perfcake component implementation.
     */
    public static PerfCakeComponent detectComponentType(Class<?> clazz) {
        PerfCakeComponent result = null;
        for (PerfCakeComponent c : values()) {
            if (c.getApi().isAssignableFrom(clazz)) {
                result = c;
            }
        }

        return result;
    }
}
