/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.helper;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.contentrepository.jcrentity.Label;
/**
 * 
 * @author aartimon@dginternational.org
 * @since Apr 14, 2011
 */
public class TemporaryDocument implements Serializable {
	private String title;
	private String description;
	private String note;
	private AmpCategoryValue type;
	private FileUpload file;
	private Calendar date;
	private String year;
	private boolean existing;
	private AmpActivityDocument existingDocument;
	private String webLink;
	private double fileSize;
	private String fileName;
	private List<Label> labels;
	private String contentType;
	
	public TemporaryDocument() {
		existing = false;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public AmpCategoryValue getType() {
		return type;
	}

	public void setType(AmpCategoryValue type) {
		this.type = type;
	}

	public FileUpload getFile() {
		return file;
	}

	public void setFile(FileUpload file) {
		this.file = file;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public boolean isExisting() {
		return existing;
	}

	public void setExisting(boolean existing) {
		this.existing = existing;
	}

	public AmpActivityDocument getExistingDocument() {
		return existingDocument;
	}

	public void setExistingDocument(AmpActivityDocument existingDocument) {
		this.existingDocument = existingDocument;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public String getWebLink() {
		return webLink;
	}

	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}

	public double getFileSize() {
		return fileSize;
	}

	public void setFileSize(double fileSize) {
		this.fileSize = fileSize;
	}

	public void setLabels(List<Label> labels) {
		this.labels = labels;		
	}

	public List<Label> getLabels() {
		return labels;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}

	
	
}
