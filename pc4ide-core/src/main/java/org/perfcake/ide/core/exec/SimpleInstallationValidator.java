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

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Detects whether installation dir contains PerfCake installation simply by looking for PerfCake Jar.
 *
 * @author Jakub Knetl
 */
public class SimpleInstallationValidator implements PerfCakeInstallationValidator {

    static final Logger logger = LoggerFactory.getLogger(SimpleInstallationValidator.class);

    @Override
    public boolean isValid(Path installationDir) {
        return findPerfCakeJar(installationDir) != null;
    }

    /**
     * Finds PerfCake jar in its installation path.
     *
     * @param installationDir installation dir of PerfCake
     * @return Path to the perfcake jar or null if no such jar can be found in installationDir/lib folder.
     */
    public Path findPerfCakeJar(Path installationDir) {
        Path libDir = installationDir.resolve("lib");
        if (!Files.exists(libDir)) {
            return null;
        }
        Path perfCakeJar = null;
        DirectoryStream.Filter<Path> regexFilter = new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path entry) throws IOException {
                Path filename = entry.getFileName();
                String regex = "perfcake-\\d\\d*\\.\\d\\d*\\.jar";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(filename.toString());

                return matcher.matches();
            }
        };

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(libDir, regexFilter)) {
            Iterator<Path> it = directoryStream.iterator();
            if (it.hasNext()) {
                perfCakeJar = it.next();
            }

        } catch (IOException e) {
            logger.warn("Cannot find perfcake jar.", e);
        }

        return perfCakeJar;
    }
}
