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

package org.perfcake.ide.core.exception;

/**
 * General exception in pc4ide.
 *
 * @author Jakub Knetl
 */
public class Pc4ideException extends Exception {

    public Pc4ideException() {
    }

    public Pc4ideException(String message) {
        super(message);
    }

    public Pc4ideException(String message, Throwable cause) {
        super(message, cause);
    }

    public Pc4ideException(Throwable cause) {
        super(cause);
    }

    public Pc4ideException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
