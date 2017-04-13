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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import org.perfcake.ide.core.exception.JmxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExecutionManagerImpl manages execution and debugging of the scenario.
 *
 * @author Jakub Knetl
 */
public class ExecutionManagerImpl implements ExecutionManager {

    static final Logger logger = LoggerFactory.getLogger(ExecutionManagerImpl.class);
    public static final String DEFAULT_PERFCAKE_DOMAIN = "org.perfcake";
    public static final String MONITOR_VALUE_ATTRIBUTE = "value";

    private Process process;
    boolean debug;

    private Map<ExecutionListener, MBeanSubscription> listeners;

    // polling features
    private ScheduledExecutorService executorService;
    private ScheduledFuture<?> pollingFuture;

    // Debug fields
    private String host;
    private int port;
    private String agentName;
    private String perfcakeDomain;

    private JMXConnector jmxConnector;
    private MBeanServerConnection mBeanServerConnection;


    /**
     * Creates new debug manager.
     *
     * @param process process representing the running scenario
     */
    public ExecutionManagerImpl(Process process) {
        if (process == null) {
            throw new IllegalArgumentException("Process cannot be null");
        }
        this.debug = false;
        this.listeners = new HashMap<>();
        this.process = process;
    }

    /**
     * Creates new debug manager.
     *
     * @param process   process representing the running scenario
     * @param host      hostname of the target VM with perfcake debug agent.
     * @param port      JMX port debug agent runs on.
     * @param agentName Name of the perfcake debug agent
     */
    public ExecutionManagerImpl(Process process, String host, int port, String agentName) {
        this(process);
        if (host == null) {
            throw new IllegalArgumentException("Host cannot be null.");
        }
        if (agentName == null) {
            throw new IllegalArgumentException("Agent name cannot be null.");
        }
        this.host = host;
        this.port = port;
        this.debug = true;
        this.perfcakeDomain = DEFAULT_PERFCAKE_DOMAIN;
        this.agentName = agentName;
    }

    /**
     * Adds execution listener.
     *
     * @param executionListener execution listener to be added
     * @param mBeanSubscription MBeanSubscription representing mbeans, whose values will be watched during debug execution,
     *                          if MBeanSubscription is empty or null, then only general events are delivered to the listener.
     */
    @Override
    public void addListener(ExecutionListener executionListener, MBeanSubscription mBeanSubscription) {
        if (executionListener == null) {
            throw new IllegalArgumentException("executionListener cannot be null.");
        }

        if (mBeanSubscription == null) {
            mBeanSubscription = mBeanSubscription.createEmptySubscription();
        }

        listeners.put(executionListener, mBeanSubscription);

    }

    /**
     * Adds execution listener.
     *
     * @param executionListener execution listener to be added
     */
    @Override
    public void addListener(ExecutionListener executionListener) {
        this.addListener(executionListener, null);
    }

    /**
     * Removes execution listener.
     *
     * @param executionListener execution listener to be removed.
     */
    @Override
    public void removeListener(ExecutionListener executionListener) {
        listeners.remove(executionListener);
    }


    /**
     * Returns Debug MBeans subscriptions for given listener.
     *
     * @param executionListener listener.
     * @return MBeanSubscription or null if execution listener does not listen to this debug manager.
     */
    @Override
    public MBeanSubscription getMbeanSubscription(ExecutionListener executionListener) {
        return listeners.get(executionListener);
    }


    /**
     * Starts to watch regularly to update status of the execution.
     *
     * @throws IllegalStateException If this DebugManger is not connected to jmx server.
     */
    public void startWatching() {
        if (executorService != null) {
            throw new IllegalStateException("Another polling is in progress.");
        }

        // Notify listeners about start of the process
        for (ExecutionListener listener : listeners.keySet()) {
            listener.handleEvent(new ExecutionEvent(ExecutionEvent.Type.STARTED, "debug-started", null));
        }

        if (isDebug()) {
            try {
                waitForJmxAgentConnection(5, TimeUnit.SECONDS, 1);
            } catch (JmxException e) {
                // do nothing
            }
        }

        // schedule regular polling for resources
        executorService = Executors.newSingleThreadScheduledExecutor();
        pollingFuture = executorService.scheduleAtFixedRate(() -> updateListeners(), 500, 500, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops watching of the execution.
     */
    public void stopWatching() {
        if (isDebug()) {
            disconnect();
        }
        if (pollingFuture != null) {
            for (ExecutionListener listener : listeners.keySet()) {
                listener.handleEvent(new ExecutionEvent(ExecutionEvent.Type.STOPED, "debug-stopped", null));
            }
            pollingFuture.cancel(true);
            pollingFuture = null;
            executorService.shutdown();
        }
    }

    @Override
    public Process getProcess() {
        return process;
    }

    @Override
    public boolean isRunning() {
        return process.isAlive();
    }

    @Override
    public boolean isDebug() {
        return debug;
    }

    @Override
    public String createCounterMBeanQuery(String... values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("values must have at least one element.");
        }
        StringBuilder builder = new StringBuilder();
        builder.append(perfcakeDomain);
        builder.append(":");

        builder.append("class=").append(agentName);

        for (int i = 0; i < values.length; i++) {
            String hint = values[i];
            builder.append(",");
            if (i == 0) {
                builder.append("name=").append(hint);
            } else {
                builder.append("category").append(i).append("=").append(hint);
            }
        }
        builder.append(",type=COUNTER");
        return builder.toString();
    }

    /**
     * Updates all execution listeners with a current value.
     */
    private void updateListeners() {

        if (!isRunning()) {
            stopWatching();
            return;
        }

        if (isDebug()) {
            updateDebugListeners();
        }
    }

    private void updateDebugListeners() {
        if (!isJmxAgentConnected()) {
            logger.debug("Jmx not connected. Skipping update of debug listeners.");
            return;
        }

        for (Map.Entry<ExecutionListener, MBeanSubscription> entry : listeners.entrySet()) {

            for (String objectName : entry.getValue().getObjectNames()) {

                try {
                    ObjectName mbeanQuery = new ObjectName(objectName);
                    Set<ObjectName> mbeansFound = mBeanServerConnection.queryNames(mbeanQuery, null);

                    if (mbeansFound.isEmpty()) {
                        logger.debug("Ignoring object name: {}. No such Mbean found.", mbeanQuery);
                    }

                    for (ObjectName mbean : mbeansFound) {
                        MBeanInfo info = mBeanServerConnection.getMBeanInfo(mbean);
                        if (hasAttribute(info, MONITOR_VALUE_ATTRIBUTE)) {
                            Object value = mBeanServerConnection.getAttribute(mbean, MONITOR_VALUE_ATTRIBUTE);
                            entry.getKey().handleEvent(new JmxDebugMonitorEvent(MONITOR_VALUE_ATTRIBUTE, value, mbean));
                        }
                    }

                } catch (IOException e) {
                    logger.warn("Cannot query PerfCake MBeans.", e);
                } catch (ReflectionException | IntrospectionException | InstanceNotFoundException e) {
                    logger.info("Cannot get mbean info.", e);
                } catch (MBeanException | AttributeNotFoundException e) {
                    logger.info("Cannot get mbean attribute.", e);
                } catch (MalformedObjectNameException e) {
                    logger.warn("Cannot create valid ObjectName from: " + objectName, e);
                }
            }
        }
    }

    /**
     * Disconnects this ExecutionManagerImpl from JMX agent.
     */
    private void disconnect() {
        mBeanServerConnection = null;
        if (jmxConnector != null) {
            try {
                jmxConnector.close();
            } catch (IOException e) {
                logger.warn("Problem with disconnecting from JMX agent");
            }
            jmxConnector = null;
        }
    }

    /**
     * Determines whether debug manager is connected to JMX agent.
     *
     * @return true if debug manager is connected to JMX agent.
     */
    private boolean isJmxAgentConnected() {
        boolean connected = jmxConnector != null && mBeanServerConnection != null;
        if (connected) {
            try {
                jmxConnector.getConnectionId();
            } catch (IOException e) {
                connected = false;
            }
        }
        return connected;
    }

    /**
     * Return true if mbean has an attribute.
     *
     * @param info          mbean info
     * @param attributeName attribute name
     * @return true if mbean has an attribute
     */
    private boolean hasAttribute(MBeanInfo info, String attributeName) {

        MBeanAttributeInfo[] attributeInfos = info.getAttributes();
        if (attributeInfos == null) {
            return false;
        }

        boolean attributeFound = false;
        for (MBeanAttributeInfo attribute : attributeInfos) {
            if (attribute.getName().equals(attributeName)) {
                attributeFound = true;
                break;
            }
        }

        return attributeFound;
    }

    /**
     * Attaches manager to JMX agent.
     *
     * @throws JmxException When cannot connectJmxAgent to JMX.
     */
    private void connectJmxAgent() throws JmxException {
        waitForJmxAgentConnection(1, TimeUnit.SECONDS, -1);
    }

    /**
     * Tries to connectJmxAgent repeatedly. If connection attempt fails, then there is timeout based on arguments and another
     * attempt is made. This is repeated until connection is successfully established or maximum number of attempts exceeded.
     *
     * @param attempts number of attempts
     * @param unit     unit for timeout
     * @param timeout  length of timeout
     * @throws JmxException when connection couldn't be established in any attempt.
     */
    public void waitForJmxAgentConnection(int attempts, TimeUnit unit, long timeout) throws JmxException {
        if (attempts <= 0) {
            throw new IllegalArgumentException("Attempts must be positive.");
        }

        boolean connected = false;
        int attempt = 0;
        while (!connected && attempt < attempts) {
            attempt++;
            try {
                String address = String.format("service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi", host, port);
                logger.debug("Connecting to perfcake debug agent. Url: {}", address);
                JMXServiceURL jmxUrl = new JMXServiceURL(address);
                jmxConnector = JMXConnectorFactory.connect(jmxUrl);
                mBeanServerConnection = jmxConnector.getMBeanServerConnection();
                connected = true;
            } catch (IOException e) {
                if (attempt < attempts) {
                    try {
                        unit.sleep(timeout);
                    } catch (InterruptedException e1) {
                        //do nothing
                    }
                } else {
                    throw new JmxException("Cannot connect to jmx agent.", e);
                }
            }
        }

    }
}
