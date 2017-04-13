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

import java.util.HashSet;
import java.util.Set;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.perfcake.ide.core.exception.RuntimeJmxException;

/**
 * Represents subscription to particular MBeans.
 */
public class MBeanSubscription {
    private final Set<String> objectNames = new HashSet<>();

    /**
     * <p>Creates new subscriptions based on mbean names. Example of mbean name:</p>
     * <p>org.perfcake:class=perfcake-1,name=GeneratedSenderTasks,type=Counter</p>
     *
     * @param mbeans list of mbean names
     * @throws RuntimeJmxException if any of mbeans names is malformed {@link ObjectName}.
     */
    public MBeanSubscription(String... mbeans) throws RuntimeJmxException {
        if (mbeans != null) {
            for (String name : mbeans) {
                try {
                    ObjectName test = new ObjectName(name);
                    objectNames.add(name);
                } catch (MalformedObjectNameException e) {
                    throw new RuntimeJmxException("Cannot create mbean name:" + name, e);
                }
            }
        }
    }

    public static MBeanSubscription createEmptySubscription() {
        return new MBeanSubscription();
    }

    public Set<String> getObjectNames() {
        return objectNames;
    }
}
