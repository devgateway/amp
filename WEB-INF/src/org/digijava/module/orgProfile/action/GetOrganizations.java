package org.digijava.module.orgProfile.action;

import java.io.IOException;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.orgProfile.form.OrgProfileFilterForm;
import org.apache.log4j.Logger;

/**
 *
 * @author medea
 */
public class GetOrganizations  extends Action {
    private static Logger logger = Logger.getLogger(GetOrganizations.class);


	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		OrgProfileFilterForm orgForm = (OrgProfileFilterForm) form;
        Long orgGroupId=orgForm.getOrgGroupId();
        List<AmpOrganisation> orgs=DbUtil.getOrganisationByGroupId(orgGroupId);
        orgForm.setOrganizations(orgs);
		response.setContentType("text/xml");
				try {
                    OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
                    PrintWriter out = new PrintWriter(outputStream, true);
                    logger.debug("generating drop down...");
                    out.println(generateOrgDropDown(orgs));
                    out.close();
                    outputStream.close();
				} catch (IOException e) {
					// TODO handle this exception
					e.printStackTrace();
				}


		return null;
	}

    private static String generateOrgDropDown(List orgs) {
        String orgSelect = "<select name=\"org\" class=\"inp-text\" id=\"org_dropdown_id\">";
        orgSelect += "<option value=\"-1\" selected=\"selected\"><digi:trn>All</digi:trn></option>";
        Iterator<AmpOrganisation> orgIter = orgs.iterator();
        while (orgIter.hasNext()) {
            AmpOrganisation org = orgIter.next();
            orgSelect += "<option value=\"" + org.getAmpOrgId() + "\">" + DbUtil.filter(org.getName()) + "</option>";
        }
        orgSelect += "</select>";
        return orgSelect;

    }

}