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

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.ProjectWizardStepFactory;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import javax.swing.Icon;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.perfcake.ide.intellij.PerfCakeIcons;

/**
 * Created by miron on 8.1.2014.  + changes
 *
 * @author Miron Michalko, Stanislav Kaleta, Jakub Knetl
 */
public class PerfCakeModuleType extends ModuleType<PerfCakeModuleBuilder> {

    @NonNls
    public static final String ID = "PERFCAKE_MODULE";
    public static final String MESSAGES_DIR_NAME = "messages";
    public static final String SCENARIOS_DIR_NAME = "scenarios";
    public static final String PLUGINS_DIR_NAME = "lib";

    public PerfCakeModuleType() {
        super(ID);
    }

    public static PerfCakeModuleType getInstance() {
        return (PerfCakeModuleType) ModuleTypeManager.getInstance().findByID(ID);
    }

    public static boolean isOfType(Module module) {
        return get(module) instanceof PerfCakeModuleType;
    }

    @NotNull
    @Override
    public PerfCakeModuleBuilder createModuleBuilder() {
        return new PerfCakeModuleBuilder();
    }

    @NotNull
    @Override
    public String getName() {
        return "PerfCake Module";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "PerfCake scenario editor";
    }

    @Override
    public Icon getBigIcon() {
        return PerfCakeIcons.SMALL_PERFCAKE_ICON;
    }

    @Override
    public Icon getNodeIcon(boolean isOpened) {
        return PerfCakeIcons.SMALL_PERFCAKE_ICON;
    }

    @NotNull
    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull PerfCakeModuleBuilder moduleBuilder,
                                                @NotNull ModulesProvider modulesProvider) {
        return super.createWizardSteps(wizardContext, moduleBuilder, modulesProvider);
    }

    @Nullable
    @Override
    public ModuleWizardStep modifyProjectTypeStep(@NotNull SettingsStep settingsStep, @NotNull final ModuleBuilder moduleBuilder) {
        return ProjectWizardStepFactory.getInstance().createJavaSettingsStep(settingsStep, moduleBuilder,
                moduleBuilder::isSuitableSdkType);
    }
}
