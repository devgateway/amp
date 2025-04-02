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

package org.digijava.kernel.translator.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;

/**
 *
 * @author shamanth.murthy@mphasis.com
 * @version
 */

public final class TranslatorForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    public String strData = "";
    public String strKey = "";
    public String strMode = "";
    public String strLocale = "";
    public String strType = "";
    public String strMessageEn = "";
    public String strDestLocale = "";

    /**
     * Default Constructor
     *
     */

    public TranslatorForm() {
        //CHANGE HERE IF NEEDED//

    }

    public String getDestLocale() {

        return this.strDestLocale;
    }

    public void setDestLocale(String strDestLocale) {

        this.strDestLocale = strDestLocale;
    }

    public String getText() {

        return this.strData;
    }

    public void setText(String strTranslatedText) {

        this.strData = strTranslatedText;
    }

    public String getMessageEn() {

        return this.strMessageEn;
    }

    public void setMessageEn(String strMessageEn) {

        this.strMessageEn = strMessageEn;
    }


    public String getLocale() {

        return this.strLocale;
    }

    public void setLocale(String strLocale) {

        this.strLocale = strLocale;
    }

    public String getMode() {

        return this.strMode;
    }

    public void setMode(String strMode) {

        this.strMode = strMode;
    }

    public String getType() {

            return this.strType;
    }

    public void setType(String strType) {

            this.strType = strType;
    }
    public String getKey() {

        return this.strKey;
    }

    public void setKey(String strKey) {

        this.strKey = strKey;
    }

    /*
     *
     */

    /*This Collection holds all the URL information,
     * which will be rendered by the JSP
     */

    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */

    public void reset(ActionMapping mapping, HttpServletRequest request) {

        this.strData = "";

    }

    /**
     *
     *
     */

    public ActionErrors validate(
        ActionMapping mapping,
        HttpServletRequest request) {

        //      //System.out.println("into Validate");

        ActionErrors errors = new ActionErrors();

        if (strData.equals("")) {

            //            //System.out.println("Errors Detected");
            errors.add(
                "Translated text ",
                new ActionMessage("error.username.required"));
        }

        return errors;

        //return null;
    }

}
