/*
 *   LocalesForm.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Oct 09, 2003
 * 	 CVS-ID: $Id: LocalesForm.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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

package org.digijava.module.admin.form;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class LocalesForm
    extends ActionForm {

    private List locales;

    public List getLocales() {
        return locales;
    }

    public void setLocales(List locales) {
        this.locales = locales;
    }

    public Locale getLocale(int index) {
        Locale locale = null;
        int currentSize = locales.size();
        if (index >= currentSize) {
            for (int i = 0; i <= index - currentSize; i++) {
                locales.add(new Locale());
            }
        }

        return (Locale) locales.get(index);
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        locales = new ArrayList();
    }

}