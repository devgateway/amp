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
import org.digijava.module.sdm.form.SdmForm;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UploadImage
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        SdmForm sdmForm = (SdmForm)form;

/*        FormFile formFile = sdmForm.getImage();

      //System.out.println("--------- START IMAGE UPLOAD ACTION");
      //System.out.println(sdmForm.getEljApplet());
      //System.out.println("--------- END IMAGE UPLOAD ACTION");

        if( formFile != null ) {
            if( formFile.getFileSize() != 0 ) {
                    SdmImage img = new SdmImage();
                    img.setContentType(formFile.getContentType());
                    img.setImage(formFile.getFileData());
                    img.setName(formFile.getFileName());
                    //System.out.println("FILE NAME: " + formFile.getFileName());

                  DbUtil.updateImage(img);
            }
        }

        if(sdmForm.getEljApplet() != null) {
            String parsed = sdmForm.getEljApplet();
            sdmForm.setContent(parsed.replaceAll("~/","~"));
        }
*/
        return mapping.findForward("forward");
    }


}
