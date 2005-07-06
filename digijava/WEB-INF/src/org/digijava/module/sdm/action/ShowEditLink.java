/*
 *   ShowEditLink.java
 *   @Author Maka Kharalashvili maka@digijava.org
 *   Created: Dec 03, 2003
 * 	 CVS-ID: $Id: ShowEditLink.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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
import org.digijava.module.sdm.dbentity.SdmItem;
import org.digijava.module.sdm.form.SdmForm;
import org.digijava.module.sdm.util.SdmCommon;
import org.digijava.module.sdm.util.SdmParagraph;

public class ShowEditLink
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
        SdmForm formBean = (SdmForm) form;

        formBean.setContent("http://");
        formBean.setSdmItem(null);

        if ( (formBean.getActiveParagraphOrder() != null) ||
            (formBean.getSdmDocument() != null)) {

            if (formBean.getSdmDocument() != null) {
                formBean.setDocumentTitle(formBean.getSdmDocument().getName());
            }
            if (formBean.getActiveParagraphOrder() != null) {
                SdmItem itemEdit = formBean.getSdmDocument().getItemByIndex(
                    formBean.getActiveParagraphOrder());

                formBean.setSdmItem(itemEdit);

                SdmParagraph paragraph = SdmCommon.createParagraph(itemEdit);

                if (paragraph != null) {
                    SdmCommon.setFormParagraph(formBean, paragraph);

                    formBean.setContentTitle(paragraph.getContent());
                    formBean.setContent(itemEdit.getContentText());
                }

            }

        }
        return mapping.findForward("forward");
    }
}