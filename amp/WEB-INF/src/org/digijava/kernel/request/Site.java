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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.digijava.kernel.entity.Entity;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;

public class Site
extends Entity implements Serializable {

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
    private Set<SiteDomain> siteDomains;
    private java.util.Set<Locale> userLanguages;
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

     public Set<SiteDomain> getSiteDomains() {
         return siteDomains;
     }

     public void setSiteDomains(Set<SiteDomain> siteDomains) {
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

     /**
      * returns the id a site or zero if site is null (see TranslationCall.getSiteId() comment): For global translations, this method must return '0'
      * @param site
      * @return
      */
     public static Long getIdOf(Site site)
     {
         if (site == null)
             return 0L;
         return site.getId();
     }

    /**
     * returns the Locale from userLanguages set or default language
     * @param locale The locale code to search.
     * @return
     */
    public Locale getLocale(String locale) {
        return this.userLanguages.stream()
                .filter(x -> locale.equalsIgnoreCase(x.getCode()))
                .findAny()
                .orElse(this.defaultLanguage);
    }
    
}
