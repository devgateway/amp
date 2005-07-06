/*
 *   CMSCategory.java
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

package org.digijava.module.cms.dbentity;

import java.util.HashSet;
import java.util.Set;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class CMSCategory {
  private long id;
  private String name;
  private String scopeNote;
  private CMSCategory primaryParent;
  private Set parentCategories;
  private Set childCategories;
  private Set primaryChildCategories;
  private Set relatedCategories;
  private CMS owner;
  private Date submissionDate;
  private Set contentItems;

  private List subCategories;

  public CMSCategory() {
    parentCategories = new HashSet();
    childCategories = new HashSet();
    relatedCategories = new HashSet();
    primaryChildCategories = new HashSet();
    contentItems = new HashSet();
    submissionDate = new Date();
  }

  public CMSCategory(CMSCategory primaryParent) {
    this.primaryParent = primaryParent;
    parentCategories = new HashSet();
    childCategories = new HashSet();
    relatedCategories = new HashSet();
    primaryChildCategories = new HashSet();
    contentItems = new HashSet();
    submissionDate = new Date();
  }

  public Set getChildCategories() {
    return childCategories;
  }

  public void setChildCategories(Set childCategories) {
    this.childCategories = childCategories;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CMS getOwner() {
    return owner;
  }

  public void setOwner(CMS owner) {
    this.owner = owner;
  }

  public Set getParentCategories() {
    return parentCategories;
  }

  public void setParentCategories(Set parentCategories) {
    this.parentCategories = parentCategories;
  }

  public CMSCategory getPrimaryParent() {
    return primaryParent;
  }

  public void setPrimaryParent(CMSCategory primaryParent) {
    this.primaryParent = primaryParent;
  }

  public Set getRelatedCategories() {
    return relatedCategories;
  }

  public void setRelatedCategories(Set relatedCategories) {
    this.relatedCategories = relatedCategories;
  }

  public String getScopeNote() {
    return scopeNote;
  }

  public void setScopeNote(String scopeNote) {
    this.scopeNote = scopeNote;
  }

  public Set getPrimaryChildCategories() {
    return primaryChildCategories;
  }

  public void setPrimaryChildCategories(Set primaryChildCategories) {
    this.primaryChildCategories = primaryChildCategories;
  }
  public Date getSubmissionDate() {
    return submissionDate;
  }
  public void setSubmissionDate(Date submissionDate) {
    this.submissionDate = submissionDate;
  }

  public List getSubCategories() {
    return subCategories;
  }

  public void setSubCategories(List subCategories) {
    this.subCategories = subCategories;
  }
  public Set getContentItems() {
    return contentItems;
  }
  public void setContentItems(Set contentItems) {
    this.contentItems = contentItems;
  }

}