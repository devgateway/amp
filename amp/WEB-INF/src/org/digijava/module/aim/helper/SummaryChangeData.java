package org.digijava.module.aim.helper;

import java.util.Date;

/**
 * @author Aldo Picca
 */
public class SummaryChangeData {
    private String email;
    private String body;
    private Date date;
    private String subject;
    private String bodyHeader;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBodyHeader(String bodyHeader) {
        this.bodyHeader = bodyHeader;
    }

    public String getBodyHeader() {
        return bodyHeader;
    }
}
