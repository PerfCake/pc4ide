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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
 * DebugManager obtains data from perfcake debug agent.
 *
 * @author Jakub Knetl
 */
public class DebugManager {

    static final Logger logger = LoggerFactory.getLogger(DebugManager.class);
    public static final String DEFAULT_PERFCAKE_DOMAIN = "org.perfcake";
    public static final String MONITOR_VALUE_ATTRIBUTE = "value";

    private String host;
    private int port;

    // name of the perfcake debug agent
    private String agentName;
    private String perfcakeDomain;

    private Set<ExecutionListener> listeners;
    private JMXConnector jmxConnector;
    private MBeanServerConnection mBeanServerConnection;
    private ScheduledExecutorService executorService;

    /**
     * Creates new debug manager.
     *
     * @param host      hostname of the target VM with perfcake debug agent.
     * @param port      JMX port debug agent runs on.
     * @param agentName Name of the perfcake debug agent
     */
    public DebugManager(String host, int port, String agentName) {
        if (host == null) {
            throw new IllegalArgumentException("Host cannot be null.");
        }
        if (agentName == null) {
            throw new IllegalArgumentException("Agent name cannot be null.");
        }
        this.host = host;
        this.port = port;
        this.agentName = agentName;
        this.jmxConnector = null;
        this.perfcakeDomain = DEFAULT_PERFCAKE_DOMAIN;
        this.listeners = new HashSet<>();
    }

    /**
     * Attaches manager to JMX agent.
     *
     * @throws JmxException When cannot connect to JMX.
     */
    public void connect() throws JmxException {
        try {
            String address = String.format("service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi", host, port);
            logger.debug("Connecting to perfcake debug agent. Url: {}", address);
            JMXServiceURL jmxUrl = new JMXServiceURL(address);
            jmxConnector = JMXConnectorFactory.connect(jmxUrl);
            mBeanServerConnection = jmxConnector.getMBeanServerConnection();
        } catch (IOException e) {
            throw new JmxException("Cannot connect to jmx agent.", e);
        }
    }

    /**
     * Adds execution listener.
     *
     * @param executionListener execution listener to be added
     */
    void addExecutionListener(ExecutionListener executionListener) {
        if (executionListener == null) {
            throw new IllegalArgumentException("executionListener cannot be null.");
        }

        List<String> listenerHints = executionListener.getObjectNameHints();

        listeners.add(executionListener);

    }


    /**
     * Starts to poll regularly to update current value of subsribed listeners.
     *
     * @throws IllegalStateException If this DebugManger is not connected to jmx server.
     */
    public void startPolling() {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected");
        }
        if (executorService != null) {
            throw new IllegalStateException("Another polling is in progress.");
        }

        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                updateListeners();
            }
        }, 500, 500, TimeUnit.MILLISECONDS);
    }

    /**
     * Updates all execution listeners with a current value.
     */
    void updateListeners() {

        if (!isConnected()) {
            throw new IllegalStateException("Debug manager is not connected.");
        }

        for (ExecutionListener listener : listeners) {
            List<String> listenerHints = listener.getObjectNameHints();
            if (listenerHints == null || listenerHints.isEmpty()) {
                logger.debug("Ignoring execution listener with empty hints: {}.", listener.getClass().getSimpleName());
                return;
            }

            ObjectName mbeanQuery = createMBeanQuery(listenerHints);

            if (mbeanQuery != null) {
                try {
                    Set<ObjectName> mbeansFound = mBeanServerConnection.queryNames(mbeanQuery, null);

                    for (ObjectName mbean : mbeansFound) {
                        MBeanInfo info = mBeanServerConnection.getMBeanInfo(mbean);
                        if (hasAttribute(info, MONITOR_VALUE_ATTRIBUTE)) {
                            Object value = mBeanServerConnection.getAttribute(mbean, MONITOR_VALUE_ATTRIBUTE);
                            listener.handleEvent(new JmxDebugMonitorEvent(MONITOR_VALUE_ATTRIBUTE, value, mbean));
                        }
                    }

                } catch (IOException e) {
                    logger.warn("Cannot query PerfCake MBeans.", e);
                } catch (ReflectionException | IntrospectionException | InstanceNotFoundException e) {
                    logger.info("Cannot get mbean info.", e);
                } catch (MBeanException | AttributeNotFoundException e) {
                    logger.info("Cannot get mbean attribute.", e);
                }
            }
        }
    }

    /**
     * Disconnects this DebugManager from JMX agent.
     */
    public void disconnect() {
        executorService.shutdownNow();
        executorService = null;

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
     * Removes execution listener.
     *
     * @param executionListener execution listener to be removed.
     * @return true if the listener was removed
     */
    boolean removeExecutionListener(ExecutionListener executionListener) {
        return listeners.remove(executionListener);
    }

    public boolean isConnected() {
        return jmxConnector != null && mBeanServerConnection != null;
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

    private ObjectName createMBeanQuery(List<String> hints) {
        StringBuilder builder = new StringBuilder();
        builder.append(perfcakeDomain);

        builder.append("class=").append(agentName);

        if (hints != null && !hints.isEmpty()) {
            builder.append(":");
            for (int i = 0; i < hints.size(); i++) {
                String hint = hints.get(i);
                builder.append(",");
                if (i == 0) {
                    builder.append("name=").append(hint);
                } else {
                    builder.append("category").append(i).append("=").append(hint);
                }
            }
        }

        ObjectName objectName = null;
        try {
            objectName = new ObjectName(builder.toString());
        } catch (MalformedObjectNameException e) {
            logger.warn("Cannot create object name from string: " + builder.toString(), e);
        }

        return objectName;
    }

}
