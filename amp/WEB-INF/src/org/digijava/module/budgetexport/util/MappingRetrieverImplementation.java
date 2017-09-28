/**
 * 
 */
package org.digijava.module.budgetexport.util;

import java.util.HashMap;
import java.util.Map;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapItem;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;

/**
 * @author alex
 *
 */
public class MappingRetrieverImplementation extends MappingRetriever {

    public MappingRetrieverImplementation(Long projectId, String viewName) {
        super(projectId, viewName);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.digijava.module.budgetexport.util.MappingRetriever#retrieveMapping()
     */
    @Override
    public Map<String, String> retrieveMapping() {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            AmpBudgetExportMapRule rule =
                BudgetExportUtil.getRuleByProjectIdAndView( getProjectId(), getViewName() );
            
            if ( rule != null && rule.getItems() != null && rule.getItems().size() > 0 ) {
                for (AmpBudgetExportMapItem item : rule.getItems() ) {
                    map.put(item.getAmpLabel(), item.getImportedCode() );
                }
            }
        } catch (DgException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return map;
    }

}
