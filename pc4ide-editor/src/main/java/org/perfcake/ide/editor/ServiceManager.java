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

import org.perfcake.ide.core.components.ComponentCatalogue;
import org.perfcake.ide.core.components.ComponentLoader;
import org.perfcake.ide.core.docs.DocsService;
import org.perfcake.ide.core.exec.PerfCakeInstallationValidator;
import org.perfcake.ide.core.model.factory.ModelFactory;
import org.perfcake.ide.editor.controller.ExecutionFactory;
import org.perfcake.ide.editor.swing.SwingFactory;
import org.perfcake.ide.editor.view.factory.ViewFactory;

/**
 * Represents manager of multiple Pc4ide related services.
 *
 * @author Jakub Knetl
 */
public interface ServiceManager {

    /**
     * @return Component loader instance.
     */
    ComponentLoader getComponentLoader();

    /**
     * @return Docs service instance.
     */
    DocsService getDocsService();

    /**
     * @return model factory instance.
     */
    ModelFactory getModelFactory();

    /**
     * @return view factory instance.
     */
    ViewFactory getViewFactory();

    /**
     * @return swing factory instance.
     */
    SwingFactory getSwingFactory();

    /**
     * @return executionFactory.
     */
    ExecutionFactory getExecutionFactory();

    /**
     * @return installation validator instance.
     */
    PerfCakeInstallationValidator getInstallationValidator();

    /**
     * @return a component catalogue.
     */
    ComponentCatalogue getComponentCatalogue();

    /**
     * Sets componentLoader.
     *
     * @param componentLoader component loader
     * @return this instance
     */
    ServiceManager setComponentLoader(ComponentLoader componentLoader);

    /**
     * Sets docs service.
     *
     * @param docsService docs service
     * @return this instance.
     */
    ServiceManager setDocsService(DocsService docsService);

    /**
     * Sets model factory.
     *
     * @param modelFactory ModelFactory
     * @return this instance
     */
    ServiceManager setModelFactory(ModelFactory modelFactory);

    /**
     * Sets view factory.
     *
     * @param viewFactory view factory
     * @return this instance
     */
    ServiceManager setViewFactory(ViewFactory viewFactory);

    /**
     * Sets swing factory.
     *
     * @param swingFactory swing factory.
     * @return this instance.
     */
    ServiceManager setSwingFactory(SwingFactory swingFactory);

    /**
     * Sets PerfCake installation validator.
     *
     * @param validator installation validator
     * @return this instance
     */
    ServiceManager setPerfCakeInstallationValidator(PerfCakeInstallationValidator validator);

    /**
     * Sets PerfCake execution factory.
     *
     * @param executionFactory execution factory
     * @return this instance
     */
    ServiceManager setExecutinFactory(ExecutionFactory executionFactory);


    /**
     * Sets PerfCake component catalogue.
     * @param catalogue catalogue
     * @return this instance
     */
    ServiceManager setComponentCatalogue(ComponentCatalogue catalogue);

}
