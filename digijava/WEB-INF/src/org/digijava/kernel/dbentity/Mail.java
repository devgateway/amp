/*
*   Mail.java
*   @Author
*   Created:
*   CVS-ID: $Id: Mail.java,v 1.1 2005-07-06 10:34:18 rahul Exp $
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
package org.digijava.kernel.dbentity;

import java.util.Date;

/**
 * The value object class for the Countries. Maps to the table PL_COUNTRIES
 *
 */
public class Mail {

    /** identifier field */
    private long id;
    private String to;
    private String from;
    private String bcc;
    private String cc;
    private String subject;
    private String text;
    private Date date;

    /**
     * Empty Constructor
     */

    public Mail() {
        this.id = 0;
        this.to = "";
        this.from = "";
        this.bcc = "";
        this.cc = "";
        this.subject = "";
        this.text = "";
        this.date = new Date();

    }

    /**
     * Constructor with the mail object passed
     */

    public Mail(Mail mail) {
        this.id = mail.id;
        this.to = mail.to;
        this.from = mail.from;
        this.bcc = mail.bcc;
        this.cc = mail.cc;
        this.subject = mail.subject;
        this.text = mail.text;
        this.date = mail.date;
    }

    /**
     * @return
     */
    public String getBcc() {
        return bcc;
    }

    /**
     * @return
     */
    public String getCc() {
        return cc;
    }

    /**
     * @return
     */
    public String getFrom() {
        return from;
    }

    /**
     * @return
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * @return
     */
    public String getTo() {
        return to;
    }

    /**
     * @param string
     */
    public void setBcc(String string) {
        bcc = string;
    }

    /**
     * @param string
     */
    public void setCc(String string) {
        cc = string;
    }

    /**
     * @param string
     */
    public void setFrom(String string) {
        from = string;
    }

    /**
     * @param string
     */
    public void setSubject(String string) {
        subject = string;
    }

    /**
     * @param string
     */
    public void setText(String string) {
        text = string;
    }

    /**
     * @param string
     */
    public void setTo(String string) {
        to = string;
    }

    /**
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * @param l
     */
    public void setId(long l) {
        id = l;
    }

}