/*
 *   CMSAdminForm.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: May 13,2004
 * 	 CVS-ID: $Id$
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

package org.digijava.module.cms.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.module.cms.dbentity.CMS;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class CMSAdminForm
    extends ActionForm {

  public static int MODE_EDIT = 1;
  public static int MODE_NEW = 2;

  private String moduleName;
  private String instanceName;
  private String siteName;

  private String approve;
  private String reject;
  private String revoke;

  private boolean moderated;
  private boolean privateMode;

  private boolean sendApproveMsg;
  private boolean sendRejectMsg;
  private boolean sendRevokeMsg;

  public String getInstanceName() {
    return instanceName;
  }

  public void setInstanceName(String instanceName) {
    this.instanceName = instanceName;
  }

  public String getModuleName() {
    return moduleName;
  }

  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }

  public String getSiteName() {
    return siteName;
  }

  public void setSiteName(String siteName) {
    this.siteName = siteName;
  }

  public void reset(ActionMapping mapping, HttpServletRequest request) {
    moderated = false;
    privateMode = false;

    sendApproveMsg = false;
    sendRejectMsg = false;
    sendRevokeMsg = false;
  }

  public String getApprove() {
    return approve;
  }

  public void setApprove(String approve) {
    this.approve = approve;
  }

  public int getMODE_EDIT() {
    return MODE_EDIT;
  }

  public void setMODE_EDIT(int MODE_EDIT) {
    this.MODE_EDIT = MODE_EDIT;
  }

  public int getMODE_NEW() {
    return MODE_NEW;
  }

  public void setMODE_NEW(int MODE_NEW) {
    this.MODE_NEW = MODE_NEW;
  }

  public String getRevoke() {
    return revoke;
  }

  public void setRevoke(String revoke) {
    this.revoke = revoke;
  }

  public boolean isModerated() {
    return moderated;
  }

  public void setModerated(boolean moderated) {
    this.moderated = moderated;
  }

  public boolean isPrivateMode() {
    return privateMode;
  }

  public void setPrivateMode(boolean privateMode) {
    this.privateMode = privateMode;
  }

  public boolean isSendApproveMsg() {
    return sendApproveMsg;
  }

  public void setSendApproveMsg(boolean sendApproveMsg) {
    this.sendApproveMsg = sendApproveMsg;
  }

  public boolean isSendRevokeMsg() {
    return sendRevokeMsg;
  }

  public void setSendRevokeMsg(boolean sendRevokeMsg) {
    this.sendRevokeMsg = sendRevokeMsg;
  }

  public String getReject() {
    return reject;
  }

  public void setReject(String reject) {
    this.reject = reject;
  }

  public boolean isSendRejectMsg() {
    return sendRejectMsg;
  }

  public void setSendRejectMsg(boolean sendRejectMsg) {
    this.sendRejectMsg = sendRejectMsg;
  }

}