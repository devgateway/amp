package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.ProposedProjCost;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class EditProposedProjectCost extends Action{
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.Exception{

        EditActivityForm eaForm = (EditActivityForm) form;
        TeamMember teamMember = (TeamMember) request.getSession().getAttribute(Constants.CURRENT_MEMBER);
        AmpApplicationSettings tempSettings = DbUtil.getTeamAppSettings(teamMember.getTeamId());

        eaForm.setReset(false);
        if(eaForm.getFunding().getProProjCost()==null){
            ProposedProjCost propProjCost=new ProposedProjCost();
            if (tempSettings != null && tempSettings.getCurrency() != null)
            	propProjCost.setCurrencyCode( tempSettings.getCurrency().getCurrencyCode() );
            else
            	propProjCost.setCurrencyCode(null);
            propProjCost.setFunAmount(null);
            propProjCost.setFunDate(null);
            eaForm.getFunding().setProProjCost(propProjCost);
        }
        return mapping.findForward("forward");
    }
}
