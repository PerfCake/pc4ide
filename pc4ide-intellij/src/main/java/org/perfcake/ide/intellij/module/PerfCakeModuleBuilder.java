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

package org.perfcake.ide.intellij.module;

import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import java.io.IOException;
import org.jetbrains.annotations.NonNls;

/**
 * Created by miron on 21.1.2014. + Stanley Kaleta
 * <p>
 * Represents Perfcake module builder.
 *
 * @author Miron Michalko, Stanislav Kaleta, Jakub Knetl
 */
public class PerfCakeModuleBuilder extends JavaModuleBuilder {
    private static final Logger LOG = Logger.getInstance(PerfCakeModuleBuilder.class);

    @NonNls
    private final String messagesDirName = "messages";
    @NonNls
    private final String scenariosDirName = "scenarios";
    @NonNls
    private final String libDirName = "lib";

    @Override
    public ModuleType getModuleType() {
        return PerfCakeModuleType.getInstance();
    }

    @Override
    public String getGroupName() {
        return "PerfCake";
    }

    @Override
    public void setupRootModel(ModifiableRootModel rootModel) throws ConfigurationException {
        super.setupRootModel(rootModel);

        StartupManager.getInstance(rootModel.getProject()).runWhenProjectIsInitialized(new Runnable() {
            public void run() {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    public void run() {
                        VirtualFile moduleDir = (VirtualFileManager.getInstance().findFileByUrl(rootModel.getContentRootUrls()[0]));
                        if (moduleDir != null) {
                            try {
                                moduleDir.createChildDirectory(this, messagesDirName);
                                moduleDir.createChildDirectory(this, scenariosDirName);
                                moduleDir.createChildDirectory(this, libDirName);
                            } catch (IOException e) {
                                LOG.error("Error creating PerfCake Module Structure", e);
                            }
                        }
                    }
                });
            }
        });
    }
}