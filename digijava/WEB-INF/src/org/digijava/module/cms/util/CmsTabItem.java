/*
 *   CmsTabItem.java
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
package org.digijava.module.cms.util;

public class CmsTabItem {
  String caprionKey;
  String navigationParam;
  boolean active;

  public CmsTabItem(String caprionKey, String navigationParam) {
    this.caprionKey = caprionKey;
    this.navigationParam = navigationParam;
  }

  public String getCaprionKey() {
    return caprionKey;
  }
  public void setCaprionKey(String caprionKey) {
    this.caprionKey = caprionKey;
  }
  public String getNavigationParam() {
    return navigationParam;
  }
  public void setNavigationParam(String navigationParam) {
    this.navigationParam = navigationParam;
  }
  public boolean isActive() {
    return active;
  }
  public void setActive(boolean active) {
    this.active = active;
  }
}