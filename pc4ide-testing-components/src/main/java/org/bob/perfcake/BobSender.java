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

package org.bob.perfcake;

import java.io.Serializable;
import java.util.Properties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.perfcake.PerfCakeException;
import org.perfcake.message.Message;
import org.perfcake.message.sender.AbstractSender;
import org.perfcake.message.sender.DummySender;
import org.perfcake.reporting.MeasurementUnit;

/**
 * Specific (dummy) sender for Bob's scenario. Strucutre taken from {@link DummySender}.
 *
 * @author jknetl
 */
public class BobSender extends AbstractSender {

    private static final Logger log = LogManager.getLogger(DummySender.class);
    private boolean isDebugEnabled = false;

    /**
     * The delay duration to simulate a asynchronous waiting.
     */
    private long bobDelay = 0;

    @Override
    public void doInit(final Properties messageAttributes) throws PerfCakeException {
        final String currentTarget = safeGetTarget(messageAttributes);

        isDebugEnabled = log.isDebugEnabled();

        if (isDebugEnabled) {
            log.debug("Initializing... " + currentTarget);
        }
    }

    @Override
    public void doClose() throws PerfCakeException {
        if (isDebugEnabled) {
            log.debug("Closing Dummy sender.");
        }
    }

    @Override
    public void preSend(final Message message, final Properties messageAttributes) throws Exception {
        super.preSend(message, messageAttributes);
        if (isDebugEnabled) {
            log.debug("Sending to " + safeGetTarget(messageAttributes) + "...");
        }
    }

    @Override
    public Serializable doSend(final Message message, final MeasurementUnit measurementUnit) throws Exception {
        if (bobDelay > 0) {
            final long sleepStart = System.currentTimeMillis();
            try {
                Thread.sleep(bobDelay);
            } catch (final InterruptedException ie) { // Snooze
                final long snooze = bobDelay - (System.currentTimeMillis() - sleepStart);
                if (snooze > 0) {
                    Thread.sleep(snooze);
                }
            }
        }

        return (message == null) ? null : message.getPayload();
    }

    public long getBobDelay() {
        return bobDelay;
    }

    /**
     * Sets a delay.
     * @param bobDelay delay to be set.
     * @return this object.
     */
    public BobSender setBobDelay(long bobDelay) {
        this.bobDelay = bobDelay;
        return this;
    }
}
