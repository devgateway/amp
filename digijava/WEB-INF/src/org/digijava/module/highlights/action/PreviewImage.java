/*
 *   PreviewImage.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 13, 2003
 * 	 CVS-ID: $Id: PreviewImage.java,v 1.1 2005-07-06 10:34:04 rahul Exp $
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

import javax.servlet.ServletOutputStream;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.highlights.form.HighlightItemForm;

/**
 * Action is invoked when previewing the Highlight and previews Highlight image if the last exists
 */

public class PreviewImage
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

        HighlightItemForm formBean = (HighlightItemForm) form;

        if (formBean.getImage() != null) {
          if (formBean.getContentType() != null) {
            response.setContentType(formBean.getContentType());
          }

          ServletOutputStream output = response.getOutputStream();
            output.write(formBean.getImage());
            output.flush();
        }
        return null;
    }
}