/*
 *   Action.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 3, 2003
 * 	 CVS-ID: $Id: Action.java,v 1.1 2005-07-06 10:34:20 rahul Exp $
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
package org.digijava.kernel.config.moduleconfig;

import java.util.List;
import java.util.ArrayList;

public class Action {
    private String pattern;
    private String questionMarkPosition;
    private boolean hideAfterQMark;
    private String value;
    private List params;
    boolean loginRequired;
    private String identityType;
    private String identityPattern;

    public Action() {
        params = new ArrayList();
        loginRequired = true;
        hideAfterQMark = false;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isLoginRequired() {
        return loginRequired;
    }

    public void setLoginRequired(boolean loginRequired) {
        this.loginRequired = loginRequired;
    }

    public void setLoginRequired(String loginRequired) {
        this.loginRequired = Boolean.getBoolean(loginRequired);
    }

    public void addParam(Param param) {
        params.add(param);
    }

    public List getParams() {
        return params;
    }

    public String getQuestionMarkPosition() {
        return questionMarkPosition;
    }

    public void setQuestionMarkPosition(String questionMarkPosition) {
        this.questionMarkPosition = questionMarkPosition;
    }

    public String getIdentityPattern() {
        return identityPattern;
    }

    public void setIdentityPattern(String identityPattern) {
        this.identityPattern = identityPattern;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public boolean isHideAfterQMark() {
        return hideAfterQMark;
    }

    public void setHideAfterQMark(boolean hideAfterQMark) {
        this.hideAfterQMark = hideAfterQMark;
    }

}