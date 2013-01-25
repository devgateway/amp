package org.digijava.module.budgetexport.serviceimport.impl;

import org.digijava.module.budgetexport.serviceimport.ObjectRetriever;

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
    public Map<String, String> getItems(String serviceResponseBody) {
        return null;
    }


}
