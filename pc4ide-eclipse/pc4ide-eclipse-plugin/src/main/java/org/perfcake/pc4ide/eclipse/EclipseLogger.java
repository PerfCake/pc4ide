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

package org.perfcake.pc4ide.eclipse;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class EclipseLogger {

    /**
     * Eclipse logger instance.
     */
    private ILog logger;

    /**
     * plugin id.
     */
    private String pluginId;

    /**
     * Initilizes new logger with give ILog instance.
     *
     * @param logger logger
     * @param pluginId plugin id
     */
    public EclipseLogger(ILog logger, String pluginId) {
        super();
        this.logger = logger;
        this.pluginId = pluginId;
    }

    /**
     * Helper method which wraps logged event ot Status object.
     *
     * @param severity log event severity
     * @param msg message to be logged
     */
    private void log(int severity, String msg) {
        logger.log(new Status(severity, pluginId, msg));
    }

    /**
     * Helper method which wraps logged event ot Status object.
     *
     * @param severity log event severity
     * @param msg message to be logged
     * @param e Exception to be logged
     */
    private void log(int severity, String msg, Throwable e) {
        logger.log(new Status(severity, pluginId, msg, e));
    }

    /**
     * Log message at ok level.
     *
     * @param msg message to be logged
     */
    public void ok(String msg) {
        log(IStatus.OK, msg);
    }

    /**
     * Log message at ok level.
     *
     * @param msg message to be logged
     * @param e   exception related to the logged event
     */
    public void ok(String msg, Throwable e) {
        log(IStatus.OK, msg, e);
    }

    /**
     * Log message at info level.
     *
     * @param msg message to be logged
     */
    public void info(String msg) {
        log(IStatus.INFO, msg);
    }

    /**
     * Log message at info level.
     *
     * @param msg message to be logged
     * @param e   exception related to the logged event
     */
    public void info(String msg, Throwable e) {
        log(IStatus.INFO, msg, e);
    }

    /**
     * Log message at warning level.
     *
     * @param msg message to be logged
     */
    public void warn(String msg) {
        log(IStatus.WARNING, msg);
    }

    /**
     * Log message at warning level.
     *
     * @param msg message to be logged
     * @param e   exception related to the logged event
     */
    public void warn(String msg, Throwable e) {
        log(IStatus.WARNING, msg, e);
    }

    /**
     * Log message at error level.
     *
     * @param msg message to be logged
     */
    public void error(String msg) {
        log(IStatus.ERROR, msg);
    }

    /**
     * Log message at error level.
     *
     * @param msg message to be logged
     * @param e   exception related to the logged event
     */
    public void error(String msg, Throwable e) {
        log(IStatus.ERROR, msg, e);
    }
}