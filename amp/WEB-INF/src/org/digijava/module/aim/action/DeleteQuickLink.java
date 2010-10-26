package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.CMSContentItem;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;

public class DeleteQuickLink
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        String qlId = request.getParameter("linkId");
        if(qlId != null) {
            CMSContentItem cmsItem = TeamMemberUtil.getMemberLink(Long.valueOf(qlId));
            if(cmsItem != null) {
                DbUtil.delete(cmsItem);
                return mapping.findForward("deleteRelLink");
            }
        }
        return mapping.findForward("deleteRelLink");
    }
}
