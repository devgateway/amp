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

import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Entity;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.Group;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.PossibleValueId;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "DG_SITE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("null")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Site
extends Entity implements Serializable {


    @Id
    @Column(name = "ID")
    @Interchangeable(fieldTitle = "Id")
    @PossibleValueId
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dg_site_seq")
    @SequenceGenerator(name = "dg_site_seq", sequenceName = "dg_site_seq", allocationSize = 1)
    private Long id;

    @Override
    public String getName() {
        return name;
    }

    @Column(name = "NAME")
    private String name;

    @Column(name = "INHERIT_SECURITY")
    private boolean inheritSecurity;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "CREATION_IP")
    private String creationIP;

    @Column(name = "LAST_MODIFIED")
    private Date lastModified;

    @Column(name = "MODIFYING_IP")
    private String modifyingIP;

    @Column(name = "PARENT_ID")
    private Long parentId;

    @Column(name = "SITE_ID")
    private String siteId;

    @Column(name = "PRIVATE_P")
    private boolean secure;

    @Column(name = "METADESCRIPTION")
    private String metaDescription;

    @Column(name = "METAKEYWORDS")
    private String metaKeywords;

    @Column(name = "PRIORITY")
    private int priority;

    @Column(name = "MISSION")
    private String mission;

    @Column(name = "FOLDER")
    private String folder;

    @Column(name = "INVISIBLE")
    private boolean invisible;

    @Column(name = "ALERTS_TO_ADMIN")
    private Boolean sendAlertsToAdmin;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "DG_SITE_TRANS_LANG_MAP",
            joinColumns = @JoinColumn(name = "SITE_ID"),
            inverseJoinColumns = @JoinColumn(name = "CODE"))
    private Set<Locale> translationLanguages = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "DG_SITE_USER_LANG_MAP",
            joinColumns = @JoinColumn(name = "SITE_ID"),
            inverseJoinColumns = @JoinColumn(name = "CODE"))
    private Set<Locale> userLanguages = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "DEFAULT_LANGUAGE")
    private Locale defaultLanguage;

    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<SiteDomain> siteDomains = new HashSet<>();

    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ModuleInstance> moduleInstances = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "DG_SITE_COUNTRY_MAP",
            joinColumns = @JoinColumn(name = "SITE_ID"),
            inverseJoinColumns = @JoinColumn(name = "ISO"))
    private Set<Country> countries = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "DEFAULT_INSTANCE")
    private ModuleInstance defaultInstance;

    public static final String globalCountryIso = "global";
    public static final String globalCountryName = "global";

    public final static int MAX_PRIORITY = 3;
    public final static String[] defaultGroups = {
        "Administrators", "Members", "Translators"};

    @Transient
    private String description;
    @Transient
    private boolean active;
    @Transient
    private boolean open;
    @Transient
    private HashSet applInstances;

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
