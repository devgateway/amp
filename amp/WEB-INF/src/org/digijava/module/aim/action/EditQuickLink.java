package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.CMSContentItem;
import org.digijava.module.aim.form.QuickLinkForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;

public class EditQuickLink
    extends Action {
    private static Logger logger = Logger.getLogger(EditQuickLink.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        QuickLinkForm qlForm = (QuickLinkForm) form;

        HttpSession session = request.getSession();

        String qlId = request.getParameter("linkId");
        if(qlId != null) {
            CMSContentItem cmsItem = TeamMemberUtil.getMemberLink(Long.valueOf(qlId));
            qlForm.setId(Long.valueOf(qlId));
            qlForm.setLink(cmsItem.getUrl());
            qlForm.setLinkName(cmsItem.getTitle());
            return mapping.findForward("editRelLink");
        }

        CMSContentItem cmsItem = TeamMemberUtil.getMemberLink(qlForm.getId());
        if(cmsItem != null) {
            cmsItem.setUrl(qlForm.getLink());
            cmsItem.setTitle(qlForm.getLinkName());
            DbUtil.update(cmsItem);
            return mapping.findForward("editRelLink");
        }

        return mapping.findForward("editRelLink");
    }
}
