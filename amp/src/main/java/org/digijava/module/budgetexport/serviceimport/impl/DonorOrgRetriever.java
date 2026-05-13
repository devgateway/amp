package org.digijava.module.budgetexport.serviceimport.impl;


import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;
import org.digijava.module.budgetexport.serviceimport.ObjectRetriever;

import java.util.Map;

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
