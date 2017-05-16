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
import org.perfcake.ide.core.model.properties.DataType;
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
            new ImplementationField("monitoringPeriod", "1000", false, DataType.INTEGER),
            new ImplementationField("shutdownPeriod", "5000", false, DataType.INTEGER),
            new ImplementationField("senderTaskQueueSize", "1000", false, DataType.INTEGER)

    };

    private ImplementationField[] expectedSenderFields = new ImplementationField[] {
            new ImplementationField("driverClass", "", true, DataType.STRING),
            new ImplementationField("username", "", false, DataType.STRING),
            new ImplementationField("password", "", false, DataType.STRING),
            new ImplementationField("jdbcUrl", "", false, DataType.STRING),
            new ImplementationField("keepConnection", "true", false, DataType.BOOLEAN) //this is inherited from abstractSender
    };

    private ImplementationField[] expectedSequenceFields = new ImplementationField[] {
            new ImplementationField("min", "0", false, DataType.INTEGER),
            new ImplementationField("max", "100", false, DataType.INTEGER)
    };

    private ImplementationField[] expectedReporterFields = new ImplementationField[] {
            new ImplementationField("attribute", null, true, DataType.STRING),
            new ImplementationField("prefix", "class_", false, DataType.STRING)
    };

    private ImplementationField[] expectedValidatorFields = new ImplementationField[] {
            new ImplementationField("dictionaryDirectory", null, true, DataType.STRING),
            new ImplementationField("record", "false", false, DataType.BOOLEAN),
            new ImplementationField("dictionaryIndex", "index", false, DataType.STRING)
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
