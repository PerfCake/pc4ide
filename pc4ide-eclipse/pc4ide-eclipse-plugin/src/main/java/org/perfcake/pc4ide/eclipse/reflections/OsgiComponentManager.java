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

package org.perfcake.pc4ide.eclipse.reflections;

import java.io.InputStream;
import java.util.List;

import org.osgi.framework.Bundle;
import org.perfcake.ide.core.components.ComponentManager;
import org.reflections.Reflections;
import org.reflections.vfs.Vfs;

public class OsgiComponentManager extends ComponentManager {

    public OsgiComponentManager(InputStream javadocStream, List<String> packagesToScan) {
        super(javadocStream, packagesToScan);
    }

    @Override
    protected Reflections createReflections() {
        final Bundle bundle = org.perfcake.pc4ide.eclipse.Activator.getInstance().getBundle();
        Vfs.addDefaultURLTypes(new BundleUrlType(bundle));
        return super.createReflections();
    }
}
