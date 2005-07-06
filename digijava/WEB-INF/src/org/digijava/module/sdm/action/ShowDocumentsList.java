/*
 *   ShowDocumentsList.java
 *   @Author Maka Kharalashvili maka@digijava.org
 *   Created: Dec 03, 2003
 * 	 CVS-ID: $Id: ShowDocumentsList.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

package org.digijava.module.sdm.action;

import java.util.List;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.sdm.form.SdmForm;
import org.digijava.module.sdm.util.DbUtil;

public class ShowDocumentsList
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
        SdmForm formBean = (SdmForm) form;
        List documentsList = null;

        // get documents List from data base
        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

        documentsList = DbUtil.getDocuments(moduleInstance.getSite().
                                            getSiteId(),
                                            moduleInstance.getInstanceName());

        if (documentsList != null) {
            formBean.setDocumentsList(documentsList);
        }
        else {
            formBean.setDocumentsList(null);
        }

        return mapping.findForward("forward");
    }
}
