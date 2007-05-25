package org.digijava.module.aim.action;

import org.apache.struts.action.*;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.form.QuickLinkForm;
import java.util.ArrayList;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.util.DbUtil;
import org.apache.log4j.Logger;
import org.digijava.module.cms.dbentity.CMSContentItem;
import javax.servlet.http.HttpServletResponse;

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
