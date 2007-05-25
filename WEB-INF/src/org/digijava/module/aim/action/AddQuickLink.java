package org.digijava.module.aim.action;

import java.util.*;
import javax.servlet.http.*;

import org.apache.log4j.*;
import org.apache.struts.action.*;
import org.digijava.module.aim.form.*;
import org.digijava.module.aim.helper.*;
import org.digijava.module.aim.util.*;
import org.digijava.module.cms.dbentity.*;

public class AddQuickLink
    extends Action {
    private static Logger logger = Logger.getLogger(AddQuickLink.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        QuickLinkForm qlForm = (QuickLinkForm) form;
        qlForm.setId(null);

        HttpSession session = request.getSession();
        String mId = request.getParameter("memId");
        if(mId != null) {
            qlForm.setTempId(mId);
        }

        if(qlForm.getTempId() != null &&
           qlForm.getLinkName() != null &&
           qlForm.getLink() != null) {
            CMSContentItem cmsItem = new CMSContentItem();

            cmsItem.setDescription(" ");
            byte file[] = new byte[1];
            file[0] = 0;
            cmsItem.setFile(file);
            cmsItem.setIsFile(false);
            cmsItem.setTitle(qlForm.getLinkName());
            cmsItem.setUrl(qlForm.getLink());

            qlForm.setId(new Long(Long.parseLong(qlForm.getTempId())));
            TeamMemberUtil.addLinkToMember(qlForm.getId(), cmsItem);

            Documents document = new Documents();
            document.setDocId(new Long(cmsItem.getId()));
            document.setTitle(cmsItem.getTitle());
            document.setIsFile(cmsItem.getIsFile());
            document.setFileName(cmsItem.getFileName());
            document.setUrl(cmsItem.getUrl());
            document.setDocDescription(cmsItem.getDescription());
            Collection col = (Collection) session.getAttribute(
                Constants.MY_LINKS);
            if(col == null) {
                col = new ArrayList();
            }
            col.add(document);
            session.setAttribute(Constants.MY_LINKS, col);
            return mapping.findForward("forward");
        }
        return mapping.findForward("addRelLink");
    }
}

