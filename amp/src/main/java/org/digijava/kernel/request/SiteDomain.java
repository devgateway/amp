/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.kernel.request;

import static java.util.Comparator.comparing;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.util.SiteUtils;

public class SiteDomain implements Serializable{

    public static final Comparator<SiteDomain> DEFAULTS_FIRST = comparing(SiteDomain::isDefaultDomain).reversed();

    private long siteDomainId;
    private Site site;

    //private String siteDomain;

    private String sitePath;
    private Locale language;
    private boolean defaultDomain;
    private Boolean enableSecurity;

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
        return SiteUtils.prefixDomainName(this.siteDbDomain);
    }

    public String toUrl() {
        String protocol = (enableSecurity != null && enableSecurity) ? "https" : "http";
        String path = sitePath == null ? "/" : sitePath;
        return String.format("%s://%s%s", protocol, getSiteDomain(), path);
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

    /**
     * Returns "enableSecurity" flag for the site domain. If null, domain can
     * be used both for secure and unsecure operations.
     * @return Boolean
     */
    public Boolean getEnableSecurity() {
        return enableSecurity;
    }

    public void setSiteDbDomain(String siteDbDomain) {
        this.siteDbDomain = siteDbDomain;
    }

    /**
     * Sets "enableSecurity" flag for the site domain. If null, domain can
     * be used both for secure and unsecure operations.
     * @param enableSecurity Boolean
     */
    public void setEnableSecurity(Boolean enableSecurity) {
        this.enableSecurity = enableSecurity;
    }

    /**
     * If domain is secure or "enableSecurity" flag is not set at all, returns
     * true. Otherwise returns false
     * @return boolean
     */
    public boolean isSecure() {
        return enableSecurity == null ? true : enableSecurity.booleanValue();
    }

    /**
     * If domain is unsecure or "enableSecurity" flag is not set at all, returns
     * true. Otherwise returns false
     * @return boolean
     */
    public boolean isUnsecure() {
        return enableSecurity == null ? true : enableSecurity.booleanValue();
    }

}
