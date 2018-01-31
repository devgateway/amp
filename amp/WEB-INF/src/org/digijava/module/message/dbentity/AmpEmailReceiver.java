package org.digijava.module.message.dbentity;

import java.util.Date;

public class AmpEmailReceiver {
    private Long id;
    private String address;
    private AmpEmail email;
    private String status; //sent,etc
    private Date creationDate;
    
    public AmpEmailReceiver (){
        
    }
    
    public AmpEmailReceiver (String address,AmpEmail email, String status){
        this.address=address;
        this.email=email;
        this.status=status;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AmpEmail getEmail() {
        return email;
    }
    public void setEmail(AmpEmail email) {
        this.email = email;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
