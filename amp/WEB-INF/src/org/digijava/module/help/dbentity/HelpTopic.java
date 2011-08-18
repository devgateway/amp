package org.digijava.module.help.dbentity;

import java.io.Serializable;

public class HelpTopic implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long helpTopicId=null;
	private String keywordsTrnKey=null;
	private String titleTrnKey=null;
	private HelpTopic parent=null;
	private String bodyEditKey=null;
	private String siteId=null;
	private String moduleInstance=null;
	private String topicKey;
	private Integer topicType;	
	
	public String getBodyEditKey() {
		return bodyEditKey;
	}
	public void setBodyEditKey(String bodyEditKey) {
		this.bodyEditKey = bodyEditKey;
	}
	public Long getHelpTopicId() {
		return helpTopicId;
	}
	public void setHelpTopicId(Long helpTopicId) {
		this.helpTopicId = helpTopicId;
	}

	public String getTopicKey() {
		return topicKey;
	}
	public void setTopicKey(String topicKey) {
		this.topicKey = topicKey;
	}
	public String getKeywordsTrnKey() {
		return keywordsTrnKey;
	}
	public void setKeywordsTrnKey(String keywordsTrnKey) {
		this.keywordsTrnKey = keywordsTrnKey;
	}
	public String getModuleInstance() {
		return moduleInstance;
	}
	public void setModuleInstance(String moduleInstance) {
		this.moduleInstance = moduleInstance;
	}
	public HelpTopic getParent() {
		return parent;
	}
	public void setParent(HelpTopic parent) {
		this.parent = parent;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getTitleTrnKey() {
		return titleTrnKey;
	}
	public void setTitleTrnKey(String titleTrnKey) {
		this.titleTrnKey = titleTrnKey;
	}
	public void setTopicType(Integer topicType) {
		this.topicType = topicType;
	}
	public Integer getTopicType() {
		return topicType;
	}



}
