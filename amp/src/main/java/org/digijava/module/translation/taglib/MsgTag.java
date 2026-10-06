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

package org.digijava.module.translation.taglib;

import org.apache.log4j.Logger;

import javax.servlet.jsp.tagext.BodyContent;

/**
 * Custom tag that retrieves internationalized message
 * based on the Key provided. This tag uses methods in DgUtil to determine
 * the language in which the message must be displayed.
 * If the key does not have a translation in the requested language then
 * the message in site default language is shown, else English
 * language message is shown.
 * This Tag also appends an Edit,Translate or CreateTag to the message
 * if a Translator has logged in.
 */
public class MsgTag
    extends TrnTag {

    private static Logger logger = Logger.getLogger(MsgTag.class);

    private String defaultValue = null;

    public MsgTag() {
    }

    /**
     * Gets the translation default Message
     * @return
     */
    public String getDefault() {
        return (this.defaultValue);
    }

    /**
     * @param defaultValue
     */
    public void setDefault(String defaultValue) {
        this.defaultValue = defaultValue;
    }


    /**
     * Process the start tag.
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() {

        BodyContent body = getBodyContent();

        if(body==null || body.toString() == null || body.getString().trim().length() <= 0 ) {
            writeData("<font color=\"red\">msg tag body must be not null</font>");
            return EVAL_PAGE;
        }
        setKey(body.getString().trim());
        setBodyText(defaultValue);

        // Skip the tag and continue processing this page
        return super.doEndTag();
    }


}
