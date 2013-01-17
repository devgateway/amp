/*
 * Created on 8/03/2006
 * @author akashs
 *
 */
package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.SurveyFunding;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.gateperm.core.GatePermConst;

@Deprecated
public class EditSurveyList extends Action {

    private static Logger logger = Logger.getLogger(EditSurveyList.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {

        // if user is not logged in, forward him to the home page
        if (request.getSession().getAttribute("currentMember") == null)
            return mapping.findForward("index");

        TeamMember tm = (TeamMember) request.getSession().getAttribute("currentMember");

        
        // if user is not a DONOR then forward him to his portfolio
       // if (!tm.getTeamType().equalsIgnoreCase("GOVERNMENT"))
        //      return mapping.findForward("viewMyDesktop");

        logger.debug("In edit survey list action");

        EditActivityForm svForm = (EditActivityForm) form;
        logger.debug("step[before] : " + svForm.getStep());
        svForm.setStep("17"); // for indicators tab in donor-view
        logger.debug("step[after] : " + svForm.getStep());
        
        //this is needed to acknowledge that we are still under EDIT ACTIVITY mode:
        request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.EDIT);

        Comparator sfComp = new Comparator() {
            public int compare(Object o1, Object o2) {
                SurveyFunding sf1 = (SurveyFunding) o1;
                SurveyFunding sf2 = (SurveyFunding) o2;
                return sf1.getFundingOrgName().trim().toLowerCase().compareTo(sf2.getFundingOrgName().trim().toLowerCase());
            }
        };
        svForm.setEditAct(false);
        List<SurveyFunding> surveyColl = new ArrayList<SurveyFunding>();
        if (svForm.isEditAct() == true) {
        	surveyColl = (List<SurveyFunding>) DbUtil.getAllSurveysByActivity(svForm.getActivityId(), svForm);
        	Collections.sort(surveyColl, sfComp);
            svForm.setSurveyFundings(surveyColl);
        } else {
        	//This is a new activity not saved yet.
        	//If the activity has fundings then a survey can be taken.
        	if(svForm.getFunding() != null 
        			&& svForm.getFunding().getFundingOrganizations()!= null 
        			&& svForm.getFunding().getFundingOrganizations().size() > 0 
        			&& svForm.getFunding().getFundingDetails() != null
        			&& svForm.getFunding().getFundingDetails().size() > 0) {
        		Iterator<FundingOrganization> iterOrgFundings = svForm.getFunding().getFundingOrganizations().iterator();
        		int tempID = 0;
        		while (iterOrgFundings.hasNext()) {
        			FundingOrganization fundingOrganization = iterOrgFundings.next();
        			SurveyFunding auxSurveyFunding = new SurveyFunding();
        			AmpOrganisation auxOrganization = DbUtil.getOrganisation(fundingOrganization.getAmpOrgId());
        			auxSurveyFunding.setAcronim(auxOrganization.getOrgCode());
        			//TODO: Check if the user entered a second time and changed the Point of Delivery Donor.
        			auxSurveyFunding.setDeliveryDonorName(auxOrganization.getName());
        			auxSurveyFunding.setFundingOrgName(auxOrganization.getName());
        			//I have to assign an ID, for new surveys I use negative numbers and the donor id.
        			tempID = fundingOrganization.getAmpOrgId().intValue() * -1;
        			auxSurveyFunding.setSurveyId(new Long(tempID));
        			surveyColl.add(auxSurveyFunding);
        		}
        		svForm.setSurveyFundings(surveyColl);
        	}
        }
        return mapping.findForward("forward");
    }
}
