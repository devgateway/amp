/*
 *   UserContactForm.java
 *   @Author Lasha Dolidze lasha@digijava.org
 *   Created:
 *   CVS-ID: $Id: UserContactForm.java,v 1.1 2005-07-06 10:34:17 rahul Exp $
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

package org.digijava.module.um.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.validator.ValidatorForm;
import java.util.Set;
import java.util.List;
import java.util.Collection;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UserContactForm
    extends ValidatorForm {

    private String senderName;
    private String recipientName;
    private String senderEmail;
    private String recipientEmail;
    private String subject;
    private String message;
    private boolean copytomyself;
    private Long activeUserId;
    private Collection contentLanguages;
    private String selectedLanguage;

    public boolean isCopytomyself() {
        return copytomyself;
    }

    public String getMessage() {
        return message;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSubject() {
        return subject;
    }

    public void setCopytomyself(boolean copytomyself) {
        this.copytomyself = copytomyself;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getActiveUserId() {
        return activeUserId;
    }

    public void setActiveUserId(Long activeUserId) {
        this.activeUserId = activeUserId;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        setCopytomyself(false);
        setSenderName(null);
        setRecipientName(null);
        setSenderEmail(null);
        setRecipientEmail(null);
        setSubject(null);
        setMessage(null);
        setSelectedLanguage(null);
    }

    public Collection getContentLanguages() {
        return contentLanguages;
    }

    public void setContentLanguages(Collection contentLanguages) {
        this.contentLanguages = contentLanguages;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

}