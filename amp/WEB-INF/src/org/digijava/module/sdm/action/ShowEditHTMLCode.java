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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.sdm.dbentity.SdmItem;
import org.digijava.module.sdm.form.SdmForm;

public class ShowEditHTMLCode
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
        SdmForm formBean = (SdmForm) form;

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

                //set text
                formBean.setContentTitle(itemEdit.getContentText());

            }

        }

        return mapping.findForward("forward");
    }
}
