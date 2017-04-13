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

/**
 * Execution manager manages execution of a PerfCake scenario.
 *
 * @author Jakub Knetl
 */
public interface ExecutionManager {

    /**
     * Adds execution listener to this manager.
     *
     * @param listener listener which will be notified about events.
     */
    void addListener(ExecutionListener listener);


    /**
     * Adds execution listener to this manager. Additionally, if execution is in debug mode, the listener will be notified about
     * value of subscribed mbeans
     *
     * @param listener          listener which will be notified about events.
     * @param mBeanSubscription MBeans names in which listener is interested.
     */
    void addListener(ExecutionListener listener, MBeanSubscription mBeanSubscription);

    /**
     * Removes listener.
     *
     * @param listener listener to be removed.
     */
    void removeListener(ExecutionListener listener);

    /**
     * Gets Mbean subscription for particular listener.
     *
     * @param listener listener
     * @return MBean subscription for listener. If listener does not specified any mbean, then empty mbean subscription is returned.
     */
    MBeanSubscription getMbeanSubscription(ExecutionListener listener);

    /**
     * @return Instance of process which is managed by this manager.
     */
    Process getProcess();

    /**
     * @return true if execution runs in debug mode, thus JMX debug agent is installed in the process.
     */
    boolean isDebug();

    /**
     * @return True if the process is still running.
     */
    boolean isRunning();

    /**
     * This method will start watching for execution events. Before calling this method all listeners should be already registered,
     * otherwise listeners may miss some events (e.g. about start of execution). Watching ends when process finishes.
     */
    void startWatching();

    /**
     * Stops watching for process immediately.
     */
    void stopWatching();


    /**
     * <p>Creates mbean query based on perfcake format. In perfcake, it will always following format:</p>
     * <p>org.perfcake:class=debug-agent-name,name=value1,category1=value2,category2=value3,...,type=COUNTER</p>
     * <p>,where value1,value2,value3,... comes from value argument</p>
     *
     * @param values values
     * @return string representing mbean name
     */
    String createCounterMBeanQuery(String... values);
}
