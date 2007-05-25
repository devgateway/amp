/*
 *   ShowEditDocument.java
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
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.form.SdmForm;
import org.digijava.module.sdm.util.DbUtil;
import org.digijava.module.sdm.util.SdmCommon;

public class ShowEditDocument
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
        SdmForm formBean = (SdmForm) form;

        if ((formBean.getActiveDocumentId() != null) || (formBean.getSdmDocument() != null)) {

            // get documents List from data base
            ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(
                request);

            Sdm sdmDocument = null;

            if (formBean.getActiveDocumentId() != null) {
                sdmDocument = DbUtil.getDocument(formBean.getActiveDocumentId());
                if (sdmDocument != null) {
                    formBean.setDocumentTitle(sdmDocument.getName());
                    formBean.setSdmDocument(sdmDocument);
                }
                else {
                    formBean.setSdmDocument(null);

                    return mapping.findForward("forward");
                }
            }
            else {
                if (formBean.getSdmDocument() != null) {
                    sdmDocument = formBean.getSdmDocument();
                    formBean.setDocumentTitle(sdmDocument.getName());
                }
            }

            formBean.setDocumentItemsList(SdmCommon.loadDocumentItemsList(sdmDocument));
        }


        return mapping.findForward("forward");
    }
}