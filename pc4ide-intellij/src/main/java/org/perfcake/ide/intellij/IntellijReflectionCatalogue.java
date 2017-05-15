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

import com.intellij.ide.plugins.cl.PluginClassLoader;
import java.net.MalformedURLException;
import java.nio.file.Path;
import org.perfcake.PerfCakeException;
import org.perfcake.ide.core.components.ReflectionComponentCatalogue;

/**
 * IntellijReflectionCatalogue is a {@link ReflectionComponentCatalogue}, which however use different method
 * for loading external jar packages. Since IntelliJ plugins uses separate classloaders.
 *
 * @author Jakub Knetl
 */
public class IntellijReflectionCatalogue extends ReflectionComponentCatalogue {

    @Override
    public void addSoftwareLibrary(Path jar) throws PerfCakeException {
        try {
            PluginClassLoader classLoader = (PluginClassLoader) this.getClass().getClassLoader();
            classLoader.addURL(jar.toUri().toURL());
        } catch (MalformedURLException e) {
            throw new PerfCakeException("Cannot add external jar to classpath.", e);
        }
    }
}
