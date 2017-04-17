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

package org.perfcake.ide.core.model.converter.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.perfcake.ide.core.exception.PostProcessingException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Removes nodes which matches at least one of the xpath expressions. This post processor will never delete root element.
 *
 * @author Jakub Knetl
 */
public class XpathNodeRemoverPostProcessor extends XmlDomPostProcessor {

    List<String> expressions = new ArrayList<>();

    @Override
    protected boolean requiresPostProcessing() {
        return !expressions.isEmpty();
    }

    @Override
    protected void performPostProcessing(Document document) throws PostProcessingException {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();

            for (String expr : expressions) {
                XPathExpression compiledExpr = xPath.compile(expr);
                NodeList nodeList = (NodeList) compiledExpr.evaluate(document, XPathConstants.NODESET);

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    Node parent = node.getParentNode();
                    if (parent != null) {
                        parent.removeChild(node);
                    }
                }
            }
        } catch (XPathExpressionException e) {
            throw new PostProcessingException("Cannot compile xpath expression.", e);
        }
    }

    public List<String> getExpressions() {
        return expressions;
    }
}
