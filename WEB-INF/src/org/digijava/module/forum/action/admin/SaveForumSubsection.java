/*
 *   SaveForumSubsection.java
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.forum.dbentity.ForumSection;
import org.digijava.module.forum.dbentity.ForumSubsection;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.form.AdminPageForm;
import org.apache.struts.action.ActionErrors;
import org.digijava.module.forum.exception.*;
import org.apache.struts.action.ActionError;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SaveForumSubsection
    extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        AdminPageForm adminPageForm = (AdminPageForm) form;
        //String forward = "index";
        String forward = "showAdminIndex";
        ActionErrors errors = new ActionErrors();

        long subsectionId = adminPageForm.getSubsectionId();

        //Create new subsection
        if (subsectionId == 0) {
            ForumSection section = null;
            try {
                section = DbUtil.getSectionItem(adminPageForm.
                                                getSectionId());
            }
            catch (ForumException ex2) {
                errors.add("forumGlobalError",
                       new ActionError("error.forum.savingSubection"));
            }
            ForumSubsection subsection =
                new ForumSubsection(adminPageForm.getSubsectionTitle(),
                                    adminPageForm.getSubsectionComment());
            adminPageForm.fillSubsection(subsection);
            subsection.setOrderIndex(section.getSubsections().size());
            subsection.setSection(section);
            section.getSubsections().add(subsection);

            try {
                DbUtil.updateSection(section);
            }
            catch (Exception ex) {
                errors.add("forumGlobalError",
                       new ActionError("error.forum.savingSubection"));
            }
        } else { //Update edited subsection
            ForumSubsection subsection = null;
            try {
                subsection = DbUtil.
                    getSubsectionItem(subsectionId);
            }
            catch (ForumException ex4) {
                errors.add("forumGlobalError",
                       new ActionError("error.forum.savingSubection"));
            }
            adminPageForm.fillSubsection(subsection);
            //Subsection was moved to another section
            if (subsection.getSection().getId()
                != adminPageForm.getSectionId()) {
                ForumSection section = null;
                try {
                    section =
                        DbUtil.getSectionItem(adminPageForm.getSectionId());
                }
                catch (ForumException ex3) {
                    errors.add("forumGlobalError",
                       new ActionError("error.forum.savingSubection"));
                }
                subsection.setSection(section);
            }
            try {
                DbUtil.updateSubsection(subsection);
            }
            catch (Exception ex1) {
                    errors.add("forumGlobalError",
                       new ActionError("error.forum.savingSubection"));
            }

        }
        return mapping.findForward(forward);
    }
}