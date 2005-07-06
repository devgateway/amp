/*
 *   EditLocales.java
 * 	 @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Sep 20, 2003
 * 	 CVS-ID: $Id: EditLocales.java,v 1.1 2005-07-06 10:34:09 rahul Exp $
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

package org.digijava.module.admin.action;

import java.util.List;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.module.admin.form.LocalesForm;
import org.digijava.module.admin.util.DbUtil;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class EditLocales
    extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws
        java.lang.Exception {

        LocalesForm localesForm = (LocalesForm) form;
        List locales = localesForm.getLocales();


        Iterator iterator = locales.iterator();
        while (iterator.hasNext()) {
            Locale currentLocale = (Locale) iterator.next();
            if (currentLocale != null) {
                DbUtil.updateLocale( currentLocale );
            }
        }

        return mapping.findForward("forward");
    }
}