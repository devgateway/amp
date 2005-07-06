/*
 *   TogglePublic.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Apr 28, 2004
 * 	 CVS-ID: $Id: TogglePublic.java,v 1.1 2005-07-06 10:34:04 rahul Exp $
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.highlights.dbentity.Highlight;
import org.digijava.module.highlights.form.HighlightItemForm;
import org.digijava.module.highlights.util.DbUtil;

/**
 * <p>Action toggles Highlight.showImage property:</p>
 * when false image is not showing
 */

public class TogglePublic
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        HighlightItemForm formBean = (HighlightItemForm) form;

        if (formBean.getActiveHighlightId() != null) {
            Highlight highlight = DbUtil.getHighlight(formBean.
                getActiveHighlightId());

            boolean togglePublic = highlight.isIsPublic();
            highlight.setIsPublic(!togglePublic);

            DbUtil.updateHighlight(highlight);
        }
        return mapping.findForward("forward");

    }

}
