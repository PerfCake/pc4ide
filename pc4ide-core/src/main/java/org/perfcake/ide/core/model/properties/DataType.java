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

package org.perfcake.ide.core.model.properties;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents data type of a value.
 *
 * @author Jakub Knetl
 */
public enum DataType {

    STRING, INTEGER, FLOAT, BOOLEAN("true", "false"), PERIOD("time,iterations"), MODEL;

    private List<String> values;

    DataType() {
    }

    DataType(String... values) {
        this.values = Arrays.asList(values);
    }

    /**
     * Return list of possible values. Applicable only in case that this data type can enumarate all acceptable values.
     *
     * @return List of possible values if it is possible to enumerate them, false otherwise
     */
    public List<String> getValues() {
        return values;
    }

    /**
     * Determines whether value is a placeholder.
     *
     * @param value value
     * @return true if value is a placeholder
     */
    public static boolean isPlaceholder(String value) {
        if (value == null) {
            return false;
        }

        Pattern pattern = Pattern.compile("\\$\\{.*\\}");
        Matcher matcher = pattern.matcher(value);

        return matcher.matches();
    }

    /**
     * Detects a simple data type from java class. It recognizes integer, floating point numbers, boolean. If non of these apply than
     * String is used as representation.
     *
     * @param type type which represents type in java class
     * @return either BOOLEAN, INTEGER, FLOAT, or STRING
     */
    public static DataType detectFromJavaType(Class<?> type) {
        // is boolean value
        if (boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)) {
            return BOOLEAN;
        }

        //is integer
        if (int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type)
                || long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type)) {
            return INTEGER;
        }

        // is a floating point number?
        if (float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)
                || double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type)) {
            return FLOAT;
        }

        // otherwise return string
        return STRING;
    }
}
