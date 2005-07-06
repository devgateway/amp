/*
 *   Site.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 *   Created: Jul 2, 2003
 *	 CVS-ID: $Id: Site.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.digijava.kernel.Entity;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.entity.ModuleInstance;
import java.io.Serializable;

public class Site
    extends Entity implements Serializable{

    public static final String globalCountryIso = "global";
    public static final String globalCountryName = "global";

    public final static int MAX_PRIORITY = 3;
    public final static String[] defaultGroups = {
        "Administrators", "Members", "Translators"};

    private String siteId;
    private String description;
    private String metaDescription;
    private String metaKeywords;
    private String mission;
    private boolean active;
    private boolean secure;
    private boolean open;
    private int priority;
    private HashSet applInstances;
    private Set groups;
    private Locale defaultLanguage;
    private Set siteDomains;
    private java.util.Set userLanguages;
    private java.util.Set countries;
    private java.util.Set translationLanguages;
    private Set moduleInstances;
    private String folder;
    private Long parentId;
    private boolean inheritSecurity;
    private boolean invisible;
    private Boolean sendAlertsToAdmin;
    private ModuleInstance defaultInstance;

    /**
     * Default constructor, needed for Hibernate
     */
    public Site() {
    }

    public Site(String name, String siteId) {
        super(name);
        this.siteId = siteId;
    }

    public String getSiteId() {
        return siteId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean getOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public HashSet getApplInstances() {
        return applInstances;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public java.util.Set getTranslationLanguages() {
        return translationLanguages;
    }

    public void setTranslationLanguages(java.util.Set translationLanguages) {
        this.translationLanguages = translationLanguages;
    }

    public Locale getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(Locale defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public java.util.Set getUserLanguages() {
        return userLanguages;
    }

    public void setUserLanguages(java.util.Set userLanguages) {
        this.userLanguages = userLanguages;
    }

    public Set getGroups() {
        return groups;
    }

    public void setGroups(Set groups) {
        this.groups = groups;
    }

    public Set getSiteDomains() {
        return siteDomains;
    }

    public void setSiteDomains(Set siteDomains) {
        this.siteDomains = siteDomains;
    }

    public Set getModuleInstances() {
        return moduleInstances;
    }

    public void setModuleInstances(Set moduleInstances) {
        this.moduleInstances = moduleInstances;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public boolean isInheritSecurity() {
        return inheritSecurity;
    }

    public void setInheritSecurity(boolean inheritSecurity) {
        this.inheritSecurity = inheritSecurity;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    /**
     * @return
     */
    public java.util.Set getCountries() {
        return countries;
    }

    /**
     * @param set
     */
    public void setCountries(java.util.Set set) {
        countries = set;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public Boolean getSendAlertsToAdmin() {
        return sendAlertsToAdmin;
    }

    public void setSendAlertsToAdmin(Boolean sendAlertsToAdmin) {
        this.sendAlertsToAdmin = sendAlertsToAdmin;
    }

    public ModuleInstance getDefaultInstance() {
        return defaultInstance;
    }

    public void setDefaultInstance(ModuleInstance defaultInstance) {
        this.defaultInstance = defaultInstance;
    }

}