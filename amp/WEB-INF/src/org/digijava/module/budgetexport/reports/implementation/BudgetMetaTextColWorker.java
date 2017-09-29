/**
 * 
 */
package org.digijava.module.budgetexport.reports.implementation;

import javax.servlet.http.HttpSession;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.workers.MetaTextColWorker;
import org.digijava.module.budgetexport.util.MappingEncoder;

/**
 * @author Alex Gartner
 *
 */
public class BudgetMetaTextColWorker extends MetaTextColWorker {
    
    
    ColWorkerInsider insider;

    /**
     * @param condition
     * @param viewName
     * @param columnName
     * @param generator
     */
    public BudgetMetaTextColWorker(String condition, String viewName,
            String columnName, ReportGenerator generator) {
        super(condition, viewName, columnName, generator);
    }

    @Override
    protected String retrieveValueFromRSObject( Object rsObj ) {
        return this.insider.encoder.encode( rsObj.toString() );
    }
    
    @Override
    public void setSession(HttpSession session) {
        this.insider = ColWorkerInsider.getOrBuildInsider(viewName, columnName, session);
    }
    
    @Override
    public MappingEncoder getEncoder () {
        if ( insider != null )
            return insider.getEncoder();
        return null;
    }
    
    @Override
    public String encodeUnallocatedString(String originalString) {
        if ( originalString != null && originalString.toLowerCase().contains(ArConstants.UNALLOCATED.toLowerCase()) )
            return this.insider.encoder.encode( originalString );
        else
            return originalString;
    }
    
}
