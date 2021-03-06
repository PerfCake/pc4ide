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

package org.perfcake.ide.core.utils;

import java.nio.file.Path;

/**
 * Class with static helper methods for paths.
 *
 * @author Jakub Knetl
 */
public class PathUtils {

    private PathUtils() {
    }

    /**
     * Converts path to string.
     *
     * @param path path
     * @return Path represented as a string. if path is null then empty string is returned.
     */
    public static String pathToString(Path path) {

        return path == null ? "" : path.toString();
    }

}
