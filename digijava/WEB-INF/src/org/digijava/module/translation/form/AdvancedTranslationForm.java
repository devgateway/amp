/*
 *   AdvancedTranslationForm.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Sept 28, 2004
 * 	 CVS-ID: $Id: AdvancedTranslationForm.java,v 1.1 2005-07-06 10:34:20 rahul Exp $
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

package org.digijava.module.translation.form;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.digijava.module.common.action.PaginationForm;

public class AdvancedTranslationForm
      extends PaginationForm {

    public static int  NUM_OF_MESSAGES_PER_PAGE = 25;

    public static class MessageInfo {
	private String key;
	private String sourceValue;
	private String targetValue;
	private boolean translation;

	private String encodedKey;

	public void setKey(String key) {
	    this.key = key;
	}

	public String getKey() {
	    return key;
	}

	public void setEncodedKey(String encodedKey) {
	    this.encodedKey = encodedKey;
	}

	public String getEncodedKey() {
	    return encodedKey;
	}

	public void setSourceValue(String sourceValue) {
	    this.sourceValue = sourceValue;
	}

	public String getSourceValue() {
	    return sourceValue;
	}

	public void setTargetValue(String targetValue) {
	    this.targetValue = targetValue;
	}

	public String getTargetValue() {
	    return targetValue;
	}

	public void setTranslation(boolean translation) {
	    this.translation = translation;
	}

	public boolean isTranslation() {
	    return translation;
	}
    }

    public static class PrefixInfo {
	private String value;

	public PrefixInfo() {}

	public PrefixInfo(String value) {
	    this.value = value;
	}

	public void setValue(String value) {
	    this.value = value;
	}

	public String getValue() {
	    return value;
	}
    }

    private ArrayList keyPrefixes;
    private List messages;
    private List languages;

    private String selectedLangSource;
    private String selectedLangTarget;
    private String selectedPrefix;

    private boolean addKey;
    private String key;
    private String messageText;
    private String locale;

    private String searchKey;
    private String searchSource;
    private String searchTarget;

    private String siteName;
    private Long siteId;
    private boolean globalTranslation;

    public ArrayList getKeyPrefixes() {
	return keyPrefixes;
    }

    public void setKeyPrefixes(ArrayList keyPrefixes) {
	this.keyPrefixes = keyPrefixes;
    }

    public List getMessages() {
	return messages;
    }

    public void setMessages(List messages) {
	this.messages = messages;
    }

    public List getLanguages() {
	return languages;
    }

    public void setLanguages(List languages) {
	this.languages = languages;
    }

    public MessageInfo getMessage(int index) {
	int currentSize = messages.size();
	if (index >= currentSize) {
	    for (int i = 0; i <= index - currentSize; i++) {
		messages.add(new MessageInfo());
	    }
	}
	if (messages == null) {
	    boolean enterhere = true;
	}
	if (messages.get(index) == null) {
	    boolean comehere = true;
	}
	return (MessageInfo) messages.get(index);
    }

    public String getSelectedPrefix() {
	return selectedPrefix;
    }

    public void setSelectedPrefix(String selectedPrefix) {
	this.selectedPrefix = selectedPrefix;
    }

    public String getMessageText() {
	return messageText;
    }

    public void setMessageText(String messageText) {
	this.messageText = messageText;
    }

    public String getLocale() {
	return locale;
    }

    public void setLocale(String locale) {
	this.locale = locale;
    }

    public String getSelectedLangTarget() {
	return selectedLangTarget;
    }

    public void setSelectedLangTarget(String selectedLangTarget) {
	this.selectedLangTarget = selectedLangTarget;
    }

    public String getSelectedLangSource() {
	return selectedLangSource;
    }

    public void setSelectedLangSource(String selectedLangSource) {
	this.selectedLangSource = selectedLangSource;
    }

    public String getSearchSource() {
	return searchSource;
    }

    public void setSearchSource(String searchSource) {
	this.searchSource = searchSource;
    }

    public String getSearchTarget() {
	return searchTarget;
    }

    public void setSearchTarget(String searchTarget) {
	this.searchTarget = searchTarget;
    }

    public String getSearchKey() {
	return searchKey;
    }

    public void setSearchKey(String searchKey) {
	this.searchKey = searchKey;
    }

    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public boolean isGlobalTranslation() {
	return globalTranslation;
    }

    public void setGlobalTranslation(boolean globalTranslation) {
	this.globalTranslation = globalTranslation;
    }

    public String getSiteName() {
	return siteName;
    }

    public void setSiteName(String siteName) {
	this.siteName = siteName;
    }

    public Long getSiteId() {
	return siteId;
    }

    public void setSiteId(Long siteId) {
	this.siteId = siteId;
    }

    public boolean isAddKey() {
	return addKey;
    }

    public void setAddKey(boolean addKey) {
	this.addKey = addKey;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
	super.reset(mapping,request);

	keyPrefixes = null;
	messages = new ArrayList();
	languages = null;

	key = null;
	messageText = null;
	locale = null;

	siteName = null;
    }
}