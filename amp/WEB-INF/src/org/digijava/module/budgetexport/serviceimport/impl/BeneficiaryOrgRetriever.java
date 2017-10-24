package org.digijava.module.budgetexport.serviceimport.impl;

import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;
import org.digijava.module.budgetexport.serviceimport.ObjectRetriever;

import java.util.Map;

/**
 * User: flyer
 * Date: 2/6/13
 * Time: 6:47 PM
 */
public class BeneficiaryOrgRetriever extends LocationRetriever implements ObjectRetriever {
    private static String RET_NAME = "Freebalance Service Beneficiary";
    private static String RETRIEVER_XPATH = "//root/element/concept_name[text()='DIV']";

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
