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

package org.perfcake.ide.intellij.editor;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jetbrains.annotations.NotNull;
import org.perfcake.PerfCakeConst;
import org.xml.sax.SAXException;

/**
 * Created by Stanislav Kaleta on 4/23/15.
 * Updated by Jakub Knetl
 */
public class PerfCakeScenarioUtil {

    /**
     * Determines whether file contains a PerfCake scenario.
     *
     * @param file file to be checked.
     * @return true if the file contains PerfCake scenario, false otherwise
     */
    public static boolean isPerfCakeScenario(@NotNull VirtualFile file) {
        return isDslScenario(file) || isXmlScenario(file);
    }

    // public static ScenarioManager getScenarioManager(@NotNull Project project, @NotNull VirtualFile file){
    // if (isXmlScenario(file)){
    //  return new ScenarioManagerImpl(file, project);
    // }
    // if (isDslScenario(file)){
    //  return new DslScenarioManager(file, project);
    // }
    //  throw new UnsupportedOperationException("unexpected error - scenario type can't be find!");
    // }
    private static boolean isDslScenario(@NotNull VirtualFile file) {
        if (!file.getName().contains(".dsl")) {
            return false;
        }
        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document == null) {
            return false;
        }
        if (document.getText().split("\n")[0].startsWith("scenario ")) {
            return true;
        }
        return false;
    }

    private static boolean isXmlScenario(@NotNull VirtualFile file) {
        if (file.getFileType() == StdFileTypes.XML && !StdFileTypes.XML.isBinary()) {
            try {
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = builderFactory.newDocumentBuilder();
                org.w3c.dom.Document document = builder.parse(file.getInputStream());

                if (document != null) {
                    String xmlNsAttr = document.getDocumentElement().getAttribute("xmlns");
                    if (xmlNsAttr.equals("urn:perfcake:scenario:" + PerfCakeConst.XSD_SCHEMA_VERSION)) {
                        return true;
                    }
                }
            } catch (ParserConfigurationException | IOException e) {
                e.printStackTrace();
                return false;
            } catch (SAXException e) {
                com.intellij.openapi.editor.Document document = FileDocumentManager.getInstance().getDocument(file);

                if (document != null) {
                    String text = document.getText();
                    String version = PerfCakeConst.XSD_SCHEMA_VERSION;
                    if (text.contains("<scenario xmlns=\"urn:perfcake:scenario:" + version + "\">")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
