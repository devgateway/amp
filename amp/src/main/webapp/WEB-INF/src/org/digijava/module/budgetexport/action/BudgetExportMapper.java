package org.digijava.module.budgetexport.action;

import java.util.*;

import org.digijava.module.budgetexport.reports.implementation.ColWorkerInsider;

/**
 * helper and container class which implements rewriting accesses to v_donor_funding metadata columns to corresponding columns in different column views.<br />
 * this is needed for BudgetExport reports which have hierarchies on fields which are contained in funding metadata
 * @author Dolghier Constantin
 *
 */
public class BudgetExportMapper {
    
    private Map<String, ColWorkerInsider> allKnownMappers;
    public final static Map<String, String> replacementInsiderId = Collections.unmodifiableMap(buildReplacementInsiderIdMap());
    
    public BudgetExportMapper()
    {
        allKnownMappers = new HashMap<String, ColWorkerInsider>();
    }
    
    public ColWorkerInsider getOrBuildInsider(String viewName, String columnName)
    {
        String res = buildColumnInsiderId(viewName, columnName);
        if (replacementInsiderId.containsKey(res))
            res = replacementInsiderId.get(res);
        
        if (!allKnownMappers.containsKey(res))
            allKnownMappers.put(res, new ColWorkerInsider(viewName, columnName));
        return allKnownMappers.get(res);
    }
    
    public static String buildColumnInsiderId(String viewName, String columnName)
    {
        String res = viewName + "#" + columnName;
        return res;
    }

    public static Map<String, String> buildReplacementInsiderIdMap()
    {
        Map<String, String> res = new HashMap<String, String>();
        res.put(buildColumnInsiderId("v_donor_funding", "donor_name"), buildColumnInsiderId("v_donors", "Donor Agency"));
        res.put(buildColumnInsiderId("v_donor_funding", "terms_assist_name"), buildColumnInsiderId("v_terms_assist", "Type Of Assistance"));
        res.put(buildColumnInsiderId("v_donor_funding", "donor_type_name"), buildColumnInsiderId("v_donor_type", "Donor Type"));
        res.put(buildColumnInsiderId("v_donor_funding", "financing_instrument_name"), buildColumnInsiderId("v_financing_instrument", "Financing Instrument"));
        return res;
    }
}
