/*
 *   MoveForumSubsection.java
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
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.ForumManager;
import org.digijava.module.forum.util.ForumConstants;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.form.AdminPageForm;
import org.digijava.module.forum.dbentity.ForumSection;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.dbentity.ForumSubsection;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import java.util.Iterator;
import org.digijava.module.forum.exception.*;


public class MoveForumSubsection extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        String forward = "showAdminIndex";
        AdminPageForm adminPageForm = (AdminPageForm) form;
        ActionErrors errors = new ActionErrors();

        long sectionId = adminPageForm.getSectionId();
        long subsectionId = adminPageForm.getSubsectionId();
        ForumSubsection subsection = null;
        try {
            subsection = DbUtil.getSubsectionItem(subsectionId);
        }
        catch (ForumException ex1) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.movingSubsection"));
        }
        int subsectionIndex = 0;
        int swapSubsectionIndex = subsection.getOrderIndex();

        ForumSection section = subsection.getSection();

        switch (adminPageForm.getMoveDirection()) {
            case (ForumConstants.MOVE_UP):
                subsectionIndex = swapSubsectionIndex - 1;
                break;
            case (ForumConstants.MOVE_DOWN):
                subsectionIndex = swapSubsectionIndex + 1;
                break;
        }


        ForumSubsection swapSubsection = null;
        Iterator it = section.getSubsections().iterator();
        while (it.hasNext()) {
            ForumSubsection tmpSubsection = (ForumSubsection) it.next();
            if (tmpSubsection.getOrderIndex() == subsectionIndex) {
                swapSubsection = tmpSubsection;
                break;
            }
        }


        if (swapSubsection!= null) {
            try {
                int ordInd = new Integer(swapSubsection.getOrderIndex()).
                    intValue();
                int swpOrdInd = new Integer(subsection.getOrderIndex()).
                    intValue();
                swapSubsection.setOrderIndex(swpOrdInd);
                subsection.setOrderIndex(ordInd);
                DbUtil.updateSection(section);

            }
            catch (Exception ex) {
                errors.add("forumGlobalError",
                           new ActionError(
                    "error.forum.errorMovingSubsection"));
            }
        } else {
            errors.add("forumGlobalError",
                       new ActionError(
                "error.forum.errorMovingSubsection"));
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }


        return mapping.findForward(forward);
    }
}