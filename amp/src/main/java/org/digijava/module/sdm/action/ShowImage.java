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

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.util.ResponseUtil;
import org.digijava.module.common.util.ImageInfo;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.dbentity.SdmItem;
import org.digijava.module.sdm.form.SdmForm;
import org.digijava.module.sdm.util.DbUtil;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ShowImage
    extends Action {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(ShowImage.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

        SdmForm formBean = (SdmForm) form;
        Sdm sdmDoc = null;

        Long paramDocId = null;
        if(request.getParameter("documentId")!=null){
            paramDocId=new Long(request.getParameter("documentId"));
        } else {
            if(request.getSession().getAttribute("document")!=null){ //view file from messaging/help module
                sdmDoc=(Sdm)request.getSession().getAttribute("document");
            }else{ //from sdm module
                sdmDoc = formBean.getSdmDocument();
            }      
          }
        
        
//        if (paramDocId == null) {
//            sdmDoc = formBean.getSdmDocument();
//        }
        Long paramParagId = new Long(request.getParameter("activeParagraphOrder"));

        if (paramParagId != null) {
            byte[] picture = null;
            String contentType = null;
            if (paramDocId == null) {
                if (sdmDoc == null) {
                    return null;
                }
                SdmItem sdmItem = sdmDoc.getItemByIndex(paramParagId);

                if (sdmItem != null) {
                    picture = sdmItem.getContent();
                    contentType = sdmItem.getContentType();
                }
            }
            else {
                ImageInfo imageInfo = DbUtil.getSdmImage(paramDocId.
                    longValue(), paramParagId.longValue());
                if (imageInfo != null)
                {
                    picture = imageInfo.getImageData();
                    contentType = imageInfo.getContentType();
                }
            }
            if (picture != null) {
                ResponseUtil.writeFile(request, response, contentType, null, picture);
            }

        }

        return null;
    }

}
