/*
*   ContentAlert.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: ContentAlert.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

import org.digijava.kernel.user.User;
import org.digijava.kernel.request.Site;
import java.sql.Clob;
import java.util.Set;
import java.io.Serializable;

public class ContentAlert
    implements Serializable {

    private Long value;
    private String name;

    public ContentAlert() {
    }

    public ContentAlert(Long value) {
        this.value = value;
    }

    public ContentAlert(Long value,String name) {
        this.value = value;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public Long getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Long value) {
        this.value = value;
    }

}