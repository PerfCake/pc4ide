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

import org.jetbrains.annotations.NonNls;

/**
 * Represents exception in limits of property.
 *
 * @author Jakub Knetl
 */
public class PropertyLimitException extends ModelException {

    public PropertyLimitException(@NonNls String message) {
        super(message);
    }

    public PropertyLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyLimitException(Throwable cause) {
        super(cause);
    }

    public PropertyLimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
