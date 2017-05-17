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

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.perfcake.ide.core.exception.PerfCakeResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is able to convert between Intellij Idea {@link com.intellij.openapi.vfs.VirtualFile} and {@link java.nio.file.Path}.
 *
 * @author Jakub Knetl
 */
public class VirtualFileConverter {

    static final Logger logger = LoggerFactory.getLogger(VirtualFileConverter.class);

    private VirtualFileConverter() {
    }

    /**
     * Converts Path into VirtualFile
     *
     * @param path path of a file
     * @return VirtualFile representation of a file
     * @throws PerfCakeResourceException if it cannot convert path to file.
     * @throws IllegalArgumentException  if path is null.
     */
    public static VirtualFile convertPath(Path path) throws PerfCakeResourceException, IllegalArgumentException {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        VirtualFile file = LocalFileSystem.getInstance().findFileByIoFile(path.toFile());
        if (file == null) {
            throw new PerfCakeResourceException("Cannot create VirtualFile.");
        }

        return file;
    }

    /**
     * Converts VirtualFile to a Path.
     *
     * @param file VirtualFile
     * @return Path representation of virtual file
     * @throws PerfCakeResourceException if it cannot convert file to path.
     * @throws IllegalArgumentException  if path is null.
     */
    public static Path convertPath(VirtualFile file) throws PerfCakeResourceException, IllegalArgumentException {
        if (file == null) {
            throw new IllegalArgumentException("file cannot be null");
        }

        return Paths.get(file.getCanonicalPath());
    }
}
