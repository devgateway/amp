/*
*   UserBioForm.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: UserBioForm.java,v 1.1 2005-07-06 10:34:17 rahul Exp $
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

package org.digijava.module.um.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.digijava.module.um.util.DbUtil;
import org.digijava.kernel.user.User;
import org.digijava.kernel.Constants;
import org.apache.struts.validator.ValidatorForm;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UserBioForm
    extends ValidatorForm {

    protected String bioText;
    protected FormFile photoFile;
    protected String name;
    protected boolean havePhoto;
    protected boolean clear;


    public String getBioText() {
        return bioText;
    }

    public FormFile getPhotoFile() {
        return photoFile;
    }

    public void setBioText(String bioText) {
        this.bioText = bioText;
    }

    public void setPhotoFile(FormFile photoFile) {
        this.photoFile = photoFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHavePhoto() {
        return havePhoto;
    }

    public void setHavePhoto(boolean havePhoto) {
        this.havePhoto = havePhoto;
    }

    public boolean isClear() {
        return clear;
    }
    public void setClear(boolean clear) {
        this.clear = clear;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        setBioText(null);
        setPhotoFile(null);
        setName(null);
        setHavePhoto(false);
      }

    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {

        ActionErrors errors = super.validate(actionMapping, httpServletRequest);


        long sizeLimit = 32768;
        FormFile formFile = getPhotoFile();
        if (formFile != null) {
            if (formFile.getFileSize() > sizeLimit) {
                ActionError error = new ActionError(
                    "error.registration.imageSizeLimit", "32");

                errors.add(null, error);
            }
        }

        if (errors.isEmpty()) {
            return null;

        }
        else {
            return errors;
        }

    }

}