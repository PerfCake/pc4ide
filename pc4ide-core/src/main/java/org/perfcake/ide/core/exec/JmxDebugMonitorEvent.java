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

import javax.management.ObjectName;

/**
 * Represents change of value in JMX debug monitor.
 *
 * @author Jakub Knetl
 */
public class JmxDebugMonitorEvent extends ExecutionEvent {
    private ObjectName jmxObjectName;

    public JmxDebugMonitorEvent(String name, Object value, ObjectName jmxObjectName) {
        super(Type.JMX_DEBUG_MONITOR, name, value);
        this.jmxObjectName = jmxObjectName;
    }

    public ObjectName getJmxObjectName() {
        return jmxObjectName;
    }
}
