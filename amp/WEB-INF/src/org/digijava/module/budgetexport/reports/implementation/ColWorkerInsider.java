package org.digijava.module.budgetexport.reports.implementation;

import org.digijava.module.budgetexport.action.BudgetExportMapper;
import org.digijava.module.budgetexport.form.Encoder;
import org.digijava.module.budgetexport.util.BudgetExportConstants;
import org.digijava.module.budgetexport.util.MappingEncoder;

import javax.servlet.http.HttpSession;

public class ColWorkerInsider {
    MappingEncoder encoder;
    HttpSession session;
    String viewName;
    String columnName;
    
    /**
     * DO NOT CALL THIS CONSTRUCTOR DIRECTLY! Use public static ColWorkerInsider getOrBuildInsider!
     * @param viewName
     * @param columnName
     * @param generator
     */
    public ColWorkerInsider(String viewName,
            String columnName) {
        this.columnName = columnName;
        this.viewName   = viewName;
    }

    public static ColWorkerInsider getOrBuildInsider(String viewName, String columnName, HttpSession session)
    {
        if (session.getAttribute("BUDGET_EXPORTED_MAPPER") == null)
            session.setAttribute("BUDGET_EXPORTED_MAPPER", new BudgetExportMapper());
        BudgetExportMapper mpper = (BudgetExportMapper) session.getAttribute("BUDGET_EXPORTED_MAPPER");
        ColWorkerInsider insider = mpper.getOrBuildInsider(viewName, columnName);
        insider.setSession(session);
        return insider;
    }

    protected String retrieveValueFromRSObject( Object rsObj ) {
        String result = this.encoder.encode( rsObj.toString() );
        return result;
    }
    
    public void setSession(HttpSession session) {
        if (this.session != session)
        {
            // do not reinit worker needlessly
            this.session    = session;
            this.prepareEncoder();
        }
    }
    
    public void prepareEncoder() {
        String projectIdStr     = (String) session.getAttribute(BudgetExportConstants.BUDGET_EXPORT_PROJECT_ID);
        this.encoder            = new Encoder(projectIdStr, this.viewName);
    }


    /**
     * @return the encoder
     */
    public MappingEncoder getEncoder() {
        return encoder;
    }

}
