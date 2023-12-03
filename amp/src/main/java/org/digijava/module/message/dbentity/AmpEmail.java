package org.digijava.module.message.dbentity;

import java.util.Set;

public class AmpEmail {
    private Long id;
    private String sender;
    private String receivers; // all receivers
    private String subject;
    private String body;        
    private Set<AmpEmailReceiver> receiver;
    
    public AmpEmail(){
        
    }
    
    public AmpEmail(String sender,String subject,String body){
        this.sender=sender;
        this.subject=subject;
        this.body=body;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getReceivers() {
        return receivers;
    }
    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }
    public String getSubject() {
        return subject;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public Set<AmpEmailReceiver> getReceiver() {
        return receiver;
    }
    public void setReceiver(Set<AmpEmailReceiver> receiver) {
        this.receiver = receiver;
    }
    
}
