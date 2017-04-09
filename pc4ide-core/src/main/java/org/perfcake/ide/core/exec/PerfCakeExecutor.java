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

package org.perfcake.ide.core.exec;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PerfCake executor manages execution of PerfCake.
 *
 * @author Jakub Knetl
 */
public class PerfCakeExecutor {

    public static final String CLASSPATH_SEPARATOR = System.getProperty("path.separator");

    static final Logger logger = LoggerFactory.getLogger(PerfCakeExecutor.class);
    public static final int PORT_MAX = 65535;
    public static final String DEFAULT_DEBUG_NAME = "perfcake-1";

    private Path javaHome;
    private Path perfCakeHome;

    private Map<String, String> systemProperties;
    private List<String> javaOpts;
    private boolean debugMode;
    private String debugName;
    private String logLevel;
    private Path messageDir;
    private Path pluginDir;
    private Path propertiesFile;
    private Path replayFile;
    private String scenario;
    private Path scenarioDir;
    private boolean skipTimerBenchmark;
    private boolean inheritIo;
    private int jmxPort;


    /**
     * Creates new perfcake executor.
     *
     * @param perfCakeHome jar contianing perfcake with all dependencies
     * @param scenario     name of the scenario to be executed
     * @param scenarioDir  directory which contains a scenario
     */
    public PerfCakeExecutor(Path perfCakeHome, String scenario, Path scenarioDir) {
        this.perfCakeHome = perfCakeHome;
        this.scenario = scenario;
        this.scenarioDir = scenarioDir;

        this.systemProperties = new HashMap<>();
        this.javaOpts = new ArrayList<>();

        this.javaHome = Paths.get(System.getProperty("java.home"), "bin", "java");
    }

    /**
     * Executes perfcake scenario based on configuration of this executor.
     *
     * @return Process which represents execution
     * @throws IOException if an IO error occurs
     */
    public Process execute() throws IOException {

        initializeJavaOpts();

        List<String> command = new ArrayList<>();
        command.add(javaHome.resolve("bin").resolve("java").toString());
        command.add(constructExtDirsParam());

        // add java opts
        for (String opt : javaOpts) {
            command.add(opt);
        }
        command.add("-jar");
        String perfCakeJar = new SimpleInstallationValidator().findPerfCakeJar(perfCakeHome).toString();
        command.add(perfCakeJar);
        command.add("org.perfcake.ScenarioExecution");
        command.add("-s");
        command.add(scenario);
        command.add("-sd");
        command.add(scenarioDir.toString());


        if (debugMode) {
            command.add("-d");
        }

        if (debugName != null) {
            command.add("-dn");
            command.add(debugName);
        } else {
            debugName = DEFAULT_DEBUG_NAME;
        }

        if (logLevel != null) {
            command.add("-log");
            command.add(logLevel);
        }

        if (pluginDir != null) {
            command.add("-pd");
            command.add(pluginDir.toString());
        }

        if (propertiesFile != null) {
            command.add("-pf");
            command.add(propertiesFile.toString());
        }

        if (skipTimerBenchmark) {
            command.add("-skip");
        }

        if (messageDir != null) {
            command.add("-md");
            command.add(messageDir.toString());
        }

        for (Map.Entry<String, String> entry : systemProperties.entrySet()) {
            command.add(String.format("-D%s=%s", entry.getKey(), entry.getValue()));
        }

        logger.debug("Executing scenario using command: \"{}\"", String.join(" ", command));
        ProcessBuilder pb = new ProcessBuilder(command);

        if (inheritIo) {
            logger.debug("Inheritng IO from parent process.");
            pb.inheritIO();
        }

        return pb.start();
    }

    protected void initializeJavaOpts() {
        if (debugMode) {
            jmxPort = findOpenPort();
            javaOpts.add("-Dcom.sun.management.jmxremote.port=" + jmxPort);
            javaOpts.add("-Dcom.sun.management.jmxremote.authenticate=false");
            javaOpts.add("-Dcom.sun.management.jmxremote.ssl=false");
        }
    }

    private int findOpenPort() {
        boolean available = false;
        int port = 9999;
        while (!available && port < PORT_MAX) {
            try (ServerSocket socket = new ServerSocket(port)) {
                available = true;
                logger.debug("JMX port: {}", port);
            } catch (IOException e) {
                logger.trace("Port {} is in use. trying another one...", port);
                port++;
            }
        }

        if (!available) {
            logger.warn("Couldn't find available port for JMX agent.");
        }

        return port;
    }

    protected String constructExtDirsParam() {

        StringBuilder extDirs = new StringBuilder();
        extDirs.append("-Djava.ext.dirs=")
                .append(javaHome.resolve("lib").resolve("ext")) //JAVA_HOME/lib/ext
                .append(CLASSPATH_SEPARATOR)
                .append(javaHome.resolve("jre").resolve("lib").resolve("ext")) //JAVA_HOME/jre/lib/ext
                .append(CLASSPATH_SEPARATOR)
                .append(perfCakeHome.resolve("lib").resolve("ext")) // PERFCAKE_HOME/lib/ext
                .append(CLASSPATH_SEPARATOR)
                .append(javaHome.resolve("lib")); //JAVA_HOME/lib

        return extDirs.toString();
    }

    public Path getJavaHome() {
        return javaHome;
    }

    public PerfCakeExecutor setJavaHome(Path javaHome) {
        this.javaHome = javaHome;
        return this;
    }

    public Path getPerfCakeHome() {
        return perfCakeHome;
    }

    public PerfCakeExecutor setPerfCakeHome(Path perfCakeHome) {
        this.perfCakeHome = perfCakeHome;
        return this;
    }

    public Map<String, String> getSystemProperties() {
        return systemProperties;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public PerfCakeExecutor setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
        return this;
    }

    public String getDebugName() {
        return debugName;
    }

    public PerfCakeExecutor setDebugName(String debugName) {
        this.debugName = debugName;
        return this;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public PerfCakeExecutor setLogLevel(String logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public Path getMessageDir() {
        return messageDir;
    }

    public PerfCakeExecutor setMessageDir(Path messageDir) {
        this.messageDir = messageDir;
        return this;
    }

    public Path getPluginDir() {
        return pluginDir;
    }

    public PerfCakeExecutor setPluginDir(Path pluginDir) {
        this.pluginDir = pluginDir;
        return this;
    }

    public Path getPropertiesFile() {
        return propertiesFile;
    }

    public PerfCakeExecutor setPropertiesFile(Path propertiesFile) {
        this.propertiesFile = propertiesFile;
        return this;
    }

    public Path getReplayFile() {
        return replayFile;
    }

    public PerfCakeExecutor setReplayFile(Path replayFile) {
        this.replayFile = replayFile;
        return this;
    }

    public String getScenario() {
        return scenario;
    }

    public PerfCakeExecutor setScenario(String scenario) {
        this.scenario = scenario;
        return this;
    }

    public Path getScenarioDir() {
        return scenarioDir;
    }

    public PerfCakeExecutor setScenarioDir(Path scenarioDir) {
        this.scenarioDir = scenarioDir;
        return this;
    }

    public boolean isSkipTimerBenchmark() {
        return skipTimerBenchmark;
    }

    public PerfCakeExecutor setSkipTimerBenchmark(boolean skipTimerBenchmark) {
        this.skipTimerBenchmark = skipTimerBenchmark;
        return this;
    }

    public boolean isInheritIo() {
        return inheritIo;
    }

    public PerfCakeExecutor setInheritIo(boolean inheritIo) {
        this.inheritIo = inheritIo;
        return this;
    }

    public int getJmxPort() {
        return jmxPort;
    }
}
