package org.digijava.module.message.dbentity;

import java.util.Set;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "AMP_EMAIL")
public class AmpEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_EMAIL_seq")
    @SequenceGenerator(name = "AMP_EMAIL_seq", sequenceName = "AMP_EMAIL_seq", allocationSize = 1)
    @Column(name = "email_Id")
    private Long id;

    @Column(name = "subject", columnDefinition = "text")
    private String subject;

    @Column(name = "body", columnDefinition = "text")
    private String body;

    @Column(name = "sender")
    private String sender;

    @Column(name = "receivers", columnDefinition = "text")
    private String receivers;

    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL, orphanRemoval = true)
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
