/*
 *   SiteForm.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 1, 2003
 * 	 CVS-ID: $Id: SiteForm.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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
package org.digijava.module.admin.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.SiteConfigUtils;
import java.util.List;

public class SiteForm
    extends ValidatorForm {

  public static class SiteDomainInfo
      implements Comparable {
    private String domain;
    private String path;
    private String langCode;
    private Long id;
    private int index;

    public SiteDomainInfo() {}

    public String getDomain() {
      return domain;
    }

    public void setDomain(String domain) {
      this.domain = domain;
    }

    public String getPath() {
      return path;
    }

    public void setPath(String path) {
      this.path = path;
    }

    public String getLangCode() {
      return langCode;
    }

    public void setLangCode(String langCode) {
      this.langCode = langCode;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public int getIndex() {
      return index;
    }

    public void setIndex(int index) {
      this.index = index;
    }

    public int compareTo(Object o) {
      SiteDomainInfo info = (SiteDomainInfo) o;

      String domain1 = this.domain == null ? "" : this.domain;
      int result = domain1.compareTo(info.domain);
      if (result == 0) {
        String path1 = this.path == null ? "" : this.path;
        return path1.compareTo(info.path);
      }
      else {
        return result;
      }
    }
  }

  public static class SiteInfo {

    private Site site;
    private String viewSite;
    private String admin;

    public Site getSite() {
      return site;
    }

    public void setSite(Site site) {
      this.site = site;
    }

    public String getViewSite() {
      return viewSite;
    }

    public void setViewSite(String viewSite) {
      this.viewSite = viewSite;
    }

    public String getAdmin() {
      return admin;
    }

    public void setAdmin(String admin) {
      this.admin = admin;
    }

  }

  private Long id;
  private String siteName;
  private String siteKey;
  private boolean secure;
  private boolean invisible;
  private boolean inheritSecurity;
  private int priority;
  private String metaDescription;
  private String metaKeywords;
  private String mission;
  private Collection languages;
  private String[] userLanguages;
  private String[] transLanguages;
  private String defaultLanguage;
  private Collection templates;
  private String template;
  private Collection sites;
  private String parentSiteName;
  private boolean topLevel;
  private static final Collection priorities;
  private int defDomain;
  private java.util.ArrayList siteDomains;
  private Collection children;
  private String targetAction;
  private Long parentId;
  private String folderName;

  private String recieveEmailAlerts;

  private String country;
  private List countries;

  static {
    priorities = new ArrayList();
    for (int i = 0; i <= Site.MAX_PRIORITY; i++) {
      priorities.add(new Integer(i));
    }
  }

  public void reset(ActionMapping mapping, HttpServletRequest request) {
    transLanguages = userLanguages = null;
    template = SiteConfigUtils.BLANK_TEMPLATE_NAME;
    siteDomains = new ArrayList();
    parentId = null;
    parentSiteName = null;
    siteName = null;
    siteKey = null;

    recieveEmailAlerts = "2";
  }

  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {
    // Validate using Struts validator plugin
    ActionErrors errors = super.validate(actionMapping, httpServletRequest);

    if (errors == null) {
      errors = new ActionErrors();
    }
    if ( (siteDomains == null) || (siteDomains.size() == 0)) {
      errors.add(null,
                 new ActionError("error.admin.siteDomainsEmpty"));
    }

    Iterator iter = siteDomains.iterator();
    TreeSet definedDomains = new TreeSet();
    HashSet domainLanguages = new HashSet();
    boolean hasDefault = false;
    while (iter.hasNext()) {
      SiteDomainInfo item = (SiteDomainInfo) iter.next();
      if ( (item.getDomain() == null) ||
          (item.getDomain().trim().length() == 0)) {
        errors.add(null,
                   new ActionError("error.admin.domainEmpty"));
        break;
      }
      if ( (item.getPath() != null) &&
          (item.getPath().trim().length() != 0)) {
        if (!item.getPath().startsWith("/")) {
          Object[] params = {
              item.getPath()};
          errors.add(null,
                     new ActionError("error.admin.domainPathStart",
                                     params));
        }
        if (item.getPath().endsWith("/")) {
          Object[] params = {
              item.getPath()};
          errors.add(null,
                     new ActionError("error.admin.domainPathEnd",
                                     params));
        }
      }
      if (defDomain == item.getIndex()) {
        hasDefault = true;
      }
      if (definedDomains.contains(item)) {
        Object[] params = {
            item.getDomain(),
            item.getPath()};
        errors.add(null,
                   new ActionError("error.admin.siteDomainTwise",
                                   params));
      }
      else {
        definedDomains.add(item);
      }
      if ( (item.getLangCode() != null) &&
          (item.getLangCode().trim().length() != 0)) {
        if (domainLanguages.contains(item.getLangCode())) {
          errors.add(null,
                     new ActionError(
              "error.admin.oneDomainPerLanguage"));
        }
        else {
          domainLanguages.add(item.getLangCode());
        }
      }
    }

    if (!hasDefault) {
      errors.add(null,
                 new ActionError("error.admin.defaultSiteDomainMissing"));
    }

    /*
         if ( (contentLanguages == null) || (contentLanguages.length == 0)) {
        errors.add(null,
                   new ActionError("error.admin.contentLanguagesEmpty"));
             }
         if ( (navigationLanguages == null) || (navigationLanguages.length == 0)) {
        errors.add(null,
         new ActionError("error.admin.navigationLanguagesEmpty"));
             }
         if ( (contentLanguages != null) && (navigationLanguages != null)) {
        HashSet cl = new HashSet();
        for (int i = 0; i < contentLanguages.length; i++) {
            cl.add(contentLanguages[i]);
        }
        boolean defaultFound = false;
        for (int i = 0; i < navigationLanguages.length; i++) {
            if (!defaultFound) {
                if (defaultLanguage.equals(navigationLanguages[i])) {
                    defaultFound = true;
                }
            }
            if (!cl.contains(navigationLanguages[i])) {
                Object[] param = {
                    navigationLanguages[i]};
                errors.add(null,
                           new ActionError(
                    "error.admin.navigationLanguageMustBeContent",
                    param));
            }
        }
        if (!defaultFound) {
            Object[] param = {
                defaultLanguage};
            errors.add(null,
                       new ActionError(
                "error.admin.defaultLanguageNotInList",
                param));
        }
             }
     */

    return errors.isEmpty() ? null : errors;
  }

  public String getDefaultLanguage() {
    return defaultLanguage;
  }

  public void setDefaultLanguage(String defaultLanguage) {
    this.defaultLanguage = defaultLanguage;
  }

  public Collection getLanguages() {
    return languages;
  }

  public void setLanguages(Collection languages) {
    this.languages = languages;
  }

  public boolean isSecure() {
    return secure;
  }

  public void setSecure(boolean secure) {
    this.secure = secure;
  }

  public boolean isInvisible() {
    return invisible;
  }

  public void setInvisible(boolean invisible) {
    this.invisible = invisible;
  }

  public String getSiteKey() {
    return siteKey;
  }

  public void setSiteKey(String siteKey) {
    this.siteKey = siteKey;
  }

  public String getSiteName() {
    return siteName;
  }

  public void setSiteName(String siteName) {
    this.siteName = siteName;
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public Collection getTemplates() {
    return templates;
  }

  public void setTemplates(Collection templates) {
    this.templates = templates;
  }

  public Collection getSites() {
    return sites;
  }

  public void setSites(Collection sites) {
    this.sites = sites;
  }

  public java.util.ArrayList getSiteDomains() {
    return siteDomains;
  }

  public void setSiteDomains(java.util.ArrayList siteDomains) {
    this.siteDomains = siteDomains;
  }

  public SiteDomainInfo getSiteDomain(int index) {
    SiteDomainInfo info = null;
    int actualSize = siteDomains.size();
    if (index >= actualSize) {
      // Expand the list
      for (int i = 0; i <= index - actualSize; i++) {
        siteDomains.add(new SiteDomainInfo());
      }
    }

    return (SiteDomainInfo) siteDomains.get(index);
  }

  public String[] getUserLanguages() {
    return userLanguages;
  }

  public void setUserLanguages(String[] userLanguages) {
    this.userLanguages = userLanguages;
  }

  public String getParentSiteName() {
    return parentSiteName;
  }

  public void setParentSiteName(String parentSiteName) {
    this.parentSiteName = parentSiteName;
  }

  public String[] getTransLanguages() {
    return transLanguages;
  }

  public void setTransLanguages(String[] transLanguages) {
    this.transLanguages = transLanguages;
  }

  public boolean isTopLevel() {
    return topLevel;
  }

  public void setTopLevel(boolean topLevel) {
    this.topLevel = topLevel;
  }

  public boolean isInheritSecurity() {
    return inheritSecurity;
  }

  public void setInheritSecurity(boolean inheritSecurity) {
    this.inheritSecurity = inheritSecurity;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
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

  public Collection getPriorities() {
    return priorities;
  }

  public int getDefDomain() {
    return defDomain;
  }

  public void setDefDomain(int defDomain) {
    this.defDomain = defDomain;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  public Collection getChildren() {
    return children;
  }

  public void setChildren(Collection children) {
    this.children = children;
  }

  public String getTargetAction() {
    return targetAction;
  }

  public void setTargetAction(String targetAction) {
    this.targetAction = targetAction;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMission() {
    return mission;
  }

  public void setMission(String mission) {
    this.mission = mission;
  }

  public String getFolderName() {
    return folderName;
  }

  public void setFolderName(String folderName) {
    this.folderName = folderName;
  }

  public String getRecieveEmailAlerts() {
    return recieveEmailAlerts;
  }

  public void setRecieveEmailAlerts(String recieveEmailAlerts) {
    this.recieveEmailAlerts = recieveEmailAlerts;
  }

  public List getCountries() {
    return countries;
  }

  public void setCountries(List countries) {
    this.countries = countries;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

}