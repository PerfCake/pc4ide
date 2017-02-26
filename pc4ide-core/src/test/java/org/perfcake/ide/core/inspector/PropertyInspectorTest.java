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

package org.perfcake.ide.core.inspector;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.util.List;
import org.junit.Test;
import org.perfcake.message.generator.DefaultMessageGenerator;
import org.perfcake.message.sender.JdbcSender;
import org.perfcake.message.sequence.RandomSequence;
import org.perfcake.reporting.reporter.ClassifyingReporter;
import org.perfcake.validation.DictionaryValidator;

/**
 * Tests for {@link PropertyUtilsInspector}.
 *
 * @author Jakub Knetl
 */
public class PropertyInspectorTest {

    private PropertyInspector inspector = new PropertyUtilsInspector();

    private ImplementationField[] expectedGeneratorFields = new ImplementationField[] {
            new ImplementationField("monitoringPeriod", "1000", false),
            new ImplementationField("shutdownPeriod", "5000", false),
            new ImplementationField("senderTaskQueueSize", "1000", false)

    };

    private ImplementationField[] expectedSenderFields = new ImplementationField[] {
            new ImplementationField("driverClass", "", true),
            new ImplementationField("username", "", false),
            new ImplementationField("password", "", false),
            new ImplementationField("jdbcUrl", "", false),
            new ImplementationField("keepConnection", "true", false) //this is inherited from abstractSender
    };

    private ImplementationField[] expectedSequenceFields = new ImplementationField[] {
            new ImplementationField("min", "0", false),
            new ImplementationField("max", "100", false)
    };

    private ImplementationField[] expectedReporterFields = new ImplementationField[] {
            new ImplementationField("attribute", null, true),
            new ImplementationField("prefix", "class_", false)
    };

    private ImplementationField[] expectedValidatorFields = new ImplementationField[] {
            new ImplementationField("dictionaryDirectory", null, true),
            new ImplementationField("record", "false", false),
            new ImplementationField("dictionaryIndex", "index", false)
    };

    @Test
    public void testInspectingProperties() {
        List<ImplementationField> generatorFields = inspector.getProperties(DefaultMessageGenerator.class);
        assertThat(generatorFields, containsInAnyOrder(expectedGeneratorFields));

        List<ImplementationField> senderFields = inspector.getProperties(JdbcSender.class);
        assertThat(senderFields, containsInAnyOrder(expectedSenderFields));

        List<ImplementationField> sequenceFields = inspector.getProperties(RandomSequence.class);
        assertThat(sequenceFields, containsInAnyOrder(expectedSequenceFields));

        List<ImplementationField> reporterFields = inspector.getProperties(ClassifyingReporter.class);
        assertThat(reporterFields, containsInAnyOrder(expectedReporterFields));

        List<ImplementationField> validatorFields = inspector.getProperties(DictionaryValidator.class);
        assertThat(validatorFields, containsInAnyOrder(expectedValidatorFields));
    }
}
