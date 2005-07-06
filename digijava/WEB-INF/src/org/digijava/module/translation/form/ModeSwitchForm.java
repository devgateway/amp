/*
*   ModeSwitchForm.java
*   @Author Mikheil Kapanadze mikheil@digijava.org
*   Created:
*   CVS-ID: $Id: ModeSwitchForm.java,v 1.1 2005-07-06 10:34:20 rahul Exp $
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

package org.digijava.module.translation.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;

public class ModeSwitchForm
    extends ActionForm {

    private boolean translationMode;
    private String backUrl;
    private String translationType;

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public boolean isTranslationMode() {
        return translationMode;
    }

    public void setTranslationMode(boolean translationMode) {
        this.translationMode = translationMode;
    }

    public String getTranslationType() {
        return translationType;
    }

    public void setTranslationType(String translationType) {
        this.translationType = translationType;
    }

}