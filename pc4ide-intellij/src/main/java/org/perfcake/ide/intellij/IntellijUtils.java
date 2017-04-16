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
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.exception.ModelSerializationException;
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.intellij.editor.ScenarioEditor;

/**
 * Class with static helper methods related to intellij.
 *
 * @author Jakub Knetl
 */
public class IntellijUtils {

    static final Logger logger = Logger.getInstance(IntellijUtils.class);

    public static final String PERFCAKE_NOTIFICATION_ID = "PerfCake Plugin";
    public static final String PLUGIN_ID = "perfcake-plugin";
    private static final NotificationGroup NOTIFICATION_GROUP = new NotificationGroup(PERFCAKE_NOTIFICATION_ID,
            NotificationDisplayType.BALLOON, true, "Event log", PerfCakeIcons.SMALL_PERFCAKE_ICON);

    private IntellijUtils() {
    }

    public static Notification createNotification(String title, NotificationType type) {
        return NOTIFICATION_GROUP.createNotification(title, type);
    }

    /**
     * Updates and redraw editor based on document content. It also manages error handling and notifies user if something goes wrong.
     *
     * @param editor   editor which will be updated
     * @param document document based on which editor is updated
     */
    public static void updateEditorContent(ScenarioEditor editor, Document document) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(document.getText().getBytes())) {

            Model model = editor.getPc4ideEditor().getScenarioManager().loadScenarioModel(inputStream);
            editor.getPc4ideEditor().updateModel((ScenarioModel) model);
        } catch (IOException e1) {
            logger.warn("Cannot create input stream from document.", e1);
            Notification notification = createNotification("Cannot re-load scenario", NotificationType.ERROR)
                    .setSubtitle(e1.getClass().getSimpleName())
                    .setContent("Cannot load scenario. See Intellij log file for more details");
            Notifications.Bus.notify(notification);
        } catch (ModelSerializationException | ModelConversionException e1) {
            logger.error("Cannot load scenario", e1);
            Notification notification = createNotification("Cannot load scenario", NotificationType.ERROR)
                    .setSubtitle(e1.getClass().getSimpleName())
                    .setContent("Cannot load scenario. See Intellij log file for more details");

            Notifications.Bus.notify(notification);
        }
    }
}
