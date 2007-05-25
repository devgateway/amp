/*
 *   CMSItemForm.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: May 06,2004
 * 	 CVS-ID: $Id$
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

package org.digijava.module.cms.form;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.user.User;
import org.digijava.module.common.action.PaginationForm;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class CMSContentItemForm
    extends PaginationForm {

  public static class CMSContentItemInfo {
    private Long id;

    private String title;
    private String description;
    private String url;
    private String fileName;

    private String language;
    private String languageKey;

    private String country;
    private String countryName;
    private String countryKey;
    private User authorUser;
    private Date creationDate;
    private boolean published;
    private boolean rejected;

    private boolean editable;

    public boolean isPublished() {
      return published;
    }

    public void setPublished(boolean published) {
      this.published = published;
    }

    public boolean isRejected() {
      return rejected;
    }

    public void setRejected(boolean rejected) {
      this.rejected = rejected;
    }

    public User getAuthorUser() {
      return this.authorUser;
    }

    public void setAuthorUser(User user) {
      this.authorUser = user;
    }

    public Date getCreationDate() {
      return this.creationDate;
    }

    public void setCreationDate(Date date) {
      this.creationDate = date;
    }
    public String getCountry() {
      return country;
    }

    public void setCountry(String country) {
      this.country = country;
    }

    public String getCountryKey() {
      return countryKey;
    }

    public void setCountryKey(String countryKey) {
      this.countryKey = countryKey;
    }

    public String getCountryName() {
      return countryName;
    }

    public void setCountryName(String countryName) {
      this.countryName = countryName;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getFileName() {
      return fileName;
    }

    public void setFileName(String fileName) {
      this.fileName = fileName;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getLanguage() {
      return language;
    }

    public void setLanguage(String language) {
      this.language = language;
    }

    public String getLanguageKey() {
      return languageKey;
    }

    public void setLanguageKey(String languageKey) {
      this.languageKey = languageKey;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }
    public boolean isEditable() {
      return editable;
    }
    public void setEditable(boolean editable) {
      this.editable = editable;
    }

  }

  //

  public static final int MODE_EDIT = 1;
  public static final int MODE_NEW = 2;

  public static String VIEW_MODE_ALL = "all";
  public static String VIEW_MODE_PUBLISHED = "published";
  public static String VIEW_MODE_PENDING = "pending";
  public static String VIEW_MODE_REJECTED = "rejected";

  private String viewMode;
  private String itemStatus;
  private List tabs;
  private int processingMode;
  private int itemsPerPage;

  private String[] checkboxArray;
  private String[] propertyArray;

  Long itemId;
  long categoryId;
  private String refPageUrl;

  private String title;
  private String description;
  private String url;

  private FormFile formFile;
  private byte[] file;
  private String fileName;
  private String contentType;

  private String language;
  private String languageKey;
  private List languages;

  private String country;
  private String countryKey;
  private String countryName;
  private List countries;

  CMSContentItemInfo itemPreview;
  private boolean preview;

  private boolean noReset;

  private List itemsList;

  private List categoryIdList;
  private List categoryNameList;

  private long parentCategoryId;

  private String moduleName;
  private String siteName;
  private String instanceName;
  //
  private boolean sendMessage;

  public void reset(ActionMapping mapping, HttpServletRequest request) {
    super.reset(mapping, request);
    itemPreview = null;
    preview = false;
    itemsList = null;
    sendMessage = false;
    itemsPerPage = 0;

    if (!noReset) {
      itemId = null;

      title = null;
      description = null;
      url = null;
      //
      contentType = null;
      fileName = null;
      file = null;

      languages = null;
      language = null;
      languageKey = null;

      countries = null;
      country = null;
      countryKey = null;
      countryName = null;

      processingMode = this.MODE_NEW;
    }
  }

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public CMSContentItemInfo getItemPreview() {
    return itemPreview;
  }

  public void setItemPreview(CMSContentItemInfo itemPreview) {
    this.itemPreview = itemPreview;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public byte[] getFile() {
    return file;
  }

  public void setFile(byte[] file) {
    this.file = file;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public FormFile getFormFile() {
    return formFile;
  }

  public void setFormFile(FormFile formFile) {
    this.formFile = formFile;
  }

  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {

    ActionErrors errors = new ActionErrors();
    if (title == null || title.trim().length() == 0) {
      errors.add(null,
                 new ActionError(
          "error.cms.itemTitleEmpty"));
    }
    if ( (url == null || url.trim().length() == 0 ||
          url.equals( (String) (httpServletRequest.getScheme() +
                                "://")))) {
      if (formFile != null && formFile.getFileSize() == 0) {
        if (file == null) {
          errors.add(null,
                     new ActionError(
              "error.cms.urlAndFileEmpty"));
        }
      }
    }
    if ( (url != null && url.trim().length() != 0 &&
          !url.equals( (String) (httpServletRequest.getScheme() +
                                 "://")))) {
      if (formFile != null && formFile.getFileSize() != 0 && file != null) {
        errors.add(null,
                   new ActionError(
            "error.cms.urlAndFileGiven"));
      }
    }

    if (categoryIdList == null || categoryIdList.size() == 0) {
      errors.add(null,
                 new ActionError(
          "error.cms.categoryEmpty"));
    }
    return errors.isEmpty() ? null : errors;
  }

  public List getCountries() {
    return countries;
  }

  public void setCountries(List countries) {
    this.countries = countries;
  }

  public List getLanguages() {
    return languages;
  }

  public void setLanguages(List languages) {
    this.languages = languages;
  }

  public boolean isPreview() {
    return preview;
  }

  public void setPreview(boolean preview) {
    this.preview = preview;
  }

  public boolean isNoReset() {
    return noReset;
  }

  public void setNoReset(boolean noReset) {
    this.noReset = noReset;
  }

  public List getItemsList() {
    return itemsList;
  }

  public void setItemsList(List itemsList) {
    this.itemsList = itemsList;
  }

  public int getProcessingMode() {
    return processingMode;
  }

  public void setProcessingMode(int processingMode) {
    this.processingMode = processingMode;
  }

  public String getCountryKey() {
    return countryKey;
  }

  public void setCountryKey(String countryKey) {
    this.countryKey = countryKey;
  }

  public String getLanguageKey() {
    return languageKey;
  }

  public void setLanguageKey(String languageKey) {
    this.languageKey = languageKey;
  }

  public long getParentCategoryId() {
    return parentCategoryId;
  }

  public void setParentCategoryId(long parentCategoryId) {
    this.parentCategoryId = parentCategoryId;
  }

  public List getCategoryIdList() {
    return categoryIdList;
  }

  public void setCategoryIdList(List categoryIdList) {
    this.categoryIdList = categoryIdList;
  }

  public List getCategoryNameList() {
    return categoryNameList;
  }

  public void setCategoryNameList(List categoryNameList) {
    this.categoryNameList = categoryNameList;
  }

  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  public List getTabs() {
    return tabs;
  }

  public void setTabs(List tabs) {
    this.tabs = tabs;
  }

  public String getViewMode() {
    return viewMode;
  }

  public void setViewMode(String viewMode) {
    this.viewMode = viewMode;
  }

  public String getItemStatus() {
    return itemStatus;
  }

  public void setItemStatus(String itemStatus) {
    this.itemStatus = itemStatus;
  }

  public String[] getPropertyArray() {
    if (this.propertyArray == null)
      this.propertyArray = new String[0];
    return propertyArray;
  }

  public String getPropertyArray(int index) {
    if (this.propertyArray == null) {
      this.propertyArray = new String[1];
    }

    if (index > this.propertyArray.length - 1) {
      String[] tmp = new String[index + 1];
      for (int ind = 0; ind < this.propertyArray.length; ind++) {
        tmp[ind] = this.propertyArray[ind];
      }
      this.propertyArray = tmp;
    }
    return propertyArray[index];
  }

  public void setPropertyArray(String[] checkboxArray) {
    this.propertyArray = propertyArray;
  }

  public void setPropertyArray(int index, String value) {
    if (this.propertyArray == null) {
      this.propertyArray = new String[1];
    }

    if (index > this.propertyArray.length - 1) {
      String[] tmp = new String[index + 1];
      for (int ind = 0; ind < this.propertyArray.length; ind++) {
        tmp[ind] = this.propertyArray[ind];
      }
      this.propertyArray = tmp;
    }
    this.propertyArray[index] = value;
  }

  public String[] getCheckboxArray() {
    return checkboxArray;
  }

  public void setCheckboxArray(String[] checkboxArray) {
    this.checkboxArray = checkboxArray;
  }

  public String getRefPageUrl() {
    return refPageUrl;
  }

  public void setRefPageUrl(String refPageUrl) {
    this.refPageUrl = refPageUrl;
  }

  public long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(long categoryId) {
    this.categoryId = categoryId;
  }

  public String getInstanceName() {
    return instanceName;
  }

  public void setInstanceName(String instanceName) {
    this.instanceName = instanceName;
  }

  public String getModuleName() {
    return moduleName;
  }

  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }

  public String getSiteName() {
    return siteName;
  }

  public void setSiteName(String siteName) {
    this.siteName = siteName;
  }

  public boolean isSendMessage() {
    return sendMessage;
  }

  public void setSendMessage(boolean sendMessage) {
    this.sendMessage = sendMessage;
  }

  public int getItemsPerPage() {
    if (itemsPerPage < 5) {
      itemsPerPage = 10;
    }
    return itemsPerPage;
  }

  public void setItemsPerPage(int itemsPerPage) {
    this.itemsPerPage = itemsPerPage;
  }

}