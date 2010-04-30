package org.digijava.module.contentrepository.helper;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class DocumentData implements Comparable<DocumentData>{
	String name				= null;
	String uuid				= null;
	String title			= null;
	String description		= null;
	String notes			= null;
	String calendar			= null;
	String contentType		= null;
	String webLink			= null;
	String cmDocType		= "";
	double fileSize			= 0;
	Calendar date = null;
	String iconPath			= null;
	
	Long cmDocTypeId		= null;
	
	float versionNumber;
	
	boolean hasDeleteRights			= false;
	boolean hasViewRights				= false;
	boolean hasShowVersionsRights	= false;
	boolean hasVersioningRights		= false;
	boolean hasMakePublicRights		= false;
	boolean hasDeleteRightsOnPublicVersion	= false;
	
	boolean isPublic					= false;
	boolean lastVersionIsPublic		= false;
	
	boolean showVersionHistory		= true;
	
	public boolean getHasDeleteRights() {
		return hasDeleteRights;
	}
	public void setHasDeleteRights(boolean hasDeleteRights) {
		this.hasDeleteRights = hasDeleteRights;
	}
	
	public boolean getHasShowVersionsRights() {
		return hasShowVersionsRights;
	}
	public void setHasShowVersionsRights(boolean hasShowVersionsRights) {
		this.hasShowVersionsRights = hasShowVersionsRights;
	}
	public boolean getHasVersioningRights() {
		return hasVersioningRights;
	}
	public void setHasVersioningRights(boolean hasVersioningRights) {
		this.hasVersioningRights = hasVersioningRights;
	}
	public boolean getHasViewRights() {
		return hasViewRights;
	}
	public void setHasViewRights(boolean hasViewRights) {
		this.hasViewRights = hasViewRights;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getCalendar() {
		return calendar;
	}
	public void setCalendar(String calendar) {
		this.calendar = calendar;
	}
	public String getEscapedAmpDescription() {
		String ret = description.replace("'", "\\'").replace("\r", "").replace("\n", "\\n");
		return ret;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEscapedAmpTitle() {
		return title.replace("'", "\\'");
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public boolean getHasMakePublicRights() {
		return hasMakePublicRights;
	}
	public void setHasMakePublicRights(boolean hasMakePublicRights) {
		this.hasMakePublicRights = hasMakePublicRights;
	}
	public boolean getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
			
	public boolean isLastVersionIsPublic() {
		return lastVersionIsPublic;
	}
	public void setLastVersionIsPublic(boolean lastVersionIsPublic) {
		this.lastVersionIsPublic = lastVersionIsPublic;
	}
	public boolean isHasDeleteRightsOnPublicVersion() {
		return hasDeleteRightsOnPublicVersion;
	}
	public void setHasDeleteRightsOnPublicVersion(boolean hasDeleteRightsOnPublicVersion) {
		this.hasDeleteRightsOnPublicVersion = hasDeleteRightsOnPublicVersion;
	}
	public boolean isShowVersionHistory() {
		return showVersionHistory;
	}
	public void setShowVersionHistory(boolean showVersionHistory) {
		this.showVersionHistory = showVersionHistory;
	}
	public float getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(float versionNumber) {
		this.versionNumber = versionNumber;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getIconPath() {
		return iconPath;
	}
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	
	
	public Long getCmDocTypeId() {
		return cmDocTypeId;
	}
	public void setCmDocTypeId(Long cmDocTypeId) {
		this.cmDocTypeId = cmDocTypeId;
	}
	public String getCmDocType() {
		return cmDocType;
	}
/*	public void setCmDocType(String cmDocType) { // This is not needed
		this.cmDocType = cmDocType;
	}*/
	public double getFileSize() {
		return fileSize;
	}
	public void setFileSize(double fileSize) {
		this.fileSize = fileSize;
	}
			
	public String getWebLink() {
		return webLink;
	}
	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}
	
	public void process(HttpServletRequest request) {
		if (cmDocTypeId != null) {
			AmpCategoryValue docTypeCv	= CategoryManagerUtil.getAmpCategoryValueFromDb(cmDocTypeId);
			if ( docTypeCv != null ) {
				String translation		= CategoryManagerUtil.translateAmpCategoryValue(docTypeCv, request);
				cmDocType				= translation;
			}
		}
		if ( webLink != null ) {
			if ( webLink.length() <= 25 )
				name	= webLink;
			else {
				name	= webLink.substring(0, 22) + "...";
			}
		}
	}
	
	public void computeIconPath( boolean forDigiImgTag ) {
		if ( name == null )  {
			iconPath = null;
			return;
		}
		String iconPath 	= "";
		String extension	= null;
		if ( webLink == null ) {
			int index 			= name.lastIndexOf(".");
			extension			= name.substring(index + 1,	name.length()) ;
		}
		else 
			extension		 	= "link";
		if ( extension != null) {
			if (forDigiImgTag) {
				iconPath = "images/icons/"
						+ extension
						+ ".gif";
			}
			else
				iconPath = "/TEMPLATE/ampTemplate/images/icons/"
					+ extension
					+ ".gif";
		}
		
		this.iconPath	= iconPath;
		
		
	}
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
	
	
	public int compareTo(DocumentData o) {
		return this.getDate().compareTo(o.date);
	}
	
	
}
