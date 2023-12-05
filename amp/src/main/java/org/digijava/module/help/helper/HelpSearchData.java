package org.digijava.module.help.helper;

import java.util.Date;

public class HelpSearchData {

    
    private String titleTrnKey=null;
    private String topicKey;
    private String body;
    private Date lastModDate;
    private String lang;
    private String bodyKey;
    
    
    public String getBodyKey() {
        return bodyKey;
    }
    public void setBodyKey(String bodyKey) {
        this.bodyKey = bodyKey;
    }
    
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
    
    public String getLang() {
        return lang;
    }
    public void setLang(String lang) {
        this.lang = lang;
    }
    
}
