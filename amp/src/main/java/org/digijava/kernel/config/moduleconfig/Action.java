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

package org.digijava.kernel.config.moduleconfig;

import java.util.ArrayList;
import java.util.List;

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
