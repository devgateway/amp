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

package org.digijava.kernel.entity;

import java.util.Date;

public class MailSpool {

    private Long id;
    private String senderAddress;
    private String replayToSender;
    private String subject;
    private String body;
    private Date dateLastSend;
    private Date dateSend;
    private String error;
    private String cc;
    private String bcc;
    private boolean html;
    private String charset;

    public MailSpool() {
    }

    public String getBody() {
        return body;
    }

    public Date getDateLastSend() {
        return dateLastSend;
    }

    public Date getDateSend() {
        return dateSend;
    }

    public String getError() {
        return error;
    }

    public Long getId() {
        return id;
    }

    public String getReplayToSender() {
        return replayToSender;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public String getSubject() {
        return subject;
    }

    public String getBcc() {
        return bcc;
    }

    public String getCc() {
        return cc;
    }

    public String getCharset() {
        return charset;
    }

    public boolean isHtml() {
        return html;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDateLastSend(Date dateLastSend) {
        this.dateLastSend = dateLastSend;
    }

    public void setDateSend(Date dateSend) {
        this.dateSend = dateSend;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReplayToSender(String replayToSender) {
        this.replayToSender = replayToSender;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

}
