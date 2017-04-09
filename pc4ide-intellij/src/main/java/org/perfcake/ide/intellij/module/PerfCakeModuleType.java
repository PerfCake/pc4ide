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

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import javax.swing.Icon;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.perfcake.ide.intellij.PerfCakeIcons;

/**
 * Created by miron on 8.1.2014.  + changes
 *
 * @author Miron Michalko, Stanislav Kaleta, Jakub Knetl
 */
public class PerfCakeModuleType extends ModuleType<PerfCakeModuleBuilder> {

    @NonNls
    public static final String ID = "PERFCAKE_MODULE";

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
}
