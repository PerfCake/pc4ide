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

package org.perfcake.ide.editor.swing.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.perfcake.ide.core.command.AddPropertyCommand;
import org.perfcake.ide.core.command.Command;
import org.perfcake.ide.core.model.Property;
import org.perfcake.ide.core.model.PropertyInfo;
import org.perfcake.ide.core.model.properties.KeyValueImpl;
import org.perfcake.ide.core.model.properties.SimpleValue;
import org.perfcake.ide.editor.form.FormController;

/**
 * AddPropertyListeners is a {@link java.awt.event.ActionListener} which adds a property when action is invoked.
 *
 * @author Jakub Knetl
 */
public class AddPropertyListener implements ActionListener {

    protected FormController controller;
    protected PropertyInfo propertyInfo;
    protected String defaultKey = "";
    protected String defaultValue = "";

    /**
     * Creates new AddPropertyListener.
     *
     * @param controller   controller which owns model from which property should be removed.
     * @param propertyInfo metadata about property.
     */
    public AddPropertyListener(FormController controller, PropertyInfo propertyInfo) {
        if (propertyInfo == null) {
            throw new IllegalArgumentException("propertyInfo cannot be null");
        }
        if (controller == null) {
            throw new IllegalArgumentException("controller cannot be null");
        }
        this.controller = controller;
        this.propertyInfo = propertyInfo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Property property = createProperty();
        Command command = new AddPropertyCommand(controller.getModel(), property, propertyInfo);
        controller.getFormManager().getCommandInvoker().executeCommand(command);

        /* redraw whole form! This may be performance bottleneck. If it is so, then try to reimplement this so that
         * only few new fields are added instead of redrawing whole form. */
        controller.getFormManager().redrawPage();
    }

    protected Property createProperty() {
        Property p = null;
        switch (propertyInfo.getType()) {
            case KEY_VALUE:
                p = new KeyValueImpl(defaultKey, defaultValue);
                break;
            case VALUE:
                p = new SimpleValue(defaultValue);
                break;
            case MODEL:
                p = controller.getModelFactory().createModel(propertyInfo.getPerfCakeComponent());
                break;
            default:
                //do nothing
        }

        return p;
    }

    public String getDefaultKey() {
        return defaultKey;
    }

    /**
     * Sets default key for key-propertyInfo property.
     *
     * @param defaultKey default key
     */
    public void setDefaultKey(String defaultKey) {
        if (defaultKey == null) {
            throw new IllegalArgumentException("Default key cannot be null");
        }
        this.defaultKey = defaultKey;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets default propertyInfo for Value or Key-Value property.
     *
     * @param defaultValue default propertyInfo
     */
    public void setDefaultValue(String defaultValue) {
        if (defaultValue == null) {
            throw new IllegalArgumentException("Default propertyInfo cannot be null");
        }
        this.defaultValue = defaultValue;
    }
}
