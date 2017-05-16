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

package org.perfcake.ide.intellij.exec;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.perfcake.ide.intellij.PerfCakeIcons;

/**
 * PerfCake run configuration.
 *
 * @author Jakub Knetl
 */
public class PerfCakeRunConfigurationType implements ConfigurationType {

    public static final String PERFCAKE = "PerfCake";
    public static final String PERF_CAKE_RUN_CONFIG_ID = "PerfCake-run-config";

    @Override
    public String getDisplayName() {
        return PERFCAKE;
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "PerfCake run configuration is able to run PerfCake scenario.";
    }

    @Override
    public Icon getIcon() {
        return PerfCakeIcons.SMALL_PERFCAKE_ICON;
    }

    @NotNull
    @Override
    public String getId() {
        return PERF_CAKE_RUN_CONFIG_ID;
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[] {new PerfCakeConfigurationFactory(this)};
    }
}
