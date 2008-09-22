package org.digijava.module.help.helper;

import java.util.Date;

public class HelpSearchData {

	private String titleTrnKey=null;
	private String topicKey;
	private String body;
	private Date lastModDate;
	
	public String getTitleTrnKey() {
		return titleTrnKey;
	}
	public void setTitleTrnKey(String titleTrnKey) {
		this.titleTrnKey = titleTrnKey;
	}
	public String getTopicKey() {
		return topicKey;
	}
	public void setTopicKey(String topicKey) {
		this.topicKey = topicKey;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Date getLastModDate() {
		return lastModDate;
	}
	public void setLastModDate(Date lastModDate) {
		this.lastModDate = lastModDate;
	}
	
}
