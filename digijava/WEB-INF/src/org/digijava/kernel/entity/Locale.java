/*
 *   Locale.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 *   Created: Jul 2, 2003
 *	 CVS-ID: $Id: Locale.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

import java.util.HashMap;
import java.util.Set;
import java.io.Serializable;

public class Locale implements Serializable {

    private String name;
    private String code;
    private boolean available;
    private String messageLangKey;


    public Locale() {
    }

    public Locale(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getMessageLangKey() {
        return messageLangKey;
    }

    public void setMessageLangKey(String messageLangKey) {
        this.messageLangKey = messageLangKey;
    }

}