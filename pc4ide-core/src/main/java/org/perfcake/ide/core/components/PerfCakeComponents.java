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

import org.perfcake.message.correlator.Correlator;
import org.perfcake.message.generator.MessageGenerator;
import org.perfcake.message.receiver.Receiver;
import org.perfcake.message.sender.MessageSender;
import org.perfcake.message.sequence.Sequence;
import org.perfcake.reporting.destination.Destination;
import org.perfcake.reporting.reporter.Reporter;
import org.perfcake.validation.MessageValidator;

/**
 * Represents a PerfCake inspector.
 *
 * @author jknetl
 */
public enum PerfCakeComponents {

    GENERATOR(MessageGenerator.class),
    SENDER(MessageSender.class),
    RECEIVER(Receiver.class),
    CORRELATOR(Correlator.class),
    SEQUENCE(Sequence.class),
    REPORTER(Reporter.class),
    DESTINATION(Destination.class),
    VALIDATOR(MessageValidator.class);

    /**
     * Class of the inspector interface.
     */
    private Class<?> api;

    PerfCakeComponents(Class<?> api) {
        this.api = api;

    }

    /**
     * Gets an interface which represent supertype of inspector..
     *
     * @return Class which represents the inspector type in the PerfCake
     */
    public Class<?> getApi() {
        return api;
    }
}
