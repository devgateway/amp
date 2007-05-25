/*
*   ForumPaginationItem.java
*   @Author George Kvizhinadze gio@digijava.org
*   Created:
*   CVS-ID: $Id$
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

public class ForumPaginationItem {
    public static int FIRST_PAGE = 0;
    public static int CURRENT_PAGE = 1;
    public static int LAST_PAGE = 2;
    public static int GENERIC_PAGE = 3;
    public static int INTERVAL = 4;

    private int itemType;
    private int parameter;
    private String displayString;

    public ForumPaginationItem() {
    }

    public ForumPaginationItem(int parameter) {
        this.parameter = parameter;
    }

    public ForumPaginationItem(int parameter, String displayString) {
        this.parameter = parameter;
        this.displayString = displayString;
    }

    public ForumPaginationItem(int itemType,
                               int parameter,
                               String displayString) {
        this.itemType = itemType;
        this.parameter = parameter;
        this.displayString = displayString;
    }
    public String getDisplayString() {
        return displayString;
    }
    public void setDisplayString(String displayString) {
        this.displayString = displayString;
    }
    public int getItemType() {
        return itemType;
    }
    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
    public int getParameter() {
        return parameter;
    }
    public void setParameter(int parameter) {
        this.parameter = parameter;
    }

}