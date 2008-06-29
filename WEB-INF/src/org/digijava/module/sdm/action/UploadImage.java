/*
*   UploadImage.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id$
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

      ////System.out.println("--------- START IMAGE UPLOAD ACTION");
      ////System.out.println(sdmForm.getEljApplet());
      ////System.out.println("--------- END IMAGE UPLOAD ACTION");

        if( formFile != null ) {
            if( formFile.getFileSize() != 0 ) {
                    SdmImage img = new SdmImage();
                    img.setContentType(formFile.getContentType());
                    img.setImage(formFile.getFileData());
                    img.setName(formFile.getFileName());
                    ////System.out.println("FILE NAME: " + formFile.getFileName());

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