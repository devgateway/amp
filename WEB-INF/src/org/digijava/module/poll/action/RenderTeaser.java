/*
*   RenderTeaser.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
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

package org.digijava.module.poll.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.kernel.Constants;
import org.digijava.module.poll.form.PollForm;
import org.digijava.module.poll.util.DbUtil;
import org.digijava.kernel.util.RequestUtils;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class RenderTeaser
    extends TilesAction {

    public ActionForward execute(ComponentContext context,
                                 ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws java.lang.Exception {

        PollForm pollForm = (PollForm) form;

        String style = (String) context.getAttribute(Constants.MODULE_INSTANCE);
        if (style == null) {
            throw new ServletException("module instance must be set for poll");
        }

        pollForm.setPoll(DbUtil.getPoll(style));
        pollForm.setVoted(DbUtil.isVoted(RequestUtils.getRemoteAddress(request),
        DbUtil.getPoll(style).getPollId()));

        return null;
    }

}