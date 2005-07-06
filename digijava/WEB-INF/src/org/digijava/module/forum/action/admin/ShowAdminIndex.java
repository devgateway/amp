/*
 *   ShowAdminIndex.java
 *   @Author George Kvizhinadze gio@digijava.org
 *   Created: Mar 15, 2004
 *   CVS-ID: $Id: ShowAdminIndex.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.ForumManager;
import org.digijava.module.forum.form.AdminPageForm;
import org.digijava.module.forum.dbentity.Forum;
import java.util.Map;
import org.digijava.module.forum.util.LocationTrailUtil;

public class ShowAdminIndex extends Action {
    //adminIndex
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response ) {
        String forward = "adminIndex";
        AdminPageForm adminPageForm = (AdminPageForm) form;
        adminPageForm.setSectionTitle("");
        // Get forum for the current site and instance
        RequestUtils.getModuleInstance(request);
        ModuleInstance moduleInstance = RequestUtils.getModuleInstance(
            request);
        Forum forum = ForumManager.getForum(moduleInstance);

        adminPageForm.setForum(forum);

        //Trail generation
        Map callbackMap = ForumManager.getAdminPageTrailCallbackMap();
        if (forum != null) {
            adminPageForm.setLocationTrailItems(LocationTrailUtil.getTrailItems(
                forum, callbackMap));
        }


        return mapping.findForward(forward);
    }


}