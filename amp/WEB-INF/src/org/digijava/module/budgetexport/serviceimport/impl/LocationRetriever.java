package org.digijava.module.budgetexport.serviceimport.impl;

import org.digijava.module.budgetexport.serviceimport.ObjectRetriever;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * User: flyer
 * Date: 1/25/13
 * Time: 5:51 PM
 */
public class LocationRetriever implements ObjectRetriever {
    private static final String RET_NAME = "Freebalance Service Locations";
    @Override
    public String getName() {
        return RET_NAME;
    }

    @Override
    public Map<String, String> getItems(InputStream serviceResponseStr) {
        Map<String, String> retVal = new HashMap<String, String>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = null;
            if (builder != null) {
                doc = builder.parse(serviceResponseStr);
            }

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            XPathExpression expr = xpath.compile("//root/element/concept_name[text()='DIST']");
            NodeList result = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            if (result != null && result.getLength() > 0) {
                for (int resIdx = 0; resIdx < result.getLength(); resIdx ++) {
                    Node elementNode = result.item(resIdx).getParentNode();
                    NodeList children = elementNode.getChildNodes();
                    String code = null;
                    String name = null;
                    for (int chldIdx = 0; chldIdx < children.getLength(); chldIdx ++) {
                        Node child = children.item(chldIdx);
                        if (child.getNodeName().equalsIgnoreCase("element_code")) {
                            code = child.getFirstChild().getNodeValue();
                        }

                        if (child.getNodeName().equalsIgnoreCase("element_name")) {
                            name = child.getFirstChild().getNodeValue();
                        }

                        if (code != null && name != null) {
                            retVal.put(code, name);
                            break;
                        }
                    }
                }
            }


            int gg = 0;
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (XPathExpressionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return retVal;
    }


}
