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

import java.util.List;

/**
 * Execution listener listens for information about PerfCake scenario execution.
 *
 * @author Jakub Knetl
 */
public interface ExecutionListener {

    /**
     * Handles execution event.
     *
     * @param event Execution event.
     */
    void handleEvent(ExecutionEvent event);

    /**
     * @return <p>List of names describing the JMX MBean name. Domain part of the MBean name should not appear in the list. First element
     * of the list is the name, additional elements are categories. Categories are numbered starting from 1.
     * </p>
     * <p>
     * For example if this method returns list
     * </p>
     * <p>
     * ["reporting", "my-implementation", "my-description"]
     * </p>
     * <p>
     * then it is interpreted as following name:
     * </p>
     * <p>
     * org.example.somedomain:name=reporting,category1=my-implmentation,category2="my-description"
     * </p>
     * <p>
     * If this method return empty list, then this execution listener is not interested in particular MBean.
     * </p>
     */
    List<String> getObjectNameHints();

}
