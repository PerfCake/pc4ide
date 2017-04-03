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

import java.util.HashMap;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.perfcake.ide.core.exception.PostProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XPathPostProcessor is provided with xpath expressions representing element or attribute value along with desired value. The post
 * processor goes through the expressions and for each expression it sets given attribute or element to the desired value.
 *
 * @author Jakub Knetl
 */
public class XPathPostProcessor extends XmlDomPostProcessor {

    static final Logger logger = LoggerFactory.getLogger(XPathPostProcessor.class);


    /**
     * Map of Xpath expressions and future values to be set.
     */
    private Map<String, String> expressions = new HashMap<>();

    @Override
    protected boolean requiresPostProcessing() {
        return !expressions.isEmpty();
    }

    @Override
    protected void performPostProcessing(Document document) throws PostProcessingException {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            for (String expr : expressions.keySet()) {
                XPathExpression compiledExpr = xPath.compile(expr);
                NodeList nodeList = (NodeList) compiledExpr.evaluate(document, XPathConstants.NODESET);

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);

                    node.setNodeValue(expressions.get(expr));
                }
            }
        } catch (XPathExpressionException e) {
            throw new PostProcessingException("Cannot compile xpath expression.", e);
        }
    }

    public Map<String, String> getExpressions() {
        return expressions;
    }
}
