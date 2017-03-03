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

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * Contains test for {@link ComponentLoaderImpl}.
 *
 * @author Jakub Knetl
 */
public class ComponentLoaderTest {

    private ComponentLoader loader;

    @Before
    public void setUp() {
        loader = new ComponentLoaderImpl();
    }

    @Test
    public void testLoadingDefaultComponent() {

        Class<?> generator = loader.loadComponent("DefaultMessageGenerator", PerfCakeComponent.GENERATOR);
        assertThat(generator, notNullValue());

        String fqdn = PerfCakeComponent.GENERATOR.getDefaultPackage() + ".DefaultMessageGenerator";
        Class<?> fqdnGenerator = loader.loadComponent(fqdn, PerfCakeComponent.GENERATOR);
        assertThat(fqdnGenerator, notNullValue());
    }

    @Test
    public void testLoadingThirdPartyComponent() {

        Class<?> bobSender = loader.loadComponent("org.bob.perfcake.BobSender", PerfCakeComponent.SENDER);
        assertThat(bobSender, notNullValue());
    }

    @Test
    public void testLoadingWrongComponent() {

        Class<?> unknown = loader.loadComponent("XYZ", PerfCakeComponent.GENERATOR);
        assertThat(unknown, nullValue());

        // should return null since we ask for SENDER type
        Class<?> generator = loader.loadComponent("DefaultMessageGenerator", PerfCakeComponent.SENDER);
        assertThat(generator, nullValue());

        // should return null since we ask for SENDER type
        String fqdn = PerfCakeComponent.GENERATOR.getDefaultPackage() + ".DefaultMessageGenerator";
        Class<?> generator2 = loader.loadComponent(fqdn, PerfCakeComponent.SENDER);
        assertThat(generator2, nullValue());
    }
}
