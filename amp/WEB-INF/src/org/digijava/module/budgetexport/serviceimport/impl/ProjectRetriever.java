package org.digijava.module.budgetexport.serviceimport.impl;

import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;
import org.digijava.module.budgetexport.serviceimport.ObjectRetriever;

import java.io.InputStream;
import java.util.Map;

/**
 * User: flyer
 * Date: 1/28/13
 * Time: 4:47 PM
 */
public class ProjectRetriever extends LocationRetriever implements ObjectRetriever {
    private static final String RET_NAME = "Freebalance Service Projects";
    private static String RETRIEVER_XPATH = "//root/element/concept_name[text()='ACT']";

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
