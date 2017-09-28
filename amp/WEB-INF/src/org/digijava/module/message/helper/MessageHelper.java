package org.digijava.module.message.helper;

import java.util.List;

import org.digijava.module.sdm.dbentity.Sdm;

public class MessageHelper {    
    private Long msgId;
    private String name;
    private Long priorityLevel;
    private String description;
    private String creationDate;
    private String from;
    private List<String> receivers;
    private String objectURL;
    private Sdm attachedDocs;
    
    public MessageHelper(){
        
    }
    
    public MessageHelper(Long id,String name,String description){
        this.msgId=id;
        this.name=name;
        this.description=description;       
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getPriorityLevel() {
        return priorityLevel;
    }
    public void setPriorityLevel(Long priorityLevel) {
        this.priorityLevel = priorityLevel;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public List<String> getReceivers() {
        return receivers;
    }
    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }
    public String getObjectURL() {
        return objectURL;
    }

    public void setObjectURL(String objectURL) {
        this.objectURL = objectURL;
    }

    public Sdm getAttachedDocs() {
        return attachedDocs;
    }

    public void setAttachedDocs(Sdm attachedDocs) {
        this.attachedDocs = attachedDocs;
    }
    
}
