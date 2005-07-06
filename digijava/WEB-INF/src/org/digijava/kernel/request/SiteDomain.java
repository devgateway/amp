/*
 *   SiteDomain.java
 * 	 @Author Irakli Nadareishvili irakli@digijava.org
 * 	 Created: Jul 4, 2003
 * 	 CVS-ID: $Id: SiteDomain.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

package org.digijava.kernel.request;

import org.digijava.kernel.entity.Locale;
import org.apache.log4j.Logger;
import org.digijava.kernel.util.SiteUtils;
import java.io.Serializable;

public class SiteDomain implements Serializable {
    private long siteDomainId;
    private Site site;

    //private String siteDomain;

    private String sitePath;
    private Locale language;
    private boolean defaultDomain;

    /**
     * <p>This is the hibernate-persisted domain value in the database.
     * The value that Digi uses, in runtime is siteDomain and it
     * is calculated as:<br/>
     * <code>siteDomain = prefix + siteDbDomain;</code><br/>
     * where "prefix" is defined in digi.xml's &lt;server-type&gt; tag.
     *
     * <p>Prefixes help independant installations on different physical
     * servers (different IPs) to use the same database hence the
     * same domain records, for sites. By putting prefix in digi.xml the
     * domain names that system uses runtime will change, so you can assign
     * prefixed domain names to a different server (say, development or staging),
     * but use the same database records.
     *
     */
    private String siteDbDomain;
    private static Logger logger = Logger.getLogger(SiteDomain.class);

    /**
     * Returnes prefixed domain instead of the one defined in
     * the database. Prefixed domain calculated as:
     * <code>siteDomain = prefix + siteDbDomain;</code><br/>
     * where "prefix" is defined in digi.xml's &lt;server-type&gt; tag.
     *
     * <p>Prefixes help independant installations on different physical
     * servers (different IPs) to use the same database hence the
     * same domain records, for sites. By putting prefix in digi.xml the
     * domain names that system uses runtime will change, so you can assign
     * prefixed domain names to a different server (say, development or staging),
     * but use the same database records.
     *
     */
    public String getSiteDomain() {
        return SiteUtils.prefixDomainName ( this.siteDbDomain );
    }

    /**
     * This method is deprecated (see comment for getSiteDomain).
     * It should never be used, normally. setSiteDbDomain should
     * be used instead.
     *
     * @param siteDomain
     * @deprecated
     *
     */
    public void setSiteDomain(String siteDomain) {
        setSiteDbDomain(siteDomain);
        //this.siteDomain = prefix + siteDomain;
    }

    public long getSiteDomainId() {
        return siteDomainId;
    }

    public void setSiteDomainId(long siteDomainId) {
        this.siteDomainId = siteDomainId;
    }

    public String getSitePath() {
        return sitePath;
    }

    public void setSitePath(String sitePath) {
        this.sitePath = sitePath;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }

    public boolean isDefaultDomain() {
        return defaultDomain;
    }

    public void setDefaultDomain(boolean defaultDomain) {
        this.defaultDomain = defaultDomain;
    }

    /**
     * This is the hibernate-persisted domain value in the database.
     * The value that Digi uses, in runtime is siteDomain and it
     * is calculated as:
     * <code>siteDomain = prefix + siteDbDomain;</code><br/>
     * where "prefix" is defined in digi.xml's &lt;server-type&gt; tag.
     *
     * <p>Prefixes help independant installations on different physical
     * servers (different IPs) to use the same database hence the
     * same domain records, for sites. By putting prefix in digi.xml the
     * domain names that system uses runtime will change, so you can assign
     * prefixed domain names to a different server (say, development or staging),
     * but use the same database records.
     *
     */
    public String getSiteDbDomain() {
        return siteDbDomain;
    }

    public void setSiteDbDomain(String siteDbDomain) {
        this.siteDbDomain = siteDbDomain;
    }

}