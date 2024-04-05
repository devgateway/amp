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

package org.digijava.module.translation.form;

import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.common.action.PaginationForm;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class AdvancedTranslationForm
    extends PaginationForm {

    public static int NUM_OF_MESSAGES_PER_PAGE = 25;

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
        private String key;
        private String value;

        public PrefixInfo() {}

        public PrefixInfo(String value) {
            this.value = value;
            this.key = value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return DgUtil.dehtmlize(DgUtil.decodeString(value));
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

    }

    private ArrayList keyPrefixes;
    private List messages;
    private List languages;
    private List dstLanguages;

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

    private boolean permitted;

    private List translations;
    private boolean showExpired;
    private String[] selectedKeys;
    private String expireSelected;
    private String unExpireSelected;
    private String expireKey;
    private String unexpireKey;
    private String rootSiteId;
    private boolean siteTranslation;
    private String switchToGlobal;
    private String switchToSite;
    private boolean globalTranslationGranted;
    private boolean groupTranslationGranted;
    private boolean localTranslationGranted;
    private String parameters;

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

    public boolean isPermitted() {
        return permitted;
    }

    public List getTranslations() {
        return translations;
    }

    public boolean isShowExpired() {
        return showExpired;
    }

    public void setShowExpired(boolean showExpired) {
        this.showExpired = showExpired;
    }

    public List getDstLanguages() {
        return dstLanguages;
    }

    public String[] getSelectedKeys() {
        return selectedKeys;
    }

    public String getExpireSelected() {
        return expireSelected;
    }

    public String getUnExpireSelected() {
        return unExpireSelected;
    }

    public String getExpireKey() {

        return expireKey;
    }

    public String getUnexpireKey() {
        return unexpireKey;
    }

    public String getRootSiteId() {
        return rootSiteId;
    }

    public boolean isSiteTranslation() {
        return siteTranslation;
    }

    public String getSwitchToGlobal() {
        return switchToGlobal;
    }

    public String getSwitchToSite() {
        return switchToSite;
    }

    public boolean isGlobalTranslationGranted() {
        return globalTranslationGranted;
    }

    public boolean isGroupTranslationGranted() {
        return groupTranslationGranted;
    }

    public boolean isLocalTranslationGranted() {
        return localTranslationGranted;
    }

    public String getParameters() {
        return parameters;
    }

    public void setDstLanguages(List dstLanguages) {
        this.dstLanguages = dstLanguages;
    }

    public void setPermitted(boolean permitted) {
        this.permitted = permitted;
    }

    public void setTranslations(List translations) {
        this.translations = translations;
    }

    public void setSelectedKeys(String[] selectedKeys) {
        this.selectedKeys = selectedKeys;
    }

    public void setExpireSelected(String expireSelected) {
        this.expireSelected = expireSelected;
    }

    public void setUnExpireSelected(String unExpireSelected) {
        this.unExpireSelected = unExpireSelected;
    }

    public void setExpireKey(String expireKey) {

        this.expireKey = expireKey;
    }

    public void setUnexpireKey(String unexpireKey) {
        this.unexpireKey = unexpireKey;
    }

    public void setRootSiteId(String rootSiteId) {
        this.rootSiteId = rootSiteId;
    }

    public void setSiteTranslation(boolean siteTranslation) {
        this.siteTranslation = siteTranslation;
    }

    public void setSwitchToGlobal(String switchToGlobal) {
        this.switchToGlobal = switchToGlobal;
    }

    public void setSwitchToSite(String switchToSite) {
        this.switchToSite = switchToSite;
    }

    public void setGlobalTranslationGranted(boolean globalTranslationGranted) {
        this.globalTranslationGranted = globalTranslationGranted;
    }

    public void setGroupTranslationGranted(boolean groupTranslationGranted) {
        this.groupTranslationGranted = groupTranslationGranted;
    }

    public void setLocalTranslationGranted(boolean localTranslationGranted) {
        this.localTranslationGranted = localTranslationGranted;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);

        keyPrefixes = null;
        messages = new ArrayList();
        languages = null;

        key = null;
        messageText = null;
        locale = null;

        siteName = null;

        showExpired = false;
        selectedKeys = null;

        expireSelected = null;
        unExpireSelected = null;
        expireKey = null;
        unexpireKey = null;

        searchKey = null;
        searchSource = null;
        searchTarget = null;
        rootSiteId = null;

        switchToGlobal = null;
    }
}
