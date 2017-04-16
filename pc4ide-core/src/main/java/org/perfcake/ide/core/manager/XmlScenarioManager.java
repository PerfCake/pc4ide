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

package org.perfcake.ide.core.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.ide.core.exception.ModelSerializationException;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.serialization.ModelLoader;
import org.perfcake.ide.core.model.serialization.ModelWriter;

/**
 * XML scenario manager manages a xml scenario file.
 *
 * @author Jakub Knetl
 */
public class XmlScenarioManager implements ScenarioManager {

    private Path location;
    private ModelWriter writer;
    private ModelLoader loader;

    /**
     * Creates new XML scenario manager.
     *
     * @param location location of scenario.
     */
    public XmlScenarioManager(Path location) {
        if (location == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        this.location = location;
        this.writer = new ModelWriter();
        this.loader = new ModelLoader();
    }

    @Override
    public ScenarioModel loadScenarioModel() throws ModelSerializationException, ModelConversionException {
        ScenarioModel scenarioModel = null;
        try (InputStream inputStream = Files.newInputStream(location)) {
            scenarioModel = loadScenarioModel(inputStream);
        } catch (IOException e) {
            throw new ModelSerializationException("Cannot create inputstream", e);
        }
        return scenarioModel;
    }

    @Override
    public ScenarioModel loadScenarioModel(InputStream inputStream) throws ModelSerializationException, ModelConversionException {
        return loader.loadModel(inputStream);
    }

    @Override
    public void writeScenario(ScenarioModel model) throws ModelSerializationException, ModelConversionException {

        try (OutputStream outputStream = Files.newOutputStream(location)) {
            writeScenario(model, outputStream);
        } catch (IOException e) {
            throw new ModelSerializationException("Cannot create output stream.", e);
        }
    }

    @Override
    public void writeScenario(ScenarioModel model, OutputStream outputStream) throws ModelSerializationException, ModelConversionException {
        writer.writeScenario(model, outputStream);
    }

    @Override
    public Path getScenarioLocation() {
        return location;
    }

    @Override
    public void setScenarioLocation(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        this.location = path;
    }
}
