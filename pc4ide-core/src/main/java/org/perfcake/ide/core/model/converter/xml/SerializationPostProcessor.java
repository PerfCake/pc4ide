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

package org.perfcake.ide.core.model.converter.xml;

import java.nio.file.Path;
import org.perfcake.ide.core.exception.PostProcessingException;

/**
 * Serialization post processor performs additional processing of a serialized model.
 *
 * @author Jakub Knetl
 */
public interface SerializationPostProcessor {

    /**
     * Performs post processing on given file.
     *
     * @param file file which should be processed
     * @throws PostProcessingException when it is not possible to post process file.
     */
    void postProcess(Path file) throws PostProcessingException;
}
