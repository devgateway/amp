package org.digijava.module.fundingpledges.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.fundingpledges.form.PledgeForm;

public class SelectPledgeLocation extends Action {

    private static Logger logger = Logger.getLogger(SelectPledgeLocation.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {

        PledgeForm pledgeForm = (PledgeForm) form;
        
        String extraAction = request.getParameter("extraAction");
        if (extraAction != null)
        {
            if (extraAction.equals("add_locations_submit"))
            {
                addLocations(request, response, pledgeForm);
                return null;
            }
            
            if (extraAction.equals("add_locations_delete")){
                pledgeForm.deleteUniquelyIdentifiable(pledgeForm.getSelectedLocs(), Long.parseLong(request.getParameter("id")));
                return null;
            }
                
            
            if (extraAction.equals("add_locations_implLevelChanged"))
            {
                pledgeForm.setLevelId(Long.parseLong(request.getParameter("implLevelId")));
                if (pledgeForm.getLevelId() <= 0) {
                    //implementation level changed to "not selected"
                    CategoryManagerUtil.removeAmpCategryBykey("implementation_level"); // hack: reset category manager cache for this key
                    pledgeForm.cleanLocationData(true);
                }
                return null;
            }
            
            if (extraAction.equals("add_locations_implLocationChanged"))
            {
                pledgeForm.setImplemLocationLevel(Long.parseLong(request.getParameter("implLocationId")));
                if (pledgeForm.getImplemLocationLevel() <= 0)
                {
                    //implementation location changed to "not selected"
                    CategoryManagerUtil.removeAmpCategryBykey("implementation_location"); // hack: reset category manager cache for this key
                    pledgeForm.cleanLocationData(false); // reset selected      
                }
                return null;
            }
            if (extraAction.equals("add_locations_locations_selected"))
                return null; //ignore
        }
        
        // gone till here -> render some page
 
        return mapping.findForward("forward");
    }
    
    public void addLocations(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, PledgeForm pledgeForm)
    {
        try
        {
            String[] ids = request.getParameter("selected_loc").split(",");
            for(String id:ids)
            {
                Long lid = Long.parseLong(id);
                pledgeForm.addSelectedLocation(lid);
            }
            ARUtil.writeResponse(response, "ok");
        }
        catch(Exception e)
        {
            ARUtil.writeResponse(response, e.getMessage());
        }
    }
}
