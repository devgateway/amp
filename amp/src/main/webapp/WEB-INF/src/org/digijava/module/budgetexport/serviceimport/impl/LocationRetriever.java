package org.digijava.module.budgetexport.serviceimport.impl;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPEnvelope;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;
import org.digijava.module.budgetexport.serviceimport.ObjectRetriever;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * User: flyer
 * Date: 1/25/13
 * Time: 5:51 PM
 */
public class LocationRetriever implements ObjectRetriever {
    private static String RET_NAME = "Freebalance Service Locations";
    private static String RETRIEVER_XPATH = "//root/element/concept_name[text()='DIST']";
    private static final String NAME_ELEMENT = "element_name";
    private static final String CODE_ELEMENT = "element_code";
    
    @Override
    public String getName() {
        return RET_NAME;
    }
    
    private static String getXPath() {
        return RETRIEVER_XPATH;
    }

    public Map<String, String> getItems(AmpBudgetExportMapRule rule, String xpathStr) {
        Map<String, String> retVal = new HashMap<String, String>();
        Document doc = null;

        InputStream input = LocationRetriever.class.
                        getClassLoader().
                        getResourceAsStream("org/digijava/module/budgetexport/serviceimport/resources/SOAPRequestEvnelope.xml");
        Service service = new Service();
        Call call = null;
        SOAPEnvelope soapEnvelope = null;
        try {
            call = (Call) service.createCall();
            soapEnvelope = new SOAPEnvelope(input);
            call.setTargetEndpointAddress(new URL(rule.getProject().getMappingImportServiceURL()));
            call.setSOAPActionURI(rule.getProject().getServiceActionURL());
//            call.setTargetEndpointAddress(new URL("http://srv-pb.mof.gov.tl/PBAMP/wsDataAccessServices.asmx?WSDL"));
//            call.setSOAPActionURI("http://hq.influatec.com/foundation/webservices/AMP_ExportChartOfAccountInformation");
            call.setUseSOAPAction(true);
            soapEnvelope = call.invoke(soapEnvelope);

            doc = soapEnvelope.getAsDocument();

        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        /*
     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
     factory.setNamespaceAware(true); // never forget this!
     DocumentBuilder builder = null;*/
        try {
/*            builder = factory.newDocumentBuilder();
            Document doc = null;
            if (builder != null) {
                doc = builder.parse(serviceResponseStr);
            }*/

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            XPathExpression expr = xpath.compile(xpathStr);
            NodeList result = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            if (result != null && result.getLength() > 0) {
                for (int resIdx = 0; resIdx < result.getLength(); resIdx ++) {
                    Node elementNode = result.item(resIdx).getParentNode();
                    NodeList children = elementNode.getChildNodes();
                    String code = null;
                    String name = null;
                    for (int chldIdx = 0; chldIdx < children.getLength(); chldIdx ++) {
                        Node child = children.item(chldIdx);
                        if (child.getNodeName().equalsIgnoreCase(CODE_ELEMENT)) {
                            code = child.getFirstChild().getNodeValue();
                        }

                        if (child.getNodeName().equalsIgnoreCase(NAME_ELEMENT)) {
                            name = child.getFirstChild().getNodeValue();
                        }

                        if (code != null && name != null) {
                            retVal.put(code, name);
                            break;
                        }
                    }
                }
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return retVal;
    }

    @Override
    public Map<String, String> getItems(AmpBudgetExportMapRule rule) {
        return getItems(rule, RETRIEVER_XPATH);
    }


}
