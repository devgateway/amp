

package org.digijava.module.orgProfile.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.orgProfile.util.OrgProfileUtil;
import org.digijava.module.orgProfile.form.OrgProfileNameValueYearForm;



public class ShowOrgProfileTables extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        OrgProfileNameValueYearForm orgForm = (OrgProfileNameValueYearForm) form;
        HttpSession session=request.getSession();
        FilterHelper filter= (FilterHelper)session.getAttribute("orgProfileFilter");
        orgForm.setValues(OrgProfileUtil.getData(filter,orgForm.getType()));
        return mapping.findForward("forward");

    }

}
