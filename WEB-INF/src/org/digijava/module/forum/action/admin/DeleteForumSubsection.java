/*
 *   DeleteForumSubsection.java
 *   @Author George Kvizhinadze gio@digijava.org
 *   Created: Mar 15, 2004
 *   CVS-ID: $Id$
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/


package org.digijava.module.forum.action.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.forum.form.AdminPageForm;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.dbentity.ForumSubsection;
import org.digijava.module.forum.dbentity.ForumSection;
import java.util.Iterator;
import org.apache.struts.action.ActionErrors;
import org.digijava.module.forum.exception.*;
import org.apache.struts.action.ActionError;

public class DeleteForumSubsection
    extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        AdminPageForm adminPageForm = (AdminPageForm) form;
        ActionErrors errors = new ActionErrors();
        String forward = "showAdminIndex";

        long subsectionId = adminPageForm.getSubsectionId();
        ForumSubsection subsection = null;
        try {
            subsection =
                DbUtil.getSubsectionItem(subsectionId);
        }
        catch (ForumException ex1) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.deletingSubection"));
        }
        ForumSection section = subsection.getSection();

        section.getSubsections().remove(subsection);


        Iterator it = section.getSubsections().iterator();
        for (int index = 0; it.hasNext(); index++) {
            ForumSubsection tmpSubs = (ForumSubsection) it.next();
            tmpSubs.setOrderIndex(index);
        }

        try {
            subsection.setLastPost(null);
            DbUtil.updateSubsection(subsection);
            DbUtil.updateSection(section);
        }
        catch (Exception ex) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.deletingSubection"));
        }
        return mapping.findForward(forward);
    }

}