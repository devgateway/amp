package org.digijava.module.calendar.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.calendar.form.CalendarEventForm;
import org.digijava.module.aim.util.DbUtil;
import java.util.Collection;
import java.util.*;
import org.digijava.module.aim.dbentity.AmpOrganisation;
@Deprecated
public class SelectOrganization extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {

        CalendarEventForm ceform = (CalendarEventForm) form;

        Collection orgs=DbUtil.getAllOrganisation();
        if(ceform.getSearchOrgKey()!=null && !ceform.getSearchOrgKey().equals("")){
            for (Iterator iter = orgs.iterator(); iter.hasNext(); ) {
                AmpOrganisation org = (AmpOrganisation) iter.next();
                if(org.getName().toLowerCase().indexOf(ceform.getSearchOrgKey().toLowerCase())==-1){
                    iter.remove();
                }
            }
        }
//        ceform.setOrganisations(orgs);
        return mapping.findForward("forward");
    }
}
