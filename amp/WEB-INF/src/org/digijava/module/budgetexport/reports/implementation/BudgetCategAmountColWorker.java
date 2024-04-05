/**
 * 
 */
package org.digijava.module.budgetexport.reports.implementation;

import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.workers.CategAmountColWorker;

import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Alex Gartner
 *  this class has a per-column insider and they are recalculated (cached in BudgetExportMapper)!
 */
public class BudgetCategAmountColWorker extends CategAmountColWorker {

    HttpSession session;
    
    /**
     * @param condition
     * @param viewName
     * @param columnName
     * @param generator
     */
    public BudgetCategAmountColWorker(String condition, String viewName,
            String columnName, ReportGenerator generator) {
        super(condition, viewName, columnName, generator);  
    }

    @Override
    protected String retrieveValueFromRS ( ResultSet rs, String tableColumnName ) throws SQLException {
        ColWorkerInsider insider = ColWorkerInsider.getOrBuildInsider(this.getViewName(), tableColumnName, this.session);
        return insider.encoder.encode( rs.getString(tableColumnName) );
    }
    
    @Override
    public void setSession(HttpSession session) {
        this.session = session;
    }
}
