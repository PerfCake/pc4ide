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

package org.perfcake.ide.core.docs;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import org.perfcake.ide.core.utils.TestUtils;
import org.perfcake.message.generator.ConstantSpeedMessageGenerator;
import org.perfcake.message.generator.DefaultMessageGenerator;
import org.perfcake.message.receiver.HttpReceiver;
import org.perfcake.message.sender.CoapSender;
import org.perfcake.message.sender.JmsSender;
import org.perfcake.message.sequence.NumberSequence;
import org.perfcake.reporting.destination.CsvDestination;
import org.perfcake.reporting.reporter.MemoryUsageReporter;
import org.perfcake.validation.RegExpValidator;

/**
 * Contains tests for DocsServiceImpl.
 *
 * @author Jakub Knetl
 */
public class DocsServiceTest {

    public static final String PERFCAKE_COMMENT_PROPERTIES = "src/main/resources/perfcake-comment.properties";
    private DocsService docsService;

    /**
     * Loads properties file.
     *
     * @throws IOException if javadoc cannot be loaded.
     */
    @Before
    public void setUp() throws IOException {
        Properties javadoc = TestUtils.loadJavadocProperties();
        docsService = new DocsServiceImpl(javadoc);
    }

    @Test
    public void testGettingTypeDocs() {
        String docs = docsService.getDocs(JmsSender.class);
        assertThat(docs, not(isEmptyOrNullString()));

        docs = docsService.getDocs(CsvDestination.class);
        assertThat(docs, not(isEmptyOrNullString()));

        docs = docsService.getDocs(RegExpValidator.class);
        assertThat(docs, not(isEmptyOrNullString()));
    }

    @Test
    public void testGettingFieldDocs() {
        String docs;

        // test general docs (not implementation specific)
        docs = docsService.getFieldDocs(CoapSender.class, "target");
        assertThat(docs, not(isEmptyOrNullString()));
        docs = docsService.getFieldDocs(DefaultMessageGenerator.class, "threads");
        assertThat(docs, not(isEmptyOrNullString()));

        // test implementation specific
        docs = docsService.getFieldDocs(CoapSender.class, "method");
        assertThat(docs, not(isEmptyOrNullString()));
        docs = docsService.getFieldDocs(ConstantSpeedMessageGenerator.class, "speed");
        assertThat(docs, not(isEmptyOrNullString()));
        docs = docsService.getFieldDocs(HttpReceiver.class, "source");
        assertThat(docs, not(isEmptyOrNullString()));
        docs = docsService.getFieldDocs(NumberSequence.class, "end");
        assertThat(docs, not(isEmptyOrNullString()));
        docs = docsService.getFieldDocs(MemoryUsageReporter.class, "agentHostname");
        assertThat(docs, not(isEmptyOrNullString()));

    }

}
