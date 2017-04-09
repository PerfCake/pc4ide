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

package org.perfcake.ide.intellij.components;

import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;
import org.perfcake.ide.core.exec.SimpleInstallationValidator;
import org.perfcake.ide.core.model.factory.ValidModelFactory;
import org.perfcake.ide.editor.AbstractServiceManager;
import org.perfcake.ide.editor.swing.DefaultSwingFactory;
import org.perfcake.ide.editor.view.factory.GraphicalViewFactory;
import org.perfcake.ide.intellij.PerfCakeIntellijConstatns;

/**
 * Implementation of service manager for intellij idea. This class is managed as Intellij Application component.
 *
 * @author Jakub Knetl
 */
public class IntellijServiceManager extends AbstractServiceManager implements ApplicationComponent {

    public static final String NAME = "ServiceManager";

    @Override
    public void initComponent() {
        docsService = createDocsService();
        modelFactory = new ValidModelFactory(docsService);
        viewFactory = new GraphicalViewFactory();
        swingFactory = new DefaultSwingFactory();
        installationValidator = new SimpleInstallationValidator();
    }

    @Override
    public void disposeComponent() {
        // do nothing
    }

    @NotNull
    @Override
    public String getComponentName() {
        return String.format("%s.%s", PerfCakeIntellijConstatns.PLUGIN_ID, NAME);
    }
}
