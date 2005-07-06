/*
*   ItemBeanInfo.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: ItemBeanInfo.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

package org.digijava.kernel.entity;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ItemBeanInfo {
    private String info;
    private String value;
    public ItemBeanInfo(String Info, String Value) {
        this.info = Info;
        this.value = Value;
    }

    public String getInfo() {
        return info;
    }

    public String getValue() {
        return value;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setValue(String Value) {
        this.value = Value;
    }

}