/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.digijava.module.orgProfile.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.orgProfile.form.LargestProjectsForm;
import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.orgProfile.util.OrgProfileUtil;

/**
 *
 * @author medea
 */
public class ShowLargestProjects extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        LargestProjectsForm orgForm = (LargestProjectsForm) form;
        HttpSession session=request.getSession();
        FilterHelper filter= (FilterHelper)session.getAttribute("orgProfileFilter");
        orgForm.setProjects(OrgProfileUtil.getOrganisationLargestProjects(filter));
        return mapping.findForward("forward");

    }
}
