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

package org.perfcake.ide.editor;

import org.perfcake.ide.core.components.ComponentLoaderImpl;
import org.perfcake.ide.core.exec.SimpleInstallationValidator;
import org.perfcake.ide.core.model.factory.ValidModelFactory;
import org.perfcake.ide.editor.swing.DefaultSwingFactory;
import org.perfcake.ide.editor.view.factory.GraphicalViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link ServiceManager}. This class is implemented as singleton.
 *
 * @author Jakub Knetl
 */
public class DefaultServiceManager extends AbstractServiceManager implements ServiceManager {

    static final Logger logger = LoggerFactory.getLogger(DefaultServiceManager.class);

    public static ServiceManager INSTANCE = null;

    private DefaultServiceManager() {
        super();

        docsService = createDocsService();
        modelFactory = new ValidModelFactory(docsService);
        viewFactory = new GraphicalViewFactory();
        swingFactory = new DefaultSwingFactory();
        installationValidator = new SimpleInstallationValidator();
        componentLoader = new ComponentLoaderImpl();
    }

    /**
     * @return Instance of this class.
     */
    public static ServiceManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DefaultServiceManager();
        }

        return INSTANCE;
    }

}
