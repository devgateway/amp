package org.digijava.module.budgetexport.serviceimport.impl;

import org.digijava.module.budgetexport.serviceimport.ObjectRetriever;

import java.io.InputStream;
import java.util.Map;

/**
 * User: flyer
 * Date: 1/28/13
 * Time: 4:48 PM
 */
public class DonorOrgRetriever implements ObjectRetriever {
    private static final String RET_NAME = "Freebalance Service Donors";
    @Override
    public String getName() {
        return RET_NAME;
    }

    @Override
    public Map<String, String> getItems(InputStream serviceResponseStr) {
        return null;
    }
}
