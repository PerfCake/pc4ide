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
 * This exception indicates that operation was invoked with unsupported property.
 *
 * @author Jakub Knetl
 */
public class UnsupportedPropertyException extends ModelException {
    public UnsupportedPropertyException(String message) {
        super(message);
    }

    public UnsupportedPropertyException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedPropertyException(Throwable cause) {
        super(cause);
    }

    public UnsupportedPropertyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
