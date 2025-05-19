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
