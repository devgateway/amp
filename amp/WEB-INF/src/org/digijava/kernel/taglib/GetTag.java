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

package org.digijava.kernel.taglib;

import org.digijava.kernel.entity.Message;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

public class GetTag extends BodyTagSupport {

    private static final long serialVersionUID = 1L;


    private String locale;
    private String key;
    private String type;

    /**
     *
     * @exception JspException if a JSP exception has occurred
     * @return
     */
    public int doAfterBody() {

        HttpServletRequest request =
            (HttpServletRequest) pageContext.getRequest();

        BodyContent body = getBodyContent();

        String output = "";

        if (getKey() != null && getType() != null) { //commented
//        if (getKey() != null) {

            Long siteId;

            if(this.getKey().trim().startsWith("cn") || this.getKey().trim().startsWith("ln")){

                siteId = 0L;


            }else{
                if (getType().equalsIgnoreCase("local")) {

                    siteId =
                        RequestUtils.getSiteDomain(request).getSite().getId();

                } else {
                    siteId =
                        DgUtil
                            .getRootSite(RequestUtils.getSiteDomain(request).getSite()).getId();

                }
            }

            Message msg =
                    new TranslatorWorker().getByKey(getKey(), getLocale(), siteId);

            if (msg != null) {
                if (msg.getMessage() != null) {
                    output = msg.getMessage();

                }else{
                    output="key:" + getKey();
                }
            }else{
                output="key:" + getKey();
            }
        }

        writeData(output, body);
        return SKIP_BODY;

    }

    /**
     * Writes given string to the browser
     * @param localizedMsg
     * @param body
     */
    private void writeData(String localizedMsg, BodyContent body) {

        try {

            JspWriter out = body.getEnclosingWriter();

            out.print(localizedMsg);

        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

    }

    /**
     * @return
     */
    public String getLocale() {
        return locale;
    }

    /**
     * @param locale
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }
    /**
     * @return
     */
    public String getType() {

        if (this.type == null) {

            type = "group";
        }
        return type;
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
}
