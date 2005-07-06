/*
 *   SearchUserForm.java
 * 	 @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Sep 18, 2003
 * 	 CVS-ID: $Id: SearchUserForm.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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

package org.digijava.module.admin.form;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

//ActionForm for user Search

public class SearchUserForm
    extends ActionForm {

    private String searchUserInfo;
    private List userList;
    private String targetAction;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        searchUserInfo = null;
        userList = null;
        targetAction = null;
    }

    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {
        return null; // Validation was moved to SearchUser.java
    }

    public String[] getSelectedUsers() {
        return null;
    }

    public String getSearchUserInfo() {
        return searchUserInfo;
    }

    public void setSearchUserInfo(String searchUserInfo) {
        this.searchUserInfo = searchUserInfo;
    }

    public List getUserList() {
        return userList;
    }

    public void setUserList(List userList) {
        this.userList = userList;
    }

    public String getTargetAction() {
        return targetAction;
    }

    public void setTargetAction(String targetAction) {
        this.targetAction = targetAction;
    }

}