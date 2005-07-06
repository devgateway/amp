/*
 *   SiteDomainItem.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: May 9, 2004
 * 	 CVS-ID: $Id: SiteDomainItem.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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
package org.digijava.module.translation.util;

import org.digijava.kernel.util.SiteUtils;

public class SiteDomainItem {

    private String code;
    private String domain;
    private String path;

    public SiteDomainItem(String code, String domain, String path) {
        this.code = code;
        this.domain = SiteUtils.prefixDomainName(domain);
        this.path = path;
    }

    public int compareTo(Object o) {
        return this.code.compareTo( ( (SiteDomainItem) o).code);
    }

    public String getCode() {
        return code;
    }

    public String getDomain() {
        return domain;
    }

    public String getPath() {
        return path;
    }
}
