package org.digijava.module.aim.action;

import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.cms.dbentity.CMSContentItem;
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
