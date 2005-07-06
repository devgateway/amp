/*
 *   SecondaryPage.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Apr 14, 2004
 * 	 CVS-ID: $Id: SecondaryPage.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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
package org.digijava.kernel.siteconfig;

public class SecondaryPage {
    private String name;
    private String page;
    private String title;
    private String teaser;

    public SecondaryPage() {
        name = page = title = null;
    }

    public String toString() {
        return "<page name=\"" + name + "\" page=\"" + page +
            "\" title=\"" + title + "\" teaser=\"" + teaser + "\"/>";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }
}