/*
 *   PreviewDocument.java
 *   @Author Maka Kharalashvili maka@digijava.org
 *   Created: Dec 03, 2003
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

package org.digijava.module.sdm.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.form.SdmForm;
import org.digijava.module.sdm.util.DbUtil;
import org.digijava.module.sdm.util.SdmCommon;

public class PreviewDocument
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
        SdmForm formBean = (SdmForm) form;
        Sdm document = null;

        if (formBean.getActiveDocumentId() != null) {
            document = DbUtil.getDocument(formBean.getActiveDocumentId());
            formBean.setGoToEdit(false);

            if (document != null) {
                formBean.setSdmDocument(document);
            } else {
                formBean.setSdmDocument(null);
            }
        }
        else {
            if (formBean.getSdmDocument() != null) {
                document = formBean.getSdmDocument();
                formBean.setGoToEdit(true);
            }
        }
        if (document != null) {
            formBean.setDocumentTitle(document.getName());
            formBean.setDocumentItemsList(SdmCommon.loadDocumentItemsList(
                document));
        }

        return mapping.findForward("forward");
    }
}