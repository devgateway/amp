
package org.digijava.module.orgProfile.action;

import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.orgProfile.form.OrganizationSummaryForm;
import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.orgProfile.util.OrgProfileUtil;


/**
 *
 * @author medea
 */
public class OrgSummaryAction extends DispatchAction  {

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return view(mapping, form, request, response);
    }

    public ActionForward view(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        OrganizationSummaryForm orgForm = (OrganizationSummaryForm) form;
        HttpSession session=request.getSession();
        FilterHelper filter= (FilterHelper)session.getAttribute("orgProfileFilter");
        AmpOrganisation org=filter.getOrganization();
        if (org != null) {
            orgForm.setOrgBackground(org.getOrgBackground());
            orgForm.setOrgDescription(org.getOrgDescription());
            orgForm.setOrgId(org.getAmpOrgId());
        }
        return mapping.findForward("forward");

    }

    public ActionForward save(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        OrganizationSummaryForm orgForm = (OrganizationSummaryForm) form;
        Long orgId=orgForm.getOrgId();
        OrgProfileUtil.saveAdditionalInfo(orgId,orgForm.getOrgBackground(),orgForm.getOrgDescription());
        return mapping.findForward("forward");

    }

}
