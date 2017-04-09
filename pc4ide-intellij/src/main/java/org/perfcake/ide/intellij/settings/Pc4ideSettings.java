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

package org.perfcake.ide.intellij.settings;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.perfcake.ide.core.Pc4ideConstants;
import org.perfcake.ide.intellij.PerfCakeIntellijConstatns;

/**
 * Represents settings of Pc4ide intellij plugin.
 *
 * @author Jakub Knetl
 */
public class Pc4ideSettings implements Configurable {

    public static final String INSTALLATION_DIR_KEY = Pc4ideConstants.FQDN_ID + ".perfcake-dir";

    private PerfCakeSettingsForm perfCakeSettingsForm;

    @Nls
    @Override
    public String getDisplayName() {
        return "PerfCake settings";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        perfCakeSettingsForm = new PerfCakeSettingsForm();
        return perfCakeSettingsForm;
    }

    @Override
    public boolean isModified() {
        String storedInstallation = PropertiesComponent.getInstance().getValue(INSTALLATION_DIR_KEY);
        String selectedInstallation = perfCakeSettingsForm.getInstallationFolder();
        if (storedInstallation == null && selectedInstallation.isEmpty())  {
            return false;
        } else {
            return !selectedInstallation.equals(storedInstallation);
        }
    }

    @Override
    public void apply() throws ConfigurationException {
        String selectedInstallation = perfCakeSettingsForm.getInstallationFolder();
        PropertiesComponent.getInstance().setValue(INSTALLATION_DIR_KEY, selectedInstallation);
    }

    @Override
    public void reset() {
        String storedInstallation = PropertiesComponent.getInstance().getValue(INSTALLATION_DIR_KEY);
        perfCakeSettingsForm.getInstallationField().setText(storedInstallation);
    }
}
