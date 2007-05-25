/*
 *   CMS.java
 *   @Author George Kvizhinadze gio@digijava.org
 *   Created: May 7, 2004
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

package org.digijava.module.cms.dbentity;

import java.util.List;
import java.util.ArrayList;

public class CMS {
  private long id;
  private String siteId;
  private String instanceId;

  private List topLevelCategories;

  private boolean moderated;
  private boolean privateMode;

  private boolean sendApproveMsg;
  private String approveMsg;

  private boolean sendRejectMsg;
  private String rejectMsg;

  private boolean sendRevokeMsg;
  private String revokeMsg;

  public CMS() {
    this.topLevelCategories = new ArrayList();
  }

  public CMS(String siteId, String instanceId) {
    this.siteId = siteId;
    this.instanceId = instanceId;

    this.topLevelCategories = new ArrayList();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public List getTopLevelCategories() {
    return topLevelCategories;
  }

  public void setTopLevelCategories(List topLevelCategories) {
    this.topLevelCategories = topLevelCategories;
  }

  public String getApproveMsg() {
    return approveMsg;
  }

  public void setApproveMsg(String approveMsg) {
    this.approveMsg = approveMsg;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
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

  public String getRevokeMsg() {
    return revokeMsg;
  }

  public void setRevokeMsg(String revokeMsg) {
    this.revokeMsg = revokeMsg;
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

  public String getSiteId() {
    return siteId;
  }

  public void setSiteId(String siteId) {
    this.siteId = siteId;
  }

  public String getRejectMsg() {
    return rejectMsg;
  }

  public void setRejectMsg(String rejectMsg) {
    this.rejectMsg = rejectMsg;
  }

  public boolean isSendRejectMsg() {
    return sendRejectMsg;
  }

  public void setSendRejectMsg(boolean sendRejectMsg) {
    this.sendRejectMsg = sendRejectMsg;
  }
}