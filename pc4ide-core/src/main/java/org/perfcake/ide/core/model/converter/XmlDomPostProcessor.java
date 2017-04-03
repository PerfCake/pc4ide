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

package org.perfcake.ide.core.model.converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.perfcake.ide.core.exception.PostProcessingException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * XmlDomPostProcessor uses Java DOM to post process xml file. XmlDomPostProcessor handles loading and parsing xml file and passes parsed
 * document to the {@link #performPostProcessing(Document)} method.
 *
 * @author Jakub Knetl
 */
public abstract class XmlDomPostProcessor implements SerializationPostProcessor {
    @Override
    public final void postProcess(Path file) throws PostProcessingException {
        if (!requiresPostProcessing()) {
            return;
        }
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try (InputStream inputStream = Files.newInputStream(file)) {

            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);

            performPostProcessing(document);

            // write results back to file
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            try (OutputStream outputStream = Files.newOutputStream(file)) {
                StreamResult result = new StreamResult(outputStream);
                transformer.transform(source, result);
            }
        } catch (ParserConfigurationException e) {
            throw new PostProcessingException("Cannot create document builder.", e);
        } catch (SAXException e) {
            throw new PostProcessingException("Cannot parse document.", e);
        } catch (IOException e) {
            throw new PostProcessingException("IO exception when accessing file.", e);
        } catch (TransformerConfigurationException e) {
            throw new PostProcessingException("Cannot create transformer instance.", e);
        } catch (TransformerException e) {
            throw new PostProcessingException("Cannot write stream.", e);
        }
    }

    /**
     * @return true, if this class will perform changes on the document. If this method returns false, then this class won't do nothing
     *     on {@link #postProcess(Path)} method call, so that no file will be loaded.
     */
    protected abstract boolean requiresPostProcessing();

    /**
     * Performs post processing.
     *
     * @param document Parsed DOM of the xml document
     * @throws PostProcessingException when error happens during post processing
     */
    protected abstract void performPostProcessing(Document document) throws PostProcessingException;
}
