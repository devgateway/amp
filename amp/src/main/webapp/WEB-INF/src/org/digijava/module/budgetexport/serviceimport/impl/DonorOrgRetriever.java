package org.digijava.module.budgetexport.serviceimport.impl;


import org.apache.axis.AxisFault;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;
import org.digijava.module.budgetexport.serviceimport.ObjectRetriever;

import java.net.MalformedURLException;
import java.util.Map;

import org.apache.axis.client.Service;
import org.apache.axis.client.Call;
import org.apache.axis.message.SOAPEnvelope;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.rpc.ServiceException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.URL;

/**
 * User: flyer
 * Date: 1/28/13
 * Time: 4:48 PM
 */
public class DonorOrgRetriever extends LocationRetriever implements ObjectRetriever {
    private static String RET_NAME = "Freebalance Service Donors";
    private static String RETRIEVER_XPATH = "//root/element/concept_name[text()='FUNDS']";

    private static String getXPath() {
        return RETRIEVER_XPATH;
    }

    public Map<String, String> getItems(AmpBudgetExportMapRule rule) {
        return getItems(rule, RETRIEVER_XPATH);
        //return getItems(serviceResponseStr, RETRIEVER_XPATH);
    }

    @Override
    public String getName() {
        return RET_NAME;
    }

}
