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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;


/**
 * Tests for {@link ReflectionComponentCatalogue}.
 *
 * @author Jakub Knetl
 */
public class ReflectionCatalogueTest {

    public static final String ADDITIONAL_IMPLEMENTATIONS = "org.bob.perfcake";
    public static final String BOB_SENDER = "org.bob.perfcake.BobSender";
    private ComponentCatalogue catalogue;

    private String[] generators = new String[] {"DefaultMessageGenerator", "RampUpDownGenerator", "CustomProfileGenerator",
            "ConstantSpeedMessageGenerator"};
    private String[] receivers = new String[] {"HttpReceiver"};
    private String[] correlators = new String[] {"GenerateHeaderCorrelator"};
    private String[] sequences = new String[] {"PrimitiveNumberSequence", "NumberSequence", "RandomSequence", "RandomUuidSequence",
            "TimeStampSequence", "FileLinesSequence", "FilesContentSequence"};
    private String[] reporters = new String[] {"ClassifyingReporter", "GeolocationReporter", "IterationsPerSecondReporter",
            "MemoryUsageReporter", "ResponseTimeStatsReporter", "ThroughputStatsReporter", "WarmUpReporter", "RawReporter"};
    private String[] destination = new String[] {"ChartDestination", "ConsoleDestination", "CsvDestination", "ElasticsearchDestination",
            "InfluxDbDestination", "Log4jDestination"};
    private String[] validators = new String[] {"DictionaryValidator", "PrintingValidator", "RegExpValidator", "RulesValidator",
            "ScriptValidator"};

    @Test
    public void testContainsDefaultComponents() {
        catalogue = new ReflectionComponentCatalogue();

        // check that default components are present
        checkDefaultPerfcakeComponents();
    }

    @Test
    public void testAdditionalcomponents() {
        List<String> packages = new ArrayList(Arrays.asList(ComponentCatalogue.DEFAULT_PACKAGES));
        packages.add(ADDITIONAL_IMPLEMENTATIONS);

        catalogue = new ReflectionComponentCatalogue(ADDITIONAL_IMPLEMENTATIONS);

        // bob's sender is present
        assertThat(catalogue.list(PerfCakeComponent.SENDER), hasItem(BOB_SENDER));

        // default components are present
        checkDefaultPerfcakeComponents();
    }

    @Test
    public void testModifyingPackages() {
        catalogue = new ReflectionComponentCatalogue();
        assertThat(catalogue.list(PerfCakeComponent.SENDER), not(hasItem(BOB_SENDER)));

        catalogue.addPackage(ADDITIONAL_IMPLEMENTATIONS);
        assertThat(catalogue.list(PerfCakeComponent.SENDER), not(hasItem(BOB_SENDER))); // sender should not be there

        catalogue.update();
        assertThat(catalogue.list(PerfCakeComponent.SENDER), hasItem(BOB_SENDER)); // sender should be present after update

        catalogue.removePackage(ADDITIONAL_IMPLEMENTATIONS);
        assertThat(catalogue.list(PerfCakeComponent.SENDER), hasItem(BOB_SENDER)); // sender should still be present

        catalogue.update();
        assertThat(catalogue.list(PerfCakeComponent.SENDER), not(hasItem(BOB_SENDER))); //sender should be gone

    }

    private void checkDefaultPerfcakeComponents() {
        // assert that all named components were detected
        assertThat(catalogue.list(PerfCakeComponent.GENERATOR), hasItems(generators));
        assertThat(catalogue.list(PerfCakeComponent.RECEIVER), hasItems(receivers));
        assertThat(catalogue.list(PerfCakeComponent.SEQUENCE), hasItems(sequences));
        assertThat(catalogue.list(PerfCakeComponent.REPORTER), hasItems(reporters));
        assertThat(catalogue.list(PerfCakeComponent.DESTINATION), hasItems(destination));
        assertThat(catalogue.list(PerfCakeComponent.CORRELATOR), hasItems(correlators));
        assertThat(catalogue.list(PerfCakeComponent.VALIDATOR), hasItems(validators));

        // assert that there is at least 20 senders ( we don't want to list them by names)
        assertThat(catalogue.list(PerfCakeComponent.SENDER).size(), is(greaterThan(20)));

        // check that scenario and message collections are empty since they have no implementations
        assertThat(catalogue.list(PerfCakeComponent.SCENARIO), empty());
        assertThat(catalogue.list(PerfCakeComponent.MESSAGE), empty());
    }

}
