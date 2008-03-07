/*
 *   ShowEditForumSection.java
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
import org.digijava.module.forum.exception.*;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

public class ShowEditForumSection
    extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        AdminPageForm adminPageForm = (AdminPageForm) form;
        String forward = "sectionDetails";
        ActionErrors errors = new ActionErrors();
        long sectionId = adminPageForm.getSectionId();
        ForumSection section = null;
        try {
            section =
                DbUtil.getSectionItem(sectionId);
        }
        catch (ForumException ex) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.gettingSection"));
        }
        adminPageForm.setSectionParams(section);
        return mapping.findForward(forward);
    }
}