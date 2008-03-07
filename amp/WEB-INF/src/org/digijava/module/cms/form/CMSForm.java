/*
 *   CMSForm.java
 *   @Author George Kvizhinadze gio@digijava.org
 *   Created: May 7, 2004
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

import org.apache.struts.action.ActionForm;
import org.digijava.module.cms.dbentity.CMSCategory;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.digijava.module.common.action.PaginationForm;

public class CMSForm
    extends PaginationForm {

  public static int MODE_EDIT = 1;
  public static int MODE_NEW = 2;

  public static int SELECT_CAT_FOR_PARENT = 1;
  public static int SELECT_CAT_FOR_RELATED = 2;
  public static int SELECT_CAT_FOR_CONTENT_ITEM = 3;

  private int processingMode;
  private int selType;

  private String[] propertyArray;
  private String[] nameArray;

  private String[] relatedIdArray;
  private String[] relatedNameArray;

  //Category properties
  private CMSCategory category;
  private long categoryId;
  private long parentCategoryId;
  private String categoryName;
  private String categoryScopeNote;
  private long primaryParentCategoryId;

  private long removeCategoryId;

  private List categoryList;
  private List contentItemList;
  private long itemId;
  private String categoryTreeXml;

  private List parentCategories;
  private List relatedCategories;
  private CMSCategory primaryParentCategory;

  private List bradcrampItems;

  private int itemsPerPage;
  private int currentOffset;

  private String refPageUrl;

  public ActionErrors validate(
          ActionMapping actionMapping,
          HttpServletRequest httpServletRequest) {

          ActionErrors errors = super.validate(actionMapping, httpServletRequest);
          if( errors == null )
              errors = new ActionErrors();

          if (actionMapping.getType().equals("org.digijava.module.cms.action.SaveCategory")) {
            if (this.categoryName == null ||
                this.categoryName.trim().length()==0) {
              errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("cms.error.categoryNameCanNotBeEmpty"));
            }
          }

          return errors;
      }


  public void setCategoryProperties(CMSCategory category) {
    this.categoryId = category.getId();
    this.categoryName = category.getName();
    this.categoryScopeNote = category.getScopeNote();
  }

  public void fillCategoryProperties(CMSCategory category) {
    category.setName(this.categoryName);
    category.setScopeNote(this.categoryScopeNote);
  }

  public CMSCategory getCategory() {
    return category;
  }

  public void setCategory(CMSCategory category) {
    this.category = category;
  }

  public long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(long categoryId) {
    this.categoryId = categoryId;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public String getCategoryScopeNote() {
    return categoryScopeNote;
  }

  public void setCategoryScopeNote(String categoryScopeNote) {
    this.categoryScopeNote = categoryScopeNote;
  }

  public List getCategoryList() {
    return categoryList;
  }

  public void setCategoryList(List categoryList) {
    this.categoryList = categoryList;
  }

  public String getCategoryTreeXml() {
    return categoryTreeXml;
  }

  public void setCategoryTreeXml(String categoryTreeXml) {
    this.categoryTreeXml = categoryTreeXml;
  }

  public long getParentCategoryId() {
    return parentCategoryId;
  }

  public void setParentCategoryId(long parentCategoryId) {
    this.parentCategoryId = parentCategoryId;
  }

  public List getParentCategories() {
    return parentCategories;
  }

  public void setParentCategories(List parentCategories) {
    this.parentCategories = parentCategories;
  }

  public CMSCategory getPrimaryParentCategory() {
    return primaryParentCategory;
  }

  public void setPrimaryParentCategory(CMSCategory primaryParentCategory) {
    this.primaryParentCategory = primaryParentCategory;
  }

  public List getRelatedCategories() {
    return relatedCategories;
  }

  public void setRelatedCategories(List relatedCategories) {
    this.relatedCategories = relatedCategories;
  }

  public int getProcessingMode() {
    return processingMode;
  }

  public void setProcessingMode(int processingMode) {
    this.processingMode = processingMode;
  }

  public long getPrimaryParentCategoryId() {
    return primaryParentCategoryId;
  }

  public void setPrimaryParentCategoryId(long primaryParentCategoryId) {
    this.primaryParentCategoryId = primaryParentCategoryId;
  }

  public String[] getPropertyArray() {
    if (this.propertyArray == null)
      this.propertyArray = new String[0];
    return propertyArray;
  }

  public void setPropertyArray(String[] propertyArray) {
    this.propertyArray = propertyArray;
  }

  public String getPropertyArray(int index) {
    return this.propertyArray[index];
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

  public String[] getNameArray() {
    if (this.nameArray == null)
      this.nameArray = new String[0];
    return nameArray;
  }

  public void setNameArray(String[] nameArray) {
    this.nameArray = nameArray;
  }

  public String getNameArray(int index) {
    return this.nameArray[index];
  }

  public void setNameArray(int index, String value) {
    if (this.nameArray == null) {
      this.nameArray = new String[1];
    }
    if (index > this.nameArray.length - 1) {
      String[] tmp = new String[index + 1];
      for (int ind = 0; ind < this.nameArray.length; ind++) {
        tmp[ind] = this.nameArray[ind];
      }
      this.nameArray = tmp;
    }

    this.nameArray[index] = value;
  }

  public String[] getRelatedIdArray() {
    if (this.relatedIdArray == null)
      this.relatedIdArray = new String[0];
    return relatedIdArray;
  }

  public String getRelatedIdArray(int index) {
    return relatedIdArray[index];
  }

  public void setRelatedIdArray(String[] relatedIdArray) {
    this.relatedIdArray = relatedIdArray;
  }

  public void setRelatedIdArray(int index, String value) {
    if (this.relatedIdArray == null) {
      this.relatedIdArray = new String[1];
    }

    if (index > this.relatedIdArray.length - 1) {
      String[] tmp = new String[index + 1];
      for (int ind = 0; ind < this.relatedIdArray.length; ind++) {
        tmp[ind] = this.relatedIdArray[ind];
      }
      this.relatedIdArray = tmp;
    }
    this.relatedIdArray[index] = value;
  }

  public String[] getRelatedNameArray() {
    if (this.relatedNameArray == null)
      this.relatedNameArray = new String[0];
    return relatedNameArray;
  }

  public String getRelatedNameArray(int index) {
    return relatedNameArray[index];
  }

  public void setRelatedNameArray(String[] relatedNameArray) {
    this.relatedNameArray = relatedNameArray;
  }

  public void setRelatedNameArray(int index, String value) {
    if (this.relatedNameArray == null) {
      this.relatedNameArray = new String[1];
    }
    if (index > this.relatedNameArray.length - 1) {
      String[] tmp = new String[index + 1];
      for (int ind = 0; ind < this.relatedNameArray.length; ind++) {
        tmp[ind] = this.relatedNameArray[ind];
      }
      this.relatedNameArray = tmp;
    }

    this.relatedNameArray[index] = value;
  }

  public int getSelType() {
    return selType;
  }

  public void setSelType(int selType) {
    this.selType = selType;
  }

  //
  public void reset(ActionMapping mapping, HttpServletRequest request) {

    super.reset(mapping, request);

    categoryList = null;
    contentItemList = null;

    categoryId = 0;
    itemId = 0;
  }

  public long getRemoveCategoryId() {
    return removeCategoryId;
  }

  public void setRemoveCategoryId(long removeCategoryId) {
    this.removeCategoryId = removeCategoryId;
  }

  public List getContentItemList() {
    return contentItemList;
  }

  public void setContentItemList(List contentItemList) {
    this.contentItemList = contentItemList;
  }

  public long getItemId() {
    return itemId;
  }

  public void setItemId(long itemId) {
    this.itemId = itemId;
  }
  public List getBradcrampItems() {
    return bradcrampItems;
  }
  public void setBradcrampItems(List bradcrampItems) {
    this.bradcrampItems = bradcrampItems;
  }
  public int getCurrentOffset() {
    return currentOffset;
  }
  public void setCurrentOffset(int currentOffset) {
    this.currentOffset = currentOffset;
  }
  public int getItemsPerPage() {
    if (itemsPerPage<5) {
      //Default
      itemsPerPage = 10;
    }
    return itemsPerPage;
  }
  public void setItemsPerPage(int itemsPerPage) {
    this.itemsPerPage = itemsPerPage;
  }
  public String getRefPageUrl() {
    return refPageUrl;
  }
  public void setRefPageUrl(String refPageUrl) {
    this.refPageUrl = refPageUrl;
  }


}