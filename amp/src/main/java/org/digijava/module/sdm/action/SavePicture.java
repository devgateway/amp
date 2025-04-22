/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.module.sdm.action;

import org.apache.struts.action.*;
import org.digijava.module.sdm.dbentity.SdmItem;
import org.digijava.module.sdm.form.SdmForm;
import org.digijava.module.sdm.util.DbUtil;
import org.digijava.module.sdm.util.SdmCommon;
import org.digijava.module.sdm.util.SdmParagraph;

public class SavePicture
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
        SdmForm formBean = (SdmForm) form;

        if (formBean.getContentTitle() == null || formBean.getContentTitle().trim().length() == 0) {
            return mapping.findForward("error");
        }

        if (formBean.getSdmDocument() != null) {
            SdmItem sdmItem = null;

            if (formBean.getSdmItem() != null) {
                sdmItem = formBean.getSdmItem();
            }
            else {
                sdmItem = new SdmItem();
            }

            SdmParagraph paragraph = SdmCommon.createParagraph(formBean);

            if (formBean.getFormFile() != null) {
                if (formBean.getFormFile().getFileSize() != 0) {
                    sdmItem.setContentType(formBean.getFormFile().
                                           getContentType());
                    sdmItem.setRealType(SdmItem.TYPE_IMG);
                    sdmItem.setContent(formBean.getFormFile().getFileData());
                }
            }

            if( sdmItem.getContent() == null ) {
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.sdm.pictureRequired"));
                saveErrors(request, errors);

                return mapping.getInputForward();
            }

            SdmCommon.fillParagraph(paragraph, sdmItem);
            sdmItem.setContentTitle(paragraph.getContent());

//            sdmItem.setContentTitle(SdmCommon.createParagraph(paragraph));

            // set file name
            if (formBean.getFormFile() != null &&
                formBean.getFormFile().getFileSize() != 0) {
                sdmItem.setContentText(formBean.getFormFile().getFileName());
            }

            DbUtil.addUpdateItem(formBean.getSdmDocument(), sdmItem);

            formBean.setSdmDocument(DbUtil.getDocument(formBean.getSdmDocument().
                getId()));
            formBean.setDocumentItemsList(SdmCommon.loadDocumentItemsList(
                formBean.getSdmDocument()));
        }

        return mapping.findForward("forward");
    }
}
