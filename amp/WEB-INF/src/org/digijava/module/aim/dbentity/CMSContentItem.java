/*
 *   CMSContentItem.java
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

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;

import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.user.User;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class CMSContentItem implements Serializable 
{
	private long id;
	private String title;
	private String description;
	private String docComment;
	private String url;
	private String fileName;
	private String date;
	private byte[] file;
	private String contentType;
	private boolean isFile;
	private Locale language;
	private Country country;
	private Date submissionDate;
	private User authorUser;
	private boolean published;
	private boolean rejected;

	private AmpCategoryValue docType;

	private AmpCategoryValue docLanguage;

	public AmpCategoryValue getDocType() {
		return docType;
	}
	public void setDocType(AmpCategoryValue docType) {
		this.docType = docType;
	}
	public CMSContentItem() {
		submissionDate = new Date();
	}

	public CMSContentItem(String title) {
		this.title = title;
		submissionDate = new Date();
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Locale getLanguage() {
		return language;
	}

	public void setLanguage(Locale language) {
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

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean getIsFile() {
		return isFile;
	}

	public void setIsFile(boolean isFile) {
		this.isFile = isFile;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}
	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}
	public User getAuthorUser() {
		return authorUser;
	}
	public void setAuthorUser(User authorUser) {
		this.authorUser = authorUser;
	}
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}
	public String getDocComment() {
		return docComment;
	}
	public void setDocComment(String docComment) {
		this.docComment = docComment;
	}
	public AmpCategoryValue getDocLanguage() {
		return docLanguage;
	}
	public void setDocLanguage(AmpCategoryValue docLanguage) {
		this.docLanguage = docLanguage;
	}


}