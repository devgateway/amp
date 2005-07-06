/*
 *   NewsAdminForm.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: NewsAdminForm.java,v 1.1 2005-07-06 10:34:31 rahul Exp $
 *
 *   @Author Maka Kharalashvili maka@digijava.org
 *   Created: Oct 10, 2003
 *   CVS-ID: $Id: NewsAdminForm.java,v 1.1 2005-07-06 10:34:31 rahul Exp $
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


package org.digijava.module.news.form;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.module.news.dbentity.NewsSettings;

public class NewsAdminForm
      extends ActionForm {

    public static class Item {

	private String value;

	public Item() {
	}

	public Item(String value) {
	    this.value = value;
	}

	public String getValue() {
	    return this.value;
	}

	public void setValue(String value) {
	    this.value = value;
	}
    }

    /**
     * instance of News settings
     */
    private NewsSettings setting;

    /**
     * collection containing numbers (up to ModuleInstance.NUMBER_OF_ITEMS_IN_TEASER value) to be selected as a  number of news visible in teaser
     */
    private Collection numberOfTeasers;

    /**
     * selected number of news appearing in teaser
     */
    private Long selectedNumOfItemsInTeaser;

    /**
     * number of charachters in visible title text of news item
     */
    private Long numOfCharsInTitle;

    /**
     * number of visible news per page
     */
    private Long numOfItemsPerPage;

    private String shortVersionDelimiter;
    //
    private String moduleName;
    private String instanceName;
    private String siteName;
    private String approve;
    private String reject;
    private String revoke;
    private String archive;

    public NewsAdminForm() {
    }

    public NewsSettings getSetting() {
	return setting;
    }

    public void setSetting(NewsSettings setting) {
	this.setting = setting;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
	if (setting != null) {
	    setting.setModerated(false);
	    setting.setSyndication(false);
	    setting.setPrivateItem(false);
	    setting.setApproveMessage(null);
	    setting.setSendApproveMessage(false);
	    setting.setArchiveMessage(null);
	    setting.setSendArchiveMessage(false);
	    setting.setRejectMessage(null);
	    setting.setSendRejectMessage(false);
	    setting.setRevokeMessage(null);
	    setting.setSendRevokeMessage(false);
	}

	setSelectedNumOfItemsInTeaser(new Long(ModuleInstance.
					       NUMBER_OF_ITEMS_IN_TEASER));
	numOfCharsInTitle = null;
	numOfItemsPerPage = null;

	shortVersionDelimiter = null;
    }

    public Collection getNumberOfTeasers() {
	return numberOfTeasers;
    }

    public void setNumberOfTeasers(Collection numberOfTeasers) {
	this.numberOfTeasers = numberOfTeasers;
    }

    public Long getSelectedNumOfItemsInTeaser() {
	return this.selectedNumOfItemsInTeaser;
    }

    public void setSelectedNumOfItemsInTeaser(Long selectedNumOfItemsInTeaser) {
	this.selectedNumOfItemsInTeaser = selectedNumOfItemsInTeaser;
    }

    public Long getNumOfCharsInTitle() {
	return numOfCharsInTitle;
    }

    public void setNumOfCharsInTitle(Long numOfCharsInTitle) {
	this.numOfCharsInTitle = numOfCharsInTitle;
    }

    public Long getNumOfItemsPerPage() {
	return numOfItemsPerPage;
    }

    public void setNumOfItemsPerPage(Long numOfItemsPerPage) {
	this.numOfItemsPerPage = numOfItemsPerPage;
    }

    public String getApprove() {
	return approve;
    }

    public void setApprove(String approve) {
	this.approve = approve;
    }

    public String getArchive() {
	return archive;
    }

    public void setArchive(String archive) {
	this.archive = archive;
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

    public String getReject() {
	return reject;
    }

    public void setReject(String reject) {
	this.reject = reject;
    }

    public String getRevoke() {
	return revoke;
    }

    public void setRevoke(String revoke) {
	this.revoke = revoke;
    }

    public String getSiteName() {
	return siteName;
    }

    public void setSiteName(String siteName) {
	this.siteName = siteName;
    }

    public String getShortVersionDelimiter() {
	return shortVersionDelimiter;
    }

    public void setShortVersionDelimiter(String shortVersionDelimiter) {
	this.shortVersionDelimiter = shortVersionDelimiter;
    }
}