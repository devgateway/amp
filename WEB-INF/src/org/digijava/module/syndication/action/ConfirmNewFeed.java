/*
 *   ConfirmNewsItems.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id$
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
package org.digijava.module.syndication.action;

import java.util.Date;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.syndication.dbentity.CollectorFeed;
import org.digijava.module.syndication.form.CollectorFeedForm;
import org.digijava.module.syndication.util.DbUtil;

/**
 * Action displayes confirmation page for selected news items, status of which is being changed
 */

public class ConfirmNewFeed
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        CollectorFeedForm feedForm = (CollectorFeedForm) form;

        if( feedForm != null ) {

            // get module instance
            ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);

            CollectorFeed feed = DbUtil.getFeed(moduleInstance.getSite().getSiteId(),
                                       moduleInstance.getInstanceName(), moduleInstance.getModuleName(),
                                       feedForm.getFeedUrl());

            if(feed != null && feedForm.getProcessingMode().intValue() == 1) {
                ActionErrors errors = new ActionErrors();
                errors.add(null, new ActionError("display:syndication:feedAlreadyExists"));
                saveErrors(request, errors);
                return mapping.findForward("return");
            }

            // get module instance
            feedForm.setContentType(moduleInstance.getModuleName());
            feedForm.setDateAdded(new Date());

        }

        return mapping.findForward("forward");
    }
}
