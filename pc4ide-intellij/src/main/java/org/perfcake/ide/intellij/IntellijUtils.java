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

package org.perfcake.ide.intellij;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;

/**
 * Class with static helper methods related to intellij.
 *
 * @author Jakub Knetl
 */
public class IntellijUtils {

    public static final String PERFCAKE_NOTIFICATION_ID = "PerfCake Plugin";
    public static final String PLUGIN_ID = "perfcake-plugin";
    private static final NotificationGroup NOTIFICATION_GROUP = new NotificationGroup(PERFCAKE_NOTIFICATION_ID,
            NotificationDisplayType.BALLOON, true, "Event log", PerfCakeIcons.SMALL_PERFCAKE_ICON);

    private IntellijUtils() {
    }

    public static Notification createNotification(String title, NotificationType type) {
        return NOTIFICATION_GROUP.createNotification(title, type);
    }

}
