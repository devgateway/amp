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

package org.digijava.module.um.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
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
                ActionMessage error = new ActionMessage(
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
