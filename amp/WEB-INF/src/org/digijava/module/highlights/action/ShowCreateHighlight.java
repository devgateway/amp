/*
 *   ShowCreatetHighlight.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 11, 2003
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

package org.digijava.module.highlights.action;

import java.util.ArrayList;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.highlights.dbentity.Highlight;
import org.digijava.module.highlights.form.HighlightItemForm;
import org.digijava.module.highlights.util.DbUtil;

/**
 * <p>Action renders form, whith which Highlight is created</p>
 * if active Highlight is presented,Action edits active Highlight
 */

public class ShowCreateHighlight
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        HighlightItemForm formBean = (HighlightItemForm) form;

        Highlight highlight = null;
        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);
        highlight = DbUtil.getHighlight(moduleInstance.getSite().
                                        getSiteId(),
                                        moduleInstance.getInstanceName());

        if (highlight != null) {
            return new ActionForward("/highlights/showEditHighlight.do", true);
        }

        if (request.getParameter("reset") == null) {

            HighlightItemForm.LinkInfo link = new HighlightItemForm.LinkInfo();
            formBean.setLinks(new ArrayList());
            link.setUrl(request.getScheme() + "://");
            formBean.getLinks().add(link);

            formBean.setLayout("1");
            formBean.setLayout1(true);
            formBean.setLayout2(false);
            formBean.setLayout3(false);

            formBean.setFormReset(true);
            formBean.setShortTopicLength(Highlight.MAX_VISIBLE_TEXT_LENGTH);

            formBean.setImage(null);
        }

        return mapping.findForward("forward");
    }

}