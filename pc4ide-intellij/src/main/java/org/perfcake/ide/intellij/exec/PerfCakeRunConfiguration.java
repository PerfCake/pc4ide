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

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.LocatableConfigurationBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizerUtil;
import com.intellij.openapi.util.WriteExternalException;
import java.nio.file.Paths;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.perfcake.ide.core.exec.PerfCakeExecutor;
import org.perfcake.ide.core.exec.SystemProperty;
import org.perfcake.ide.core.utils.PathUtils;

/**
 * Represents PerfCake run configuration.
 *
 * @author Jakub Knetl
 */
public class PerfCakeRunConfiguration extends LocatableConfigurationBase {

    public static final String SCENARIO_DIR = "scenario.dir";
    public static final String SCENARIO_NAME = "scenario.name";
    public static final String JAVA_HOME = "java.home";
    public static final String PERFCAKE_HOME = "perfcake.home";
    public static final String MESSAGES_DIR = "messages.dir";
    public static final String PLUGIN_DIR = "plugin.dir";
    public static final String SYSTEM_PROPERTIES = "system-properties";
    public static final String DEBUG_EXECUTION = "debug.execution";
    public static final String DEBUG_AGENT_NAME = "debug.agent.name";
    private PerfCakeSettingsEditor perfCakeSettingsEditor;
    private PerfCakeExecutor perfCakeExecutor;

    protected PerfCakeRunConfiguration(@NotNull Project project, @NotNull ConfigurationFactory factory, String name) {
        super(project, factory, name);
        this.perfCakeExecutor = new PerfCakeExecutor();
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        perfCakeSettingsEditor = new PerfCakeSettingsEditor(getProject());
        return perfCakeSettingsEditor;
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException {
        return new PerfCakeCommandLineState(environment, perfCakeExecutor);
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        super.readExternal(element);

        String scenarioDir = JDOMExternalizerUtil.readField(element, SCENARIO_DIR, "");
        if (!scenarioDir.isEmpty()) {
            perfCakeExecutor.setScenarioDir(Paths.get(scenarioDir));
        }
        String scenarioName = JDOMExternalizerUtil.readField(element, SCENARIO_NAME, "");
        if (!scenarioName.isEmpty()) {
            perfCakeExecutor.setScenario(scenarioName);
        }

        String perfCakeHome = JDOMExternalizerUtil.readField(element, PERFCAKE_HOME, "");
        if (!perfCakeHome.isEmpty()) {

            perfCakeExecutor.setPerfCakeHome(Paths.get(perfCakeHome));
        }
        String javaHome = JDOMExternalizerUtil.readField(element, JAVA_HOME, "");
        if (!javaHome.isEmpty()) {
            perfCakeExecutor.setJavaHome(Paths.get(javaHome));
        }

        String messagesDir = JDOMExternalizerUtil.readField(element, MESSAGES_DIR, "");
        if (!messagesDir.isEmpty()) {
            perfCakeExecutor.setMessageDir(Paths.get(messagesDir));
        }

        String pluginDir = JDOMExternalizerUtil.readField(element, PLUGIN_DIR, "");
        if (!pluginDir.isEmpty()) {
            perfCakeExecutor.setPluginDir(Paths.get(pluginDir));
        }

        String debugMode = JDOMExternalizerUtil.readField(element, DEBUG_EXECUTION, "false");
        boolean isDebugMode = Boolean.valueOf(debugMode);
        perfCakeExecutor.setDebugMode(isDebugMode);

        String debugAgentName = JDOMExternalizerUtil.readField(element, DEBUG_AGENT_NAME, "");
        if (!debugAgentName.isEmpty()) {
            perfCakeExecutor.setDebugName(debugAgentName);
        }

        Element systemProperties = element.getChild(SYSTEM_PROPERTIES);
        if (systemProperties != null) {

            for (Element propertyElement : systemProperties.getChildren()) {
                perfCakeExecutor.getSystemProperties().add(
                        new SystemProperty(propertyElement.getAttributeValue("name"),
                                propertyElement.getAttributeValue("value")));
            }
        }


    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);
        JDOMExternalizerUtil.writeField(element, SCENARIO_DIR, PathUtils.pathToString(perfCakeExecutor.getScenarioDir()));
        JDOMExternalizerUtil.writeField(element, SCENARIO_NAME, safeString(perfCakeExecutor.getScenario()));
        JDOMExternalizerUtil.writeField(element, JAVA_HOME, PathUtils.pathToString(perfCakeExecutor.getJavaHome()));
        JDOMExternalizerUtil.writeField(element, PERFCAKE_HOME, PathUtils.pathToString(perfCakeExecutor.getPerfCakeHome()));
        JDOMExternalizerUtil.writeField(element, MESSAGES_DIR, PathUtils.pathToString(perfCakeExecutor.getMessageDir()));
        JDOMExternalizerUtil.writeField(element, PLUGIN_DIR, PathUtils.pathToString(perfCakeExecutor.getPluginDir()));

        Element systemPropertiesElement = new Element(SYSTEM_PROPERTIES);
        for (SystemProperty p : perfCakeExecutor.getSystemProperties()) {
            JDOMExternalizerUtil.writeField(systemPropertiesElement, p.getKey(), p.getValue());
        }
        element.addContent(systemPropertiesElement);

        JDOMExternalizerUtil.writeField(element, DEBUG_EXECUTION, String.valueOf(perfCakeExecutor.isDebugMode()));
        JDOMExternalizerUtil.writeField(element, DEBUG_AGENT_NAME, safeString(perfCakeExecutor.getDebugName()));
    }

    /**
     * @param string a string
     * @return string which is same as an argument, however if argument is null, then empty string is returned.
     */
    private static String safeString(String string) {
        return string == null ? "" : string;
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        super.checkConfiguration();
    }

    public PerfCakeExecutor getPerfCakeExecutor() {
        return perfCakeExecutor;
    }

    @Override
    public String suggestedName() {
        return perfCakeExecutor.getScenario();
    }
}
