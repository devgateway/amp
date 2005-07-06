/*
 *   MsgTag.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 *   Created: Apr 2, 2004
 *   CVS-ID: $Id: MsgTag.java,v 1.1 2005-07-06 10:34:18 rahul Exp $
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

package org.digijava.module.translation.taglib;

import javax.servlet.jsp.tagext.BodyContent;
import org.apache.log4j.Logger;
import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.http.HttpServletRequest;

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
    private boolean bodySkip = false;

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

    public int doStartTag() {
        logger.debug("Action 1");
        return super.doStartTag();
    }

    /**
     * Process the start tag.
     * @exception JspException if a JSP exception has occurred
     */
    public int doAfterBody() {

        BodyContent body = getBodyContent();

        logger.debug("Action 2");
        if (body == null) {
            logger.debug("Action 3");
            bodySkip = true;
            return SKIP_BODY;
        }

        if( body.getString().trim().length() <= 0 ) {
            logger.debug("Action 4");
            HttpServletRequest request = (HttpServletRequest) pageContext.
                getRequest();
            try {
                JspWriter out = pageContext.getOut();
                out.write("<font color=\"red\">msg body must be not null</font>");
            }
            catch (IOException e) {
                logger.warn("msg tag failed to render", e);
            }
            return SKIP_BODY;
        }
        logger.debug("Action 5");

        setKey(body.getString().trim());
        setBodyText(defaultValue);

        // Skip the tag and continue processing this page
        return super.doAfterBody();
    }

    /**
     *
     * @param body
     * @return
     */
    private String removeDummySymbols(String body) {

        body = body.replaceAll("\\\r", "");
        body = body.replaceAll("\\\n", "");
        body = body.replaceAll("\\\t", "");
        body = body.replaceAll("\\\f", "");
        body = body.replaceAll("\\\"", "");
        body = body.replaceAll("\\\b", "");
        body = body.replaceAll("\\\'", "");
        body = body.replaceAll("\\ ", "");

        return body;
    }


    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();

    }

}