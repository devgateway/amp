/*
*   LocationTrailItem.java
*   @Author George Kvizhinadze gio@digijava.org
*   Created:
*   CVS-ID: $Id: LocationTrailItem.java,v 1.1 2005-07-06 10:34:19 rahul Exp $
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

package org.digijava.module.forum.util;

import java.util.Map;
import java.util.HashMap;

public class LocationTrailItem {

    public static int GENERIC_ITEM = 0;
    public static int TOP_ITEM = 1;
    public static int CURRENT_ITEM = 2;
    public static int DISABLED_ITEM = 3;

    private String paramName;
    private long paramValue;
    private String actionName;
    private String caption;
    private int itemType;

    public LocationTrailItem() {
    }
    public String getActionName() {
        return actionName;
    }
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
    public String getParamName() {
        return paramName;
    }
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
    public long getParamValue() {
        return paramValue;
    }
    public void setParamValue(long paramValue) {
        this.paramValue = paramValue;
    }
    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }
    public int getItemType() {
        return itemType;
    }
    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}