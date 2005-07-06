/*
 *   UserTag.java
 * 	 @Author Irakli Nadareishvili inadareishvili@worldbank.org
 * 	 Created: Sept 27, 2003
 * 	 CVS-ID: $Id: UserTag.java,v 1.1 2005-07-06 10:34:24 rahul Exp $
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
package org.digijava.kernel.taglib.html;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.admin.exception.AdminException;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.kernel.user.UserInfo;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.util.RequestUtils;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UserTag
    extends TagSupport {

    final static String PROPERTY_NAME = "name";
    final static String PROPERTY_URL = "url";
    final static String PROPERTY_FIRSTNAMES = "firstnames";
    final static String PROPERTY_LASTNAME = "lastname";
    final static String PROPERTY_EMAIL = "email";

    public String property = PROPERTY_NAME;
    public String userId = null;

    private static Logger logger = Logger.getLogger(UserTag.class);


    /**
     * Process the start of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String content = null;
        String userName = null;
        String firstNames = null;
        String lastName = null;
        String email = null;
        Long realUserId = null;


        if( userId != null && userId.trim().length() > 0 ) {
                try {
                    realUserId = new Long(userId);
                    UserInfo userInfo = DgUtil.getUserInfo(realUserId);
                    userName = userInfo.getFirstNames() + " " + userInfo.getLastName();
                    firstNames = userInfo.getFirstNames();
                    lastName = userInfo.getLastName();
                    email = userInfo.getEmail();
                }
                catch (NumberFormatException ex) {
                    content = ex.getMessage();
                }
                catch (DgException ex) {
                    content = ex.getMessage();
                }
        } else {
            User user = RequestUtils.getUser(request);
            realUserId = user.getId();
            userName = user.getFirstNames() + " " + user.getLastName();
            firstNames = user.getFirstNames();
            lastName = user.getLastName();
            email = user.getEmail();
        }

        if( content == null ) {
            // set user firstname + lastname
            if (property.equalsIgnoreCase(PROPERTY_NAME)) {
                content = userName;
            } else if (property.equalsIgnoreCase(PROPERTY_FIRSTNAMES)) {
                content = firstNames;
            } else if (property.equalsIgnoreCase(PROPERTY_LASTNAME)) {
                content = lastName;
            } else if (property.equalsIgnoreCase(PROPERTY_EMAIL)) {
                content = email;
            } else if (property.equalsIgnoreCase(PROPERTY_URL)) {
                content = DgUtil.getCurrRootUrl(request) +
                    "/um/user/showUserProfile.do?activeUserId=" + realUserId;
            }
        }

        JspWriter out = pageContext.getOut();
        try {
            if( content != null )
                out.write(content);
        }
        catch (IOException e) {
            logger.warn(" UserTag failed to render",e);
        }

        return EVAL_BODY_INCLUDE;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}