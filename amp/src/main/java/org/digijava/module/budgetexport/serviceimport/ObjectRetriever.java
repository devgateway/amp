package org.digijava.module.budgetexport.serviceimport;

import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;

import java.io.InputStream;
import java.util.Map;

/**
 * User: flyer
 * Date: 1/25/13
 * Time: 5:35 PM
 */
public interface ObjectRetriever {
    String getName();
    Map<String, String> getItems(AmpBudgetExportMapRule rule);
}
